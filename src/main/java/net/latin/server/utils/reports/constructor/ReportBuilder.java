package net.latin.server.utils.reports.constructor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import net.latin.server.utils.collections.CollectionFactory;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.reports.reporte.ReportBroker;
import net.latin.server.utils.reports.reporte.ReportDataContext;
import net.latin.server.utils.reports.reporte.builders.exceptions.InvalidNumberOfCopiesException;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			ReportDirector

/**
 * Construye y obtiene el formato de salida especificado para un reporte.
 * @author bfalese
 * @author ccancinos
 */
public class ReportBuilder {

	private JRAbstractExporter exporter;
	private ReportDirector reportDirector;
	private String templateLocation;
	private JasperReport jasperReport;
	private JasperPrint jasperPrint;
	private ReportDataContext context;
	private boolean isURL;
	private int copiesQuantity = 1;

    /**
     * Inicializa el objeto constructor
     * @param reportDirector Director del reporte.
     */
	public ReportBuilder(ReportDirector reportdirector) {
		reportDirector = reportdirector;
	}


	public void setCopiesQuantity(int copiesQuantity) {
		if(copiesQuantity < 1) {
			throw new InvalidNumberOfCopiesException("La cantidad de copias a generar debe ser mayor a 0. Cantidad de copias ingresadas: " + copiesQuantity);
		}

		this.copiesQuantity = copiesQuantity;
	}

    /**
     * Construye el reporte con el formato de salida seleccionado por el director.
     */
	public void build() {
		//Si el jasperReport no ha sido cargado se lee y compila.
		if (jasperReport == null)
			compileReport();

		if (jasperPrint == null)
			fillReport();

		exporter = reportDirector.prepareToExport();

		if(copiesQuantity == 1) {
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		} else {
			List<JasperPrint> jasperPrintList = CollectionFactory.createList();

			for(int i = 0; i < copiesQuantity; i++) {
				jasperPrintList.add(jasperPrint);
			}

			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		}


		try {
			exporter.exportReport();
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se ha podido exportar el reporte", jrexception);
		}
	}

    /**
     * Construye el reporte para mostrarlo con un servlet
     */
	public byte[] buildWebOutput() {
		//Si el jasperReport no ha sido cargado se lee y compila.
		if (jasperReport == null)
			compileReport();

		if (jasperPrint == null)
			fillReport();
		try {
			return JasperExportManager.exportReportToPdf(jasperPrint);
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se ha podido exportar el reporte para mandar al servlet", jrexception);
		}
	}

    /**
     * Muestra una vista previa por pantalla del reporte a imprimirse.
     */
	public void preview() {
		//Si el jasperReport no ha sido cargado se lee y compila.
		if (jasperReport == null)
			compileReport();

		if (jasperPrint == null)
			fillReport();
//		try {
			if (jasperPrint != null)
				JasperViewer.viewReport(jasperPrint);
//		}
//		catch (JRException jrexception) {
//			throw LnwException.wrap("No se ha podido abrir la vista previa del reporte", jrexception);
//		}
	}

    /**
     * Carga al reporte el template indicado.
     * @param templateName Identificador del reporte.
     * @return
     */
	public ReportBuilder setTemplate(String templateName) {
		jasperReport = ReportBroker.getInstance().getTemplate(templateName);
		return this;
	}

    /**
     * Lee y compila un subreporte indicado su ubicacion a través de una URL.
     * @param templateLocation Ubicación del reporte.
     */
	public ReportBuilder setTemplate(URL url) {
		if (jasperReport != null)
			jasperReport = null;

		if (jasperPrint != null)
			jasperPrint = null;

		try {
			templateLocation = (new File(new URI(url.toString().replaceAll(" ", "%20")))).toString();
		}
		catch (URISyntaxException urisyntaxexception) {
			urisyntaxexception.printStackTrace();
		}
		isURL = false;
		return this;
	}

    /**
     * Carga los datos del reporte.
     * @param context Objeto con la imformación a usar en el reporte.
     * @return
     */
	public ReportBuilder setDataContext(ReportDataContext reportdatacontext) {
		if (jasperPrint != null)
			jasperPrint = null;

		context = reportdatacontext;
		return this;
	}

    /**
     * Permite cambiar el director una vez creado el ReportBuilder.
     * @param reportDirector
     * @return
     */
	public ReportBuilder setDirector(ReportDirector reportdirector) {
		reportDirector = reportdirector;
		return this;
	}

	private void compileReport() {
		JasperDesign jasperdesign = null;
		URL url = null;
		try {
			//Cargo la plantilla y la compilo
			if (isURL) {
				url = new URL(templateLocation);
				jasperdesign = JRXmlLoader.load(url.openConnection().getInputStream());
			} else {
				jasperdesign = JRXmlLoader.load(templateLocation);
			}

			jasperReport = JasperCompileManager.compileReport(jasperdesign);
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se ha podido compilar el reporte", jrexception)
				.addInfo("templateName", templateLocation);
		}
		catch (MalformedURLException malformedurlexception) {
			throw LnwException.wrap("Url mal formada", malformedurlexception)
				.addInfo("url", url);
		}
		catch (IOException ioexception) {
			throw LnwException.wrap("Error de entrada/salida", ioexception);
		}
	}

    /**
     * Carga los datos en el reporte.
     */
	private void fillReport() {
		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, context.getParameters(), context.getDataSource());
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se han podido introducir los valores al reporte", jrexception)
				.addInfo("reporte", jasperReport)
				.addInfo("parametros", context.getParameters())
				.addInfo("datasource", context.getDataSource());
		}
	}


	/**
	 * @return the jasperPrint
	 */
	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}
}
