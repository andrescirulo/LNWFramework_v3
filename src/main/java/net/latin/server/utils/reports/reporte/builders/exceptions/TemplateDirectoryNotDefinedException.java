package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que el directorio de los template de reportes
 * no ha sido seteado.
 *
 * @author mdaloia
 */
public class TemplateDirectoryNotDefinedException extends ReportsException {

	private static final long serialVersionUID = -3801179208755689229L;

	public TemplateDirectoryNotDefinedException(String message) {
		super(message);
	}

	public TemplateDirectoryNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}
}
