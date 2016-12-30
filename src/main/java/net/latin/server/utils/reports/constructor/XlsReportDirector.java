package net.latin.server.utils.reports.constructor;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			FileReportDirector

/**
 * Clase directora para la construcción de un reporte con formato de salida xls (Excel).
 * @author bfalese
 * @author ccancinos
 */
public class XlsReportDirector extends FileReportDirector {

	public XlsReportDirector() {
		super();
	}

	public XlsReportDirector(String fileName) {
		super(fileName);
	}

	public XlsReportDirector(String path, String fileName) {
		super(path, fileName);
	}
	
	public JRAbstractExporter getExporter() {
		return new JRXlsxExporter();
	}

	protected void initExporter(JRAbstractExporter jrabstractexporter) {
		jrabstractexporter.setParameters(this.getParameters());
		jrabstractexporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	}

	protected String getFileExtension() {
		return "xlsx";
	}
}
