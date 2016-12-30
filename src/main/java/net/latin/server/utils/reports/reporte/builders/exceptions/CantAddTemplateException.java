package net.latin.server.utils.reports.reporte.builders.exceptions;


/**
 * Exception que indica que no se pudo agregar un template para generar los
 * reportes.
 *
 * @author mdaloia
 */
public class CantAddTemplateException extends ReportsException {

	private static final long serialVersionUID = 3091437480063879035L;

	public CantAddTemplateException(String message) {
		super(message);
	}

	public CantAddTemplateException(String message, Throwable cause) {
		super(message, cause);
	}
}
