package net.latin.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exception utilizada para los pedidos hechos desde clientes GWT hacia el servidor
 *
 * @author Matias Leone
 */
public class GwtBusinessException extends RuntimeException implements IsSerializable {

	private static final long serialVersionUID = 6777210412766669033L;

	private String message;

	public GwtBusinessException() {
		this.message = "";
	}

	public GwtBusinessException(String msg, Throwable throwable) {
		super( throwable );
		this.message = msg;
	}

	public GwtBusinessException( String msg ) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
