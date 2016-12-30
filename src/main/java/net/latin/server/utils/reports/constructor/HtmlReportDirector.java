package net.latin.server.utils.reports.constructor;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			FileReportDirector

/**
 * Clase directora para la construcción de un reporte con formato de salida html.
 * @author bfalese
 * @author ccancinos
 */
public class HtmlReportDirector extends FileReportDirector {

	public HtmlReportDirector() {
		super();
	}

	public HtmlReportDirector(String fileName) {
		super(fileName);
	}

	public HtmlReportDirector(String path, String fileName) {
		super(path, fileName);
	}

	public JRAbstractExporter getExporter() {
		return new JRHtmlExporter();
	}

	protected String getFileExtension() {
		return "html";
	}
}
