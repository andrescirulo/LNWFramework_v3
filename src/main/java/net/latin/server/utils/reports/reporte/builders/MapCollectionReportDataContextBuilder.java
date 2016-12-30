package net.latin.server.utils.reports.reporte.builders;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;


// Referenced classes of package ar.com.latinNet.desarrollo.impresion.reporte.builders:
//			ReportDataContextBuilder

public class MapCollectionReportDataContextBuilder extends ReportDataContextBuilder {

	public MapCollectionReportDataContextBuilder() {
	}

	protected JRDataSource createDataSource() {
		return new JRMapCollectionDataSource(getDataSourceList());
	}
}
