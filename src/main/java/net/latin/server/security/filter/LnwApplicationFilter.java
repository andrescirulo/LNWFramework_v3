package net.latin.server.security.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.persistence.UserContext;
import net.latin.server.persistence.user.LnwUser;
import net.latin.server.security.config.CommonAccess;
import net.latin.server.security.config.LnwFilterRuleData;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.security.config.LnwMenuGroup;
import net.latin.server.security.config.LnwMenuItem;

/**
 * <p>
 * Filtro principal del Framework.
 * La mayoría de los pedidos HTTP pasan por acá.
 * Todos los pedidos RPC de las interfaces Client pasan por acá
 *
 * <p>
 * Crea el usuario que se agrega a la session
 * Maneja permisos de usuario.
 *
 * @author Fernando Diaz
 *
 */
public class LnwApplicationFilter implements Filter{

	private static final String DEFAULT_LOGOUT_URL = "LogoutPage.do";
	public static final String APPLICATION_USER_KEY = "__applicationUser__";
	private final static Log LOG = LogFactory.getLog(LnwApplicationFilter.class);


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		UserContext userContext = UserContext.getInstance();
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);


		/**
		 *  Buscamos si esta el usuario en la session de tomcat,
		 *  en caso de estarlo lo metemos en el UserContext
		 */
		Object user = httpRequest.getSession(true).getAttribute(APPLICATION_USER_KEY);

		//si existe, lo metemos en el ThreadLocal para poder usarlo f�cilmente despu�s
		if (user != null) {
			userContext.setUser((LnwUser) user);

		//si no existe, es porque nunca se loggeo al sistema
		} else{
			//si no esta excluida ir al session expired page
			if( !esAccesoPermitido(httpRequest) ){
				httpResponse.sendRedirect( getLogoutUrl() );
				return;
			}
		}
		//AGREGADO PARA QUE CUANDO PONE CUALQUIER URL, TE MANDE A LA PAGINA DE SESION EXPIRADA EN VEZ DE MOSTRARTE
		//LA EXCEPCION DE TOMCAT
		try{
			chain.doFilter(request, response);
		} catch (Exception e) {
			LOG.error("Error proveniente del chain.doFilter()", e);

			// Si se hace un sendRedirect() cuando ya se ha enviado parte
			// o toda la respuesta genera un IllegalStateException
			if(!httpResponse.isCommitted())
				httpResponse.sendRedirect( getLogoutUrl() );
			else {
				LnwUser currentUser = userContext.getUser();
				String msg = "No se pudo hacer el redirect a " + getLogoutUrl()
							+ " al procesar el request URL: " + getFullURL(httpRequest)
							+ " solicitada por el Usuario: " + (currentUser != null ? currentUser.getUserName() : "<not_logged_in>")
							+ " con IP: " + httpRequest.getRemoteAddr()
							+ " debido a que el response ya ha sido enviado parcial o totalmente.";
				LOG.warn(msg);
			}

			return;
		}
	}

	private String getFullURL(HttpServletRequest req) {
		StringBuffer url = req.getRequestURL();
		String queryString = req.getQueryString();
		if (queryString != null) {
		    url.append('?');
		    url.append(queryString);
		}
		return url.toString();
	}

	/**
	 * <p>
	 * Devuelve la direcci�n de LOGOUT a la cual se redirige si se quiere
	 * acceder a una URL no exclu�da, sin haberse loggeado previamente.
	 * Redefinir para cambiar la URL.
	 */
	protected String getLogoutUrl() {
		return DEFAULT_LOGOUT_URL;
	}

	/**
	 * Devuelve true si la URL a la que se intenta acceder:
	 * 1) Pasa todas las reglas de fitro
	 * 2) Es una URL valida, que esta dentro de la lista de URLs excluidas del GeneralConfig.xml
	 */
	protected boolean esAccesoPermitido(HttpServletRequest httpRequest) {
		final String url = httpRequest.getRequestURL().toString();
		final List<LnwFilterRule> filtros = LnwGeneralConfig.getInstance().getFiltros();

		for (LnwFilterRule filtro : filtros) {
			if(!filtro.validar(httpRequest)){
				LOG.info("La URL " + url + " no paso el filtro de " + filtro.getClass().getSimpleName());
				return false;
			}
		}

		//ejecutar filtros de EndWidth
		final List<String> permitidas = LnwGeneralConfig.getInstance().getFilterExcludedUrls();
		for (String  permitida: permitidas) {
			if(url.endsWith(permitida)) {
				return true;
			}
		}

		LOG.info("La URL " + url + " no paso el filtro de endsWith");
		return false;
	}

	/**
	 * <p>
	 * Utilidad para agregar al usuario de la aplicaci�n
	 * en el contexto de session y en el ThreadLocal
	 *
	 * @param user usuario de la aplicacion. Configurar clases concretas en el GeneralConfig.xml
	 */
	public static void registerUser( LnwUser user ) {
		UserContext userContext = UserContext.getInstance();
		userContext.setUser(user);
		userContext.getRequest().getSession().setAttribute( LnwApplicationFilter.APPLICATION_USER_KEY, user);
	}

	/**
	 * <p>
	 * Utilidad para otorgar a un usuario acceso a todos los casos
	 * de uso existentes en la aplicaci�n, declarados en GeneralConfig.xml
	 *
	 * @param lnwUser
	 */
	public static void addAllAccessToUser(LnwUser user) {
		//Recorremos todo el GeneralConfig.xml y le damos acceso a todos los casos de uso
		List<LnwMenuGroup> groups = LnwGeneralConfig.getInstance().getGroups();
		for (LnwMenuGroup lnwMenuGroup : groups) {
			List<LnwMenuItem> menuItems = lnwMenuGroup.getMenuItems();
			for (LnwMenuItem lnwMenuItem : menuItems) {
				user.addAccess( lnwMenuItem.getId() );
			}
		}

		//tambien le damos acceso a los casos de uso gen�ricos que no est�n en el men�
		addCommonAccessToUser(user);
	}

	/**
	 * Utilidad para otorgar a un usuario acceso a todos los casos
	 * de uso comunes, declarods en GeneralConfig.xml, en la seccion <CommonAccess>
	 * @param user
	 */
	public static void addCommonAccessToUser(LnwUser user) {
		List<CommonAccess> commonAccess = LnwGeneralConfig.getInstance().getCommonAccess();
		for (CommonAccess access : commonAccess) {
			user.addAccess( access.getId() );
		}
	}




	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			List<LnwFilterRuleData> filterRules = LnwGeneralConfig.getInstance().getFilterRules();
			for (LnwFilterRuleData lnwFilterRule : filterRules) {
				LnwFilterRule filtroAplicacion = (LnwFilterRule) Class.forName(lnwFilterRule.getClassName()).newInstance();
				filtroAplicacion.init(lnwFilterRule.getParams());
				LnwGeneralConfig.getInstance().addNewFilter(filtroAplicacion);
			}
		} catch (Exception e) {
			throw new RuntimeException("No se pudieron instanciar los filtros",e);
		}

	}

	public void destroy() {
	}


}
