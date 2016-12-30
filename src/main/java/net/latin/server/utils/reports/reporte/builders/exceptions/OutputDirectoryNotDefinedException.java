package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que el directorio de salida de los reportes no ha sido
 * seteado.
 *
 * @author mdaloia
 */
public class OutputDirectoryNotDefinedException extends ReportsException {

	private static final long serialVersionUID = -3693000887114832519L;

	public OutputDirectoryNotDefinedException(String message) {
		super(message);
	}

	public OutputDirectoryNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}
}
