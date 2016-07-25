package net.latin.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Excepcion utilizada por las validaciones hechas en GWT.
 *
 * @author Martin D'Aloia
 */
public class GwtValidationException extends RuntimeException implements IsSerializable {

	private static final long serialVersionUID = -9098470371928701497L;

	private String message;

	public GwtValidationException() {
		this.message = "";
	}

	public GwtValidationException(String msg, Throwable throwable) {
		super( throwable );
		this.message = msg;
	}

	public GwtValidationException( String msg ) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
