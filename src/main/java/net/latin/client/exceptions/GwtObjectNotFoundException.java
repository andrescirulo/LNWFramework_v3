package net.latin.client.exceptions;

import net.latin.client.rpc.GwtBusinessException;

/**
 * Exception que representa que el Objeto buscado no fue encontrado.
 *
 */
public class GwtObjectNotFoundException extends GwtBusinessException {

	private static final long serialVersionUID = -6568053585729002131L;

	public GwtObjectNotFoundException() {
		super();
	}

	public GwtObjectNotFoundException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public GwtObjectNotFoundException( String msg ) {
		super(msg);
	}
}
