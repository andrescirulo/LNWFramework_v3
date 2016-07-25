package net.latin.server;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.latin.client.exceptions.LnwTransactionException;
import net.latin.client.rpc.GwtRpcInterface;
import net.latin.server.persistence.UserContext;
import net.latin.server.persistence.user.LnwUser;
import net.latin.server.utils.exceptions.LnwSecurityException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;

/**
 * Template básico para crear un UseCase. Es el encargado de atender los pedidos
 * RPC realizados por los métodos de una interfaz Client. <br>
 * Maneja la seguridad de acceso por cada pedido RPC realizado. Administra el
 * contexto de session. Loggea errores.
 * 
 * <br>
 * Extender esta clase para cada caso de uso.
 * 
 * @author Matias Leone
 */
public abstract class GwtUseCase extends GwtBaseAction implements GwtRpcInterface {

	public final static Log LOG = LogFactory.getLog(GwtUseCase.class);
	private static final String GWT_USE_CASE_CONTEXT_NAME = "__gwtUseCaseContext";
	public final static String LNW_TRANSACTION_EXCEPTION_KEY = "__LnwTransactionException";

	@Override
	protected void onBeforeProcessCall() {
		UserContext userContext = UserContext.getInstance();

		// manejar el contexto de la session
		manageSessionContext(userContext);

		// checkeamos que el usuario tenga permiso para acceder al caso de uso
		checkAccess(userContext.getUser());

	}

	/**
	 * Verifica que halla creado un contexto en la session. Si existe se agrega
	 * al ThreadLocal (UserContext). Si no existe se crea, se agrega a la
	 * session para la próxima vez.
	 */
	private void manageSessionContext(UserContext userContext) {
		HttpServletRequest request = userContext.getRequest();
		HttpSession session = request.getSession();

		// Reset the context
		Object obj;

		/**
		 * Manejamos el contexto, sacandolo de la session
		 */

		// traer contexto existente de la session
		obj = session.getAttribute(GWT_USE_CASE_CONTEXT_NAME);

		// si existe de antes
		if (obj != null) {
			userContext.setGwtContext((GwtContext) obj);

			// si no existe un contexto, lo creamos y lo agregamos a la session
		} else {
			GwtContext context = userContext.resetContext();
			context.setGwtUseCaseClass(this.getClass());
			session.setAttribute(GWT_USE_CASE_CONTEXT_NAME, context);
		}

	}

	@Override
	protected void doUnexpectedFailure(Throwable t) {
		// crear código de error único para loguearlo en ambos lados (client y
		// server)
		String errorCode = generateErrorCode();

		// get the wrapped exception
		Throwable realThrowable = t.getCause();

		// log
		if (realThrowable == null) {
			logException(t, errorCode);
		} else {
			logException(realThrowable, errorCode);
		}

		// escribir error code en Response para que le llegue al cliente
		try {
			HttpServletResponse response = UserContext.getInstance().getResponse();
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(errorCode);
		} catch (IOException e) {
			logException(e, "Error al intentar escribir ErrorCode en Response");
		}

	}

	/**
	 * Log an unhalded exception with a random generated error code
	 * 
	 * @param throwable
	 */
	public void logException(Throwable throwable, String errorCode) {
		// log error
		LnwUser user = UserContext.getInstance().getUser();
		long userId;
		String userName;
		if (user == null) {
			userId = -1;
			userName = null;
		} else {
			userId = user.getId();
			userName = user.getNombreCompleto();
		}
		Log logger = getLog();

		logger.error("ERROR CODE: " + errorCode + " Exception grave lanzada en el GwtUseCase: "
				+ this.getClass().getSimpleName() + ". Usuario [id,name]: [" + userId + ", " + userName + "]",
				throwable);
		// logger.error(throwable);

	}

	@Override
	protected void rollbackTransaction(TransactionStatus transactionStatus) {
		super.rollbackTransaction(transactionStatus);
		getLog().error("Transaction ROLLBACK en caso de uso: " + this.getServiceName());
	}

