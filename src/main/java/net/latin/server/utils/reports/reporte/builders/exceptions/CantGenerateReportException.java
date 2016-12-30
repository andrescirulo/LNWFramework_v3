package net.latin.server.utils.reports.reporte.builders.exceptions;

/**
 * Exception que indica que no se pudo generar el reporte por algun motivo
 * interno al framework de generacion de reportes.
 * Generalmente se encontrara una explicacion en la excepcion wrappeada.
 *
 * @author mdaloia
 */
public class CantGenerateReportException extends ReportsException {

	private static final long serialVersionUID = -9053344519617886868L;

	public CantGenerateReportException(String message) {
		super(message);
	}

	public CantGenerateReportException(String message, Throwable cause) {
		super(message, cause);
	}
}
