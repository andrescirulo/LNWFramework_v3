package net.latin.server.utils.reports.reporte;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.reporte:
//			ReportDataContext

/**
 * Implementación básica.
 * El subclaseo y la complejidad (abstraccion) estan en los builders, luego siempre se resume en este objeto
 * tonto ya que es lo que necesita el Jasper.
 * 
 * @author <a href="mailto:jaf@synapsis-sa.com.ar">jfernandes</a> 
 */
public class ReportDataContextImpl implements ReportDataContext {

	private Map parameters;
	private JRDataSource dataSource;

	public ReportDataContextImpl(Map parameters, JRDataSource dataSource) {
		this.parameters = new HashMap();
		this.parameters = parameters;
		this.dataSource = dataSource;
	}

    /**
     * Retorna el mapa de parametros necesarios para popular el reporte.
     */
	public Map getParameters() {
		return this.parameters;
	}

    /**
     * Retorna el objeto Jasper DataSource necesario para popular el reporte.
     */
	public JRDataSource getDataSource() {
		return this.dataSource;
	}
}
