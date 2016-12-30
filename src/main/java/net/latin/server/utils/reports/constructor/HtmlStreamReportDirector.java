package net.latin.server.utils.reports.constructor;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;


/**
 * Director para obtener la salida de un reporte en un StringBuffer en HTML
 *
 * @author Matias Leone
 */
public class HtmlStreamReportDirector extends ReportDirector {

	public HtmlStreamReportDirector(StringBuffer buffer) {
		this.getParameters().put(JRExporterParameter.OUTPUT_STRING_BUFFER, buffer);
		this.getParameters().put(JRHtmlExporterParameter.IMAGES_URI, "image?"+"rnd=" + Math.random()+"&image=");
	}

	public JRAbstractExporter getExporter() {
		return new JRHtmlExporter();
	}
}
