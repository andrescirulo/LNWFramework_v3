package net.latin.server.utils.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.fileUpload.FileDownloaderServlet;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.fileTypes.Xls;
import net.latin.server.utils.reports.constructor.FileReportDirector;
import net.latin.server.utils.reports.constructor.ReportBuilder;
import net.latin.server.utils.reports.constructor.StreamXlsReportDirector;
import net.latin.server.utils.reports.constructor.XlsReportDirector;
import net.latin.server.utils.reports.reporte.BeansReportDataContextBuilder;
import net.latin.server.utils.reports.reporte.ReportBroker;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * Utilidad para la creacion de archivos XLSx Soporta CustomBean
 * 
 * 
 */
public class LnwXlsxBuilder {

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
	public LnwXlsxBuilder(String templateName) {
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
	 * Genera un Xlsx con el nombre especificado y lo guarda en la carpeta
	 * especificada en el archivo GeneralConfig.xml (ReportsTestingOutputFolder)
	 * 
	 * @param fileName
	 *            nombre del Xlsx, sin extension
	 */
	public void buildXlsx(String fileName) {

		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);

			final FileReportDirector reportDirector = new XlsReportDirector(directorioSalida, fileName);

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
	public Xls buildXlsxAndGet(String fileName) {
		try {
			this.reportBroker.addTemplate("/", nombreArchivoTemplate);
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			final StreamXlsReportDirector reportDirector = new StreamXlsReportDirector(output);

			builder = new ReportBuilder(reportDirector);

			// check if the user wants a data source for CustomBeans
			if (customBeans.size() > 0) {
				dataContextBuilder.withDataSource(new JRMapCollectionDataSource(customBeans));
			}

			builder.setDataContext(dataContextBuilder.build());
			builder.setTemplate(nombreArchivoTemplate);
			builder.setCopiesQuantity(cantCopias);
			builder.build();

			return new Xls(fileName + ".xlsx", output.toByteArray());
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el archivo Xlsx", e);
		}
	}

	/**
	 * Creates the xlsx file and stores it into the user session
	 */
	public void buildWebXlsx(String fileName) {
		try {
			FileDownloaderServlet.setFileToShow(buildXlsxAndGet(fileName));
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}

	/**
	 * Creates the pdf file and returns it
	 */
	public Xls getWebXlsx(String fileName) {
		try {
			return buildXlsxAndGet(fileName);
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el archivo XLSx", e);
		}
	}

}
