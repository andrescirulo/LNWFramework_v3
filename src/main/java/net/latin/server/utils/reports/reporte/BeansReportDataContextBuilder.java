package net.latin.server.utils.reports.reporte;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.latin.server.utils.collections.CollectionFactory;
import net.latin.server.utils.exceptions.LnwException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.reporte:
//			ReportDataContextImpl, ReportBroker, ReportDataContext

/**
 * Construye un contexto de datos que se utilizaran para generar un reporte.
 * El DataSource esta formado por una collection de beans, sobre los cuales se podran pedir propiedades
 * en el reporte utilizando los fields $F{nombreDeAtributo}.
 *
 * @author <a href="mailto:jaf@synapsis-sa.com.ar">jfernandes</a>
 */
public class BeansReportDataContextBuilder {

	private Collection beans;
	private Map parameters;
	private JRDataSource dataSource;

	public BeansReportDataContextBuilder() {
		beans = CollectionFactory.createList();
		parameters = CollectionFactory.createMap();
	}

    /**
     * Agrega un bean al conjunto de datos.
     * @param obj El bean a agregar.
     * @return
     */
	public BeansReportDataContextBuilder withBean(Object obj) {
		getBeans().add(obj);
		return this;
	}

	public BeansReportDataContextBuilder withBean(Object obj, Map map) {
		throw new UnsupportedOperationException();
	}

    /**
     * Agrega un grupo beans al conjunto de datos.
     * @param beansCollection Grupo de beans a agregar.
     * @return
     */
	public BeansReportDataContextBuilder withAllBeans(Collection beansCollection) {
		getBeans().addAll(beansCollection);
		return this;
	}

	public BeansReportDataContextBuilder withDataSource(Collection collection) {
		setDataSource(new JRMapCollectionDataSource(collection));
		return this;
	}

	public BeansReportDataContextBuilder withDataSource(JRDataSource jrdatasource) {
		setDataSource(jrdatasource);
		return this;
	}

	public BeansReportDataContextBuilder withParameter(Object name, Object value) {
		getParameters().put(name, value);
		return this;
	}

	public BeansReportDataContextBuilder withParameters(Map parametersMap) {
		getParameters().putAll(parametersMap);
		return this;
	}

    /**
     * Agrega un objeto a usarse como datos de un subreporte.
     * @param name Llave de busqueda del dato.
     * @param data Datos para el subreporte.
     */
	public BeansReportDataContextBuilder withSubreportData(Object name, Object data) {
		if (!(java.util.List.class).isAssignableFrom(data.getClass())) {
			throw LnwException.wrap("La clase recibe un objeto lista.", new Exception());
		}

		this.getParameters().put(name, new JRBeanCollectionDataSource((List)data));
		return this;
	}

    /**
     * Lee y compila un subreporte indicado su ubicacion a través de una path o una url.
     * @param templateName Ubicación del subreporte como path absoluto o url.
     */
	public BeansReportDataContextBuilder withSubreport(String templateName) {
		getParameters().put(templateName, ReportBroker.getInstance().getTemplate(templateName));
		return this;
	}

	public BeansReportDataContextBuilder withSubreports(Collection templateNames) {
		for (Iterator iterator = templateNames.iterator(); iterator.hasNext();) {
			this.withSubreport(iterator.next().toString());
		}

		return this;
	}

    /**
     * Construye el almacen de datos con todo lo previamente cargado.
     * @return El contenedor de los datos para un reporte.
     */
	public ReportDataContext build() {
		if (getDataSource() == null)
			if (getBeans().isEmpty())
				setDataSource(new JREmptyDataSource());
			else
				setDataSource(new JRBeanCollectionDataSource(getBeans()));
		return new ReportDataContextImpl(getParameters(), getDataSource());
	}

	private Map getParameters() {
		return parameters;
	}

	private void setParameters(Map map) {
		parameters = map;
	}

	private JRDataSource getDataSource() {
		return dataSource;
	}

	private void setDataSource(JRDataSource jrdatasource) {
		dataSource = jrdatasource;
	}

	private Collection getBeans() {
		return beans;
	}

	private void setBeans(Collection collection) {
		beans = collection;
	}
}
