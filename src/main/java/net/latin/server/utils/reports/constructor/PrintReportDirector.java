package net.latin.server.utils.reports.constructor;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.PrinterName;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			ReportDirector

/**
 * Clase directora para la construcción de un reporte con salida a impresora.
 * @author bfalese
 * @author ccancinos
 */
public class PrintReportDirector extends ReportDirector {
    private PrintRequestAttributeSet requestAttributes;
    private PrintServiceAttributeSet serviceAttributes;
	private int copies;
    
	/**
	 * Construye el director de impresión.
	 * @param printName Nombre de la impresora a usarse. Esta debe ser local o tener acceso como si fuese local
	 * a la maquina.
	 */
    public PrintReportDirector(String printName) {
        this();
        this.setPrinterName(printName);
    }
    
    public PrintReportDirector() {
        super();
		//por default se realiza una copia
        this.copies = 1;
        this.requestAttributes = new HashPrintRequestAttributeSet();
        this.serviceAttributes = new HashPrintServiceAttributeSet();
        //Por default no se mostrara ningun dialogo
        this.getParameters().put(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        this.getParameters().put(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
    }
    
    public PrintReportDirector setCopies(int copyCount) {
        this.copies = copyCount;
        return this;
    }
    
    public PrintReportDirector setPageSize(String pageSizeName) {
        //Las impresoras no reconocen este atributo, si se setea no encuentra ninguna impresora.
        //this.requestAttributes.add(new MediaSizeName());
        return this;
    }
    
    public PrintReportDirector setTray() {
    	this.requestAttributes.add(MediaTray.MAIN);
    	return this;
    }
    public PrintReportDirector setPrinterName(String printerName) {
        this.serviceAttributes.add(new PrinterName(printerName, null));
        return this;
    }
    
    /**
     * Muestra un dialogo con las caracteristicas actuales de la impresora
     * @param value
     * @return
     */
    public PrintReportDirector withPageDialog(Boolean value) {
        this.getParameters().put(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, value);
        return this;
    }
    
    /**
     * Muestra un dialogo con las opciones disponibles con respecto a bandeja para la impresora seleccionada.
     * @param value
     * @return
     */
    public PrintReportDirector withPrintDialog(Boolean value) {
        this.getParameters().put(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, value);
        return this;
    }
    
    public JRAbstractExporter getExporter() {
        return new JRPrintServiceExporter();
    }
    
    protected void initExporter(JRAbstractExporter jrabstractexporter) {
        this.requestAttributes.add(new Copies(this.copies));
        jrabstractexporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, this.requestAttributes);	
        jrabstractexporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, this.serviceAttributes);
    }
}
