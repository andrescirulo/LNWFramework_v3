package net.latin.server.utils.exceptions;

/**
 * Excecion al intentar acceder a un recurso al cual no se posee
 * permiso
 * 
 * @author Fernando Diaz
 *
 */
public class LnwSecurityException extends LnwException {

	public LnwSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public LnwSecurityException(String message) {
		super(message);
	}


	
	
}
