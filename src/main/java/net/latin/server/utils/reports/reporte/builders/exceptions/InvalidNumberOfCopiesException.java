package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que se quizo setear la cantidad de copias en un valor
 * menor a 1.
 *
 * @author mdaloia
 */
public class InvalidNumberOfCopiesException extends ReportsException {

	private static final long serialVersionUID = -640221060233819294L;

	public InvalidNumberOfCopiesException(String message) {
		super(message);
	}

	public InvalidNumberOfCopiesException(String message, Throwable cause) {
		super(message, cause);
	}
}
