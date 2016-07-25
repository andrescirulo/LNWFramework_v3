package net.latin.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Excepcion lanzada cuando se intenta realizar un pedido por ajax a un Servicio
 * del cual no se tiene permiso
 *
 * @author Matias Leone
 */
public class GwtSecurityException extends RuntimeException implements IsSerializable {

	private static final long serialVersionUID = 782002286459947193L;

	private String message;

	public GwtSecurityException() {
		this.message = "";
	}

	public GwtSecurityException(String msg, Throwable throwable) {
		super( throwable );
		this.message = msg;
	}

	public GwtSecurityException( String msg ) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
