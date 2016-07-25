package net.latin.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Excepcion lanzada cuando ha ocurrido en problema relacionado con la
 * persistencia
 *
 * @author Matias Leone
 */
public class GwtPersistenceException extends RuntimeException implements IsSerializable {

	private static final long serialVersionUID = -489464520931954905L;

	private String message;

	public GwtPersistenceException() {
		this.message = "";
	}

	public GwtPersistenceException(String msg, Throwable throwable) {
		super( throwable );
		this.message = msg;
	}

	public GwtPersistenceException( String msg ) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
