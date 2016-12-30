package net.latin.server.utils.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.fileTypes.Html;
import net.latin.server.utils.fileTypes.Pdf;
import net.latin.server.utils.reports.constructor.FileReportDirector;
import net.latin.server.utils.reports.constructor.HtmlReportDirector;
import net.latin.server.utils.reports.constructor.HtmlStreamReportDirector;
import net.latin.server.utils.reports.constructor.PdfReportDirector;
import net.latin.server.utils.reports.constructor.ReportBuilder;
import net.latin.server.utils.reports.constructor.StreamReportDirector;
import net.latin.server.utils.reports.reporte.BeansReportDataContextBuilder;
import net.latin.server.utils.reports.reporte.ReportBroker;
import net.latin.server.utils.reports.visualizer.ReportViewerServlet;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

/**
 * Utilidad para la creacion de archivos PDF y HTML Soporta CustomBean
 * 
 * Adaptada de SNO
 * 
 * @author Matias Leone
 */
public class LnwPdfBuilder {

	private final static String TEMPLATE_EXT = ".jrxml";
	public final static String IMAGES_PATH = "images/";

	private String directorioReportes;
	private String nombreArchivoTemplate;
	private String directorioSalida;
	private int cantCopias = 1;
	private List<Map<String, ?>> customBeans;

	private final ReportBroker reportBroker;
	private final BeansReportDataContextBuilder dataContextBuilder;
	private ReportBuilder builder;

	/**
	 * El templateName debe coincidir exactamente con el nombre del jrxml, debe
	 * ser sensitivo a mayusculas y minusculas
	 */
	public LnwPdfBuilder(String templateName) {
		customBeans = new ArrayList<Map<String, ?>>();
		reportBroker = ReportBroker.getInstance();
		dataContextBuilder = new BeansReportDataContextBuilder();
		builder = null; // Hasta que no tenga un ReportDirector es null.

		directorioReportes = LnwGeneralConfig.getInstance().getReportsClasspath();

		reportBroker.setPath(directorioReportes);
		reportBroker.setExtension(TEMPLATE_EXT);

		directorioSalida = LnwGeneralConfig.getInstance().getReportsTestingOutputFolder();

		nombreArchivoTemplate = templateName;
	}

	public void setDirectorioSalida(String path) {
		directorioSalida = path;
	}

	public void addParameter(Object name, Object value) {
		dataContextBuilder.withParameter(name, value);
	}

	public void addParameters(Map parametersMap) {
		dataContextBuilder.withParameters(parametersMap);
	}

	public void addBean(Object bean) {
		dataContextBuilder.withBean(bean);
	}

	public void addBeans(Collection beansCollection) {
		dataContextBuilder.withAllBeans(beansCollection);
	}

	/**
	 * Add a CustomBean to the Report. You cannot mix CustomBean with normal
	 * beans
	 */
	public void addCustomBean(CustomBean bean) {
		customBeans.add(bean.getStringMap());

	}

	public void addSubreport(String templateNameSubreport) {
		this.reportBroker.addTemplate("/", templateNameSubreport);

		try {
			dataContextBuilder.withSubreport(templateNameSubreport);
		} catch (Exception e) {
			throw new LnwException("No se pudo agregar el SubReporte: " + templateNameSubreport, e);
		}
	}

	public void setCantCopias(int cantCopias) {
		this.cantCopias = cantCopias;
	}

	/**
	 * Genera un PDF con el nombre especificado y lo guarda en la carpeta
	 * especificada en el archivo GeneralConfig.xml (ReportsTestingOutputFolder)
	 * 
	 * @param fileName
	 *            nombre del PDF, sin extension
	 */
	public void buildPdf(String fileName) {

		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);

			final FileReportDirector reportDirector = new PdfReportDirector(directorioSalida, fileName);

			builder = new ReportBuilder(reportDirector);

			// check if the user wants a data source for CustomBeans
			if (customBeans.size() > 0) {
				dataContextBuilder.withDataSource(new JRMapCollectionDataSource(customBeans));
			}

			builder.setDataContext(dataContextBuilder.build());
			builder.setTemplate(nombreArchivoTemplate);
			builder.setCopiesQuantity(cantCopias);
			builder.build();
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte: " + fileName, e);
		}
	}

	/**
	 * Creates the pdf and return it
	 */
	public Pdf buildPdfAndGet(String fileName) {
		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			final StreamReportDirector reportDirector = new StreamReportDirector(output);

			builder = new ReportBuilder(reportDirector);

			// check if the user wants a data source for CustomBeans
			if (customBeans.size() > 0) {
				dataContextBuilder.withDataSource(new JRMapCollectionDataSource(customBeans));
			}

			builder.setDataContext(dataContextBuilder.build());
			builder.setTemplate(nombreArchivoTemplate);
			builder.setCopiesQuantity(cantCopias);
			builder.build();

			return new Pdf(fileName + ".pdf", output.toByteArray());
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el archivo PDF", e);
		}
	}

	/**
	 * Creates the pdf file and stores it into the user session
	 */
	public void buildWebPdf(String fileName) {
		try {
			ReportViewerServlet.setFileToShow(buildPdfAndGet(fileName));
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}

	/**
	 * Creates the pdf file and returns it
	 */
	public Pdf getWebPdf(String fileName) {
		try {
			return buildPdfAndGet(fileName);
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el archivo PDF", e);
		}
	}

	/**
	 * Creates the HTML file and stores it int the user session
	 */
	public Html buildWebHTML(String fileName, HttpServletRequest request) {
		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);
			final StringBuffer output = new StringBuffer();
			final HtmlStreamReportDirector reportDirector = new HtmlStreamReportDirector(output);

			builder = new ReportBuilder(reportDirector);

			// check if the user wants a data source for CustomBeans
			if (customBeans.size() > 0) {
				dataContextBuilder.withDataSource(new JRMapCollectionDataSource(customBeans));
			}

			builder.setDataContext(dataContextBuilder.build());
			builder.setTemplate(nombreArchivoTemplate);
			builder.setCopiesQuantity(cantCopias);
			builder.build();

			// Cargar en session el JasperPrint para que lo pueda usar el
			// servlet de imagenes de Jasper (ImageServlet)
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
					builder.getJasperPrint());

			// changing the charset...utf-8 produce problems with the accents
			String html = output.toString();
			html = html.replaceAll("charset=UTF-8", "charset=iso-8859-1");
			// store the pdf in session
			Html report = new Html(fileName + ".html", html);
			ReportViewerServlet.setFileToShow(report);
			return report;

		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}

	/**
	 * Genera un HTML con el nombre especificado y lo guarda en la carpeta
	 * especificada en el archivo GeneralConfig.xml (ReportsTestingOutputFolder)
	 * 
	 * @param fileName
	 *            nombre del HTML, sin extension
	 */
	public void buildHTML(String fileName) {

		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);

			final FileReportDirector reportDirector = new HtmlReportDirector(directorioSalida, fileName);

			builder = new ReportBuilder(reportDirector);

			// check if the user wants a data source for CustomBeans
			if (customBeans.size() > 0) {
				dataContextBuilder.withDataSource(new JRMapCollectionDataSource(customBeans));
			}

			builder.setDataContext(dataContextBuilder.build());
			builder.setTemplate(nombreArchivoTemplate);
			builder.setCopiesQuantity(cantCopias);
			builder.build();
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte: " + fileName, e);
		}
	}

}
