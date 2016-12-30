package net.latin.server.utils.reports.constructor;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			FileReportDirector

/**
 * Clase directora para la construcción de un reporte con formato de salida csv (Excel).
 * @author bfalese
 * @author ccancinos
 */
public class CsvReportDirector extends FileReportDirector {

	public CsvReportDirector() {
		super();
	}

	public CsvReportDirector(String fileName) {
		super(fileName);
	}

	public CsvReportDirector(String path, String fileName) {
		super(path, fileName);
	}

	public JRAbstractExporter getExporter() {
		return new JRCsvExporter();
	}

	protected String getFileExtension() {
		return "csv";
	}
}