	/**
	 * Generates a random error code.
	 */
	public String generateErrorCode() {
		return String.valueOf(Math.abs(new Random().nextInt()));
		// return new Date().toString();
	}

	/**
	 * Se fija si el usuario posee acceso al caso de uso en cuestion. En caso de
	 * no tener, lanza una excepcion de seguridad.
	 * 
	 * @param user
	 */
	private void checkAccess(LnwUser user) {

		// si el usuario existe en session
		if (user != null) {

			// si tiene acceso a este caso de uso
			if (user.hasAccess(this.getServiceName())) {
				// acceso otorgado

				// COMENTADO para evitar tanto logging
				// getLog().info( "Acceso otorgado al usuario " + user.getId() +
				// ", para ingresar al caso de uso: " + this.getServiceName() );
				return;

				// si no tiene acceso a este caso de uso
			} else {
				// get user data
				final long userId = user.getId();
				final String userName = user.getNombreCompleto();

				// log acceso denegado
				final String msg = "Acceso RPC DENEGADO a User[id,name]: [" + userId + ", " + userName + "]"
						+ " Caso: " + this.getServiceName();
				getLog().error(msg);

				// lanzar excepcion de seguridad
				throw new LnwSecurityException(msg);
			}

			// si el usuario no existe en la session
		} else {
			// get user data
			// FIXME loggear a quien se otorga: ip o algo asi

			// significa que el filtro lo dejó pasar (LnwApplicationFilter), por
			// lo tanto hay que dejarlo pasar
			// el caso más común es en el login
			getLog().info(
					"Acceso RPC otorgado por exclusión de filtro (GeneralConfig.xml), Caso: " + this.getServiceName());
		}

	}

	/**
	 * Debe retornar el id del caso de uso registrado en la clase
	 * <code>UseCaseNames</code>
	 */
	protected abstract String getServiceName();

	// No debería estar esta implementación por default
	// return this.getClass().getSimpleName();

	/**
	 * Borra todos los atributos existentes
	 */
	public final void clearAtrributes() {
		UserContext.getInstance().clearAtrributes();
	}

	/**
	 * Devuelve el atributo especificado si existe, sino devuelve null
	 */
	public final Object getFromContext(String name) {
		return UserContext.getInstance().getFromContext(name);
	}

	/**
	 * Quita el atributo seleccionado, solo si existe
	 */
	public final Object removeFromContext(String name) {
		return UserContext.getInstance().removeFromContext(name);
	}

	/**
	 * Agrega el atributo especificado. Si ya existe lo sobrescribe
	 */
	public final void addToContext(String name, Object obj) {
		UserContext.getInstance().addToContext(name, obj);
	}

	/**
	 * @return herramienta para loguear
	 */
	public final static Log getLog() {
		return LOG;
	}

	/**
	 * Obtiene el HttpServletRequest asociado al caso de uso.
	 * 
	 * @return
	 */
	public final HttpServletRequest getRequest() {
		return UserContext.getInstance().getRequest();
	}

	/**
	 * Crea y lanza una exception del tipo <code>LnwTransactionException</code> <br>
	 * Es la única excepcion que puede ponerse como chequeada en en los métodos
	 * RPC de la interfaz Client y que realiza ROLLBACK ante un error. <br>
	 * Con cualquier otra exception declarada en los métodos RPC no habrá
	 * ROLLBACK
	 * 
	 * @param msg
	 *            mensaje de la exception
	 * @param realException
	 *            exception que realmente se lanzo, para ser loggeada
	 */
	public void throwTransactionException(String msg, Throwable realException) {
		// creates exception and log error code
		LnwTransactionException exception = new LnwTransactionException(msg);

		// log
		logException(realException, generateErrorCode());

		// stores the exception in session => see GwtBaseAction
		getRequest().getSession().setAttribute(LNW_TRANSACTION_EXCEPTION_KEY, exception);
		throw exception;
	}

}
