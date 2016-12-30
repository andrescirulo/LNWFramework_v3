package net.latin.server.utils.reports;

import java.util.List;
import java.util.Map;

import net.latin.server.persistence.sql.core.LnwQuery;

public class ReportData {
	private String fileName;
	private Map params;
	private LnwQuery query;
	private String dataSource;
	private List<String> subreports;
	private String printName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public LnwQuery getQuery() {
		return query;
	}

	public void setQuery(LnwQuery query) {
		this.query = query;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public List<String> getSubreports() {
		return subreports;
	}

	public void setSubreports(List<String> subreports) {
		this.subreports = subreports;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

}
