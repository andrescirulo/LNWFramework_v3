package net.latin.server.utils.reports.reporte.builders.exceptions;


/**
 * Exception que indica que no se pudo agregar un SubReporte al reporte actual.
 *
 * @author mdaloia
 */
public class CantAddSubReportException extends ReportsException {

	private static final long serialVersionUID = 1995297763465205920L;

	public CantAddSubReportException(String message) {
		super(message);
	}

	public CantAddSubReportException(String message, Throwable cause) {
		super(message, cause);
	}
}
