package net.latin.server.utils.reports.reporte.builders;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.latin.server.utils.collections.CollectionFactory;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.reports.reporte.ReportBroker;
import net.latin.server.utils.reports.reporte.ReportDataContext;
import net.latin.server.utils.reports.reporte.ReportDataContextImpl;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class ReportDataContextBuilder {

	private List dataSourceList;
	private Map parameters;
	private JRDataSource dataSource;

	public ReportDataContextBuilder() {
		dataSourceList = CollectionFactory.createList();
		parameters = CollectionFactory.createMap();
	}

	public ReportDataContextBuilder with(Object obj) {
		getDataSourceList().add(obj);
		return this;
	}

	public ReportDataContextBuilder withAll(Collection collection) {
		getDataSourceList().addAll(collection);
		return this;
	}

	public ReportDataContextBuilder withDataSource(JRDataSource jrdatasource) {
		setDataSource(jrdatasource);
		return this;
	}

	public void resetDataSource() {
		setDataSourceList(CollectionFactory.createList());
		setDataSource(null);
	}

	public ReportDataContextBuilder withParameter(String s, Object obj) {
		getParameters().put(s, obj);
		return this;
	}

	public ReportDataContextBuilder withParameters(Map map) {
		getParameters().putAll(map);
		return this;
	}

	public ReportDataContextBuilder withSubreport(String s) {
		getParameters().put(s, ReportBroker.getInstance().getTemplate(s));
		return this;
	}

	public ReportDataContextBuilder withSubreports(Collection collection) {
		for (Iterator iterator = collection.iterator(); iterator.hasNext(); withSubreport(iterator.next().toString()));
		return this;
	}

	public ReportDataContextBuilder withSubreportData(Object obj, Object obj1) {
		if (!(java.util.List.class).isAssignableFrom(obj1.getClass())) {
			throw LnwException.wrap("La clase recibe un objeto lista.", new Exception());
		} else {
			getParameters().put(obj, new JRBeanCollectionDataSource((List)obj1));
			return this;
		}
	}

	public ReportDataContext build() {
		if (getDataSource() == null)
			if (getDataSourceList().isEmpty())
				setDataSource(new JREmptyDataSource());
			else
				setDataSource(createDataSource());
		return new ReportDataContextImpl(getParameters(), getDataSource());
	}

	protected JRDataSource createDataSource() {
		if (isMapCollectionDataSource())
			return new JRMapCollectionDataSource(getDataSourceList());
		else
			return new JRBeanCollectionDataSource(getDataSourceList());
	}

	private boolean isMapCollectionDataSource() {
		Iterator iterator = getDataSourceList().iterator();
		if (iterator.hasNext())
			return (java.util.Map.class).isAssignableFrom(iterator.next().getClass());
		else
			return false;
	}

	public Map getParameters() {
		return parameters;
	}

	protected void setParameters(Map map) {
		parameters = map;
	}

	public JRDataSource getDataSource() {
		return dataSource;
	}

	protected void setDataSource(JRDataSource jrdatasource) {
		dataSource = jrdatasource;
	}

	public List getDataSourceList() {
		return dataSourceList;
	}

	protected void setDataSourceList(List list) {
		dataSourceList = list;
	}
}
