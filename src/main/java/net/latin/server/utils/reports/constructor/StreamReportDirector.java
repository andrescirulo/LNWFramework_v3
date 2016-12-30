package net.latin.server.utils.reports.constructor;

import java.io.OutputStream;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			ReportDirector

/**
 * Director para obtener la salida de un reporte en un OutputStream
 * @author bfalese
 */
public class StreamReportDirector extends ReportDirector {

	public StreamReportDirector(OutputStream outputstream) {
		this.setOutputStream(outputstream);
	}

	public void setOutputStream(OutputStream outputstream) {
		this.getParameters().put(JRExporterParameter.OUTPUT_STREAM, outputstream);
	}

	public JRAbstractExporter getExporter() {
		return new JRPdfExporter();
	}
}
