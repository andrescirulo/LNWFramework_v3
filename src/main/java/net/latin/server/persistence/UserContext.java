package net.latin.server.persistence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.latin.server.GwtContext;
import net.latin.server.persistence.user.LnwUser;

import org.springframework.transaction.TransactionStatus;



public class UserContext extends ThreadLocal<UserContext>{

	private static UserContext instance;

	private LnwUser user;

	/**
	 * Variables necesarias para gwt
	 */
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	private String requestPayload;
	private TransactionStatus transactionStatus;

	/**
	 * Map for user context
	 */
	private GwtContext context;



	private UserContext() {
	}

	public final static synchronized UserContext getInstance(){
		if(instance == null){
			instance = new UserContext();
		}
		return instance.get();
	}

	@Override
	protected final UserContext initialValue() {
		return new UserContext();
	}


	/**
	 * Clears the gwt variables
	 */
	public final void clear() {
		response = null;
		request = null;
		requestPayload = null;
		transactionStatus = null;
		context = null;
		user = null;
	}

	public final LnwUser getUser() {
		return user;
	}

	public final void setUser(LnwUser user) {
		this.user = user;
	}

	public final HttpServletResponse getResponse() {
		return response;
	}

	public final void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public final HttpServletRequest getRequest() {
		return request;
	}

	public final void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public final String getRequestPayload() {
		return requestPayload;
	}

	public final void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}


	/**
	 * Metodo interno. No utilizar.
	 * @param context
	 */
	public final void setGwtContext( GwtContext context ) {
		this.context = context;
	}

	/**
	 * Crea un nuevo GwtContext
	 */
	public final GwtContext resetContext() {
		context = new GwtContext();
		return context;
	}

	/**
	 * Borra todos los atributos existentes
	 */
	public final void clearAtrributes() {
		context.clearAtrributes();
	}

	/**
	 * Devuelve el atributo especificado si existe, sino devuelve null
	 */
	public final Object getFromContext(String name) {
		return context.getAttribute(name);
	}

	/**
	 * Quita el atributo seleccionado, solo si existe
	 */
	public final Object removeFromContext(String name) {
		return context.removeAttribute(name);
	}

	/**
	 * Agrega el atributo especificado. Si ya existe lo sobrescribe
	 */
	public final void addToContext(String name, Object obj) {
		context.setAttribute(name, obj);
	}

	/**
	 * Setea el transactionStatus para poder hacer rollback en otro momento
	 * @param transactionStatus
	 */
	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	/**
	 * @return the transactionStatus. Sirve para hacer rollback y manejo general de
	 * la transaccion
	 */
	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	/**
	 * Efectua rollback sobre la transacción actual, sin la necesidad de lanzar
	 * una excecpcion.
	 */
	public void rollback() {
		transactionStatus.setRollbackOnly();
	}




}
