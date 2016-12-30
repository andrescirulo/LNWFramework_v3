package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que el nombre del archivo que contiene el template para
 * generar el reporte no ha sido seteado.
 *
 * @author mdaloia
 */
public class TemplateFilenameNotDefinedException extends ReportsException {

	private static final long serialVersionUID = -4071201756114964276L;

	public TemplateFilenameNotDefinedException(String message) {
		super(message);
	}

	public TemplateFilenameNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}
}
