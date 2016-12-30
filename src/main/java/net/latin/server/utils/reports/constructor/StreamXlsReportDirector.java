package net.latin.server.utils.reports.constructor;

import java.io.OutputStream;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			ReportDirector

/**
 * Director para obtener la salida de un reporte en un OutputStream
 * @author bfalese
 */
public class StreamXlsReportDirector extends ReportDirector {

	public StreamXlsReportDirector(OutputStream outputstream) {
		this.setOutputStream(outputstream);
	}

	public void setOutputStream(OutputStream outputstream) {
		this.getParameters().put(JRExporterParameter.OUTPUT_STREAM, outputstream);
	}

	public JRAbstractExporter getExporter() {
		return new JRXlsxExporter();
	}
}
