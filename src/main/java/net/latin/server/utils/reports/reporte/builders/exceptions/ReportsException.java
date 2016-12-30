package net.latin.server.utils.reports.reporte.builders.exceptions;

import net.latin.server.utils.exceptions.LnwException;

/**
 * Root Exception de todas las excepciones tiradas por la clase
 * PdfBuilder.
 *
 * @author mdaloia
 */
public class ReportsException extends LnwException {

	private static final long serialVersionUID = 6701098683519738694L;

	public ReportsException(String message) {
		super(message);
	}

	public ReportsException(String message, Throwable cause) {
		super(message, cause);
	}
}
