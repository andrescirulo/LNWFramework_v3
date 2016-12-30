package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que el nombre del archivo de salida del reporte no ha
 * sido seteado.
 *
 * @author mdaloia
 */
public class OutputFilenameNotDefinedException extends ReportsException {

	private static final long serialVersionUID = 4267008862471610308L;

	public OutputFilenameNotDefinedException(String message) {
		super(message);
	}

	public OutputFilenameNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}
}
