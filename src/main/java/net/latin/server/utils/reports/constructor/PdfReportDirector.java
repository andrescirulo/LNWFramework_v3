package net.latin.server.utils.reports.constructor;

import com.lowagie.text.pdf.PdfWriter;

import net.latin.server.security.config.LnwGeneralConfig;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			FileReportDirector

/**
 * Clase directora para la construcción de un reporte con formato de salida pdf.
 * @author bfalese
 * @author ccancinos
 */
public class PdfReportDirector extends FileReportDirector {

	public PdfReportDirector() {
		super();
	}

	public PdfReportDirector(String fileName) {
		super(fileName);
	}

	public PdfReportDirector(String path, String fileName) {
		super(path, fileName);
	}

	public JRAbstractExporter getExporter() {
		this.setSecurityParameters();
		return new JRPdfExporter();
	}

	protected String getFileExtension() {
		return "pdf";
	}

	protected void setSecurityParameters() {
		this.getParameters().put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		this.getParameters().put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
		this.getParameters().put(JRPdfExporterParameter.OWNER_PASSWORD,
					LnwGeneralConfig.getInstance().getReportsPassword() );
		this.getParameters().put(JRPdfExporterParameter.PERMISSIONS,
		        	new Integer(PdfWriter.AllowCopy | PdfWriter.AllowPrinting));
	}
}
