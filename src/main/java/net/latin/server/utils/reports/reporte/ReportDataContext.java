package net.latin.server.utils.reports.reporte;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Representa el context de datos que se utilizaran para generar un jasper report a traves del reportGenerator.
 * Solo unifica los parametros con el datasource.
 * 
 * @author <a href="mailto:jaf@synapsis-sa.com.ar">jfernandes</a> 
 */
public interface ReportDataContext {

	public abstract Map getParameters();

	public abstract JRDataSource getDataSource();
}
