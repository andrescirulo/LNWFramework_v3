package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que la extension de los template de reportes
 * no ha sido seteado.
 *
 * @author mdaloia
 */
public class TemplateExtensionNotDefinedException extends ReportsException {

	private static final long serialVersionUID = 5573356523730875879L;

	public TemplateExtensionNotDefinedException(String message) {
		super(message);
	}

	public TemplateExtensionNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}
}
