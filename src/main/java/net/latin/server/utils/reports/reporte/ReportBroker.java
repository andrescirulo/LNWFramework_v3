package net.latin.server.utils.reports.reporte;

import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.utils.collections.CollectionFactory;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.helpers.FileUtils;
import net.latin.server.utils.helpers.SerializerUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public class ReportBroker {

	private static ReportBroker instance;
	private Map reports;
	private String path;
	private String extension;
	private String fileName;
	private boolean modificado;
	private static Log LOG=LogFactory.getLog(ReportBroker.class);

	private ReportBroker() {
		modificado = true;
		reports = CollectionFactory.createMap();
	}

	private ReportBroker(String fileName) {
		this.modificado = true;
		this.fileName = fileName;
		this.reports = initReportsMap(fileName);
	}

	public static synchronized ReportBroker getInstance() {
		if (instance == null)
			instance = new ReportBroker();
		return instance;
	}

	public static synchronized ReportBroker getInstance(String fileName) {
		if (instance == null)
			instance = new ReportBroker(fileName);
		return instance;
	}

	public ReportBroker addTemplate(String location, String templateName) {
		JasperDesign jasperDesign = null;
		String templateLocation = path + location + templateName + extension;
		File file;
		try {
			file = new File(this.getClass().getClassLoader().getResource(templateLocation).toURI());
		} catch (URISyntaxException e) {
			throw LnwException.wrap("Error al convertir el template Path a URI", e)
						.addInfo("templateLocation", templateLocation)
						.addInfo("templateName", templateName);
		}

		if (!modificado(FileUtils.hashCode(file), templateName + "HashCode"))
			return this;
		try {
			jasperDesign = JRXmlLoader.load(file);
			getLogger().info("Compilando reporte: " + templateLocation);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			if (reports.containsKey(templateName))
				getLogger().info("Se ha actualizado el template " + templateName + ", " + templateLocation);
			reports.put(templateName, jasperReport);
			reports.put(templateName + "HashCode", FileUtils.hashCode(file));
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("Error de JasperReports", jrexception)
				.addInfo("templateLocation", templateLocation)
				.addInfo("templateName", templateName);
		}
		modificado = true;
		return this;
	}

	private boolean modificado(Object obj, Object obj1) {
		if (reports.containsKey(obj1)) {
			Object obj2 = reports.get(obj1);
			if (obj2.equals(obj))
				return false;
		}
		return true;
	}

	public ReportBroker addTemplates(String location, Collection templateNames) {
		for (Iterator iterator = templateNames.iterator(); iterator.hasNext();) {
			this.addTemplate(location, iterator.next().toString());
		}

		return this;
	}

	public synchronized JasperReport getTemplate(String templateName) {
		LnwException.assertTrue(reports.containsKey(templateName), "No se encontr\363 el template " + templateName);
		return (JasperReport)reports.get(templateName);
	}

	private Map initReportsMap(String s) {
		Map map;
		try {
			map = (Map)SerializerUtils.deserializar(s);
		}
		catch (Exception exception) {
			getLogger().error("No se pudo levantar el archivo " + s + " que contiene los reportes ya compilados");
			return CollectionFactory.createMap();
		}
		getLogger().info("Se levantaron los reportes ya compilados desde el archivo " + s);
		modificado = false;
		return map;
	}

	public void persistir(String s) {
		if (!modificado)
			return;
		try {
			SerializerUtils.serializar(s, (Serializable)reports);
		}
		catch (Exception exception) {
			getLogger().error("Fallo al intentar serializar los reportes compilados con destino en el archivo " + s);
			return;
		}
		getLogger().info("Se bajaron exitosamente los reportes compilados al archivo " + s);
	}

	public void persistir() {
		if (fileName == null) {
			throw new LnwException("Fallo al intentar bajar los reportes compilados a disco, el nombre del archivo de destino nunca fue especificado");
		} else {
			persistir(fileName);
			return;
		}
	}

	private Log getLogger() {
		return LOG;
	}

	public ReportBroker setPath(String path) {
		this.path = path;
		return this;
	}

	public ReportBroker setExtension(String extension) {
		this.extension = extension;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public ReportBroker setFileName(String s) {
		fileName = s;
		return this;
	}

	public boolean containsReport(String templateName) {
		return reports.containsKey(templateName);
	}

	public void clearCompiledReports() {
		this.reports.clear();
		modificado = true;
		getLogger().info("Se han borrado todas las versiones compiladas de los reportes");
	}
}
