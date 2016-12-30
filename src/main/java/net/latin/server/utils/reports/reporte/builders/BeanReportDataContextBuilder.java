package net.latin.server.utils.reports.reporte.builders;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.reporte.builders:
//			ReportDataContextBuilder

public class BeanReportDataContextBuilder extends ReportDataContextBuilder {

	public BeanReportDataContextBuilder() {
	}

	protected JRDataSource createDataSource() {
		return new JRBeanCollectionDataSource(getDataSourceList());
	}
}
