package net.latin.server.utils.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.latin.client.widget.format.GwtFormatUtils;
import net.latin.server.fileUpload.FileDownloaderServlet;
import net.latin.server.persistence.LnwStoredProcedureJdbcTemplate;
import net.latin.server.persistence.SpringUtils;
import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.fileTypes.Pdf;
import net.latin.server.utils.fileTypes.Xls;
import net.latin.server.utils.reports.visualizer.ReportViewerServlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class LnwDatabaseReportBuilder {

	
	private static final String extension = ".jrxml";
	private static String path;

	static{
		path=LnwGeneralConfig.getInstance().getReportsClasspath();
	}
	
	public static void buildPdfToFile(String fileName,Map params,LnwQuery query,String dataSource,String destFileName,List<String> subreports){
		try {
			String templateLocation = path + "/" + fileName + extension;
			agregarSubReports(params,subreports);
			JasperExportManager.exportReportToPdfFile(generate(templateLocation, params, query,dataSource), destFileName + ".pdf");
		} catch (Exception e) {
			throw new LnwException("No se pudo guardar el Reporte ", e);
		}
	}
	public static void buildXlsxToFile(String fileName,Map params,LnwQuery query,String dataSource,String destFileName,List<String> subreports){
		try {
			String templateLocation = path + "/" + fileName + extension;
			agregarSubReports(params,subreports);
			
			JRXlsxExporter exporter = new JRXlsxExporter();
			JasperPrint jasperPrint = generate(templateLocation, params, query,dataSource);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFileName + ".xlsx"));
			exporter.exportReport();
		} catch (Exception e) {
			throw new LnwException("No se pudo guardar el Reporte ", e);
		}
	}
	
	public static void buildXlsxToFile(List<ReportData> reportData,String destFileName){
		try {
			JRXlsxExporter exporter = new JRXlsxExporter();
			List<JasperPrint> sheets=new ArrayList<JasperPrint>();
			for (ReportData rep:reportData){
				String templateLocation = path + "/" + rep.getFileName() + extension;
				agregarSubReports(rep.getParams(),rep.getSubreports());
				
				JasperPrint jasperPrint = generate(templateLocation, rep.getParams(), rep.getQuery(),rep.getDataSource());
				jasperPrint.setName(rep.getPrintName());
				sheets.add(jasperPrint);
			}
			exporter.setExporterInput(SimpleExporterInput.getInstance(sheets));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFileName + ".xlsx"));
			exporter.exportReport();
		} catch (Exception e) {
			throw new LnwException("No se pudo guardar el Reporte ", e);
		}
	}
	
	public static void buildWebXlsx(List<ReportData> reportData,String destFileName){
		try {
			JRXlsxExporter exporter = new JRXlsxExporter();
			List<JasperPrint> sheets=new ArrayList<JasperPrint>();
			for (ReportData rep:reportData){
				String templateLocation = path + "/" + rep.getFileName() + extension;
				agregarSubReports(rep.getParams(),rep.getSubreports());
				
				JasperPrint jasperPrint = generate(templateLocation, rep.getParams(), rep.getQuery(),rep.getDataSource());
				jasperPrint.setName(rep.getPrintName());
				sheets.add(jasperPrint);
			}
			exporter.setExporterInput(SimpleExporterInput.getInstance(sheets));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(baos);
			exporter.setExporterOutput(output);
			exporter.exportReport();
			FileDownloaderServlet.setFileToShow(new Xls(destFileName + ".xlsx", baos.toByteArray()));
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}

	public static void buildPdfToFile(String fileName,Map params,LnwQuery query,String dataSource,String destFileName){
		buildPdfToFile(fileName, params, query,dataSource, destFileName, null);
	}
	
	public static void buildPdfToFile(String fileName,Map params,LnwQuery query,String destFileName,List<String> subreports){
		buildPdfToFile(fileName, params, query, null, destFileName,subreports);
	}
	
	public static void buildPdfToFile(String fileName,Map params,LnwQuery query,String destFileName){
		buildPdfToFile(fileName, params, query, null, destFileName,null);
	}
	
		
	public static void buildWebPdf(String fileName,Map params,LnwQuery query,String dataSource,List<String> subreports) {
		try {
			String templateLocation = path + "/" + fileName + extension;
			agregarSubReports(params, subreports);
			byte[] pdfBytes = JasperExportManager.exportReportToPdf(generate(templateLocation, params, query,dataSource));
			
			ReportViewerServlet.setFileToShow(new Pdf(fileName + ".pdf", pdfBytes));
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}
	
	public static void buildWebXlsx(String fileName,Map params,LnwQuery query,String dataSource,List<String> subreports) {
		try {
			String templateLocation = path + "/" + fileName + extension;
			agregarSubReports(params, subreports);
			
			JRXlsxExporter exporter = new JRXlsxExporter();
			JasperPrint jasperPrint = generate(templateLocation, params, query,dataSource);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(baos);
			exporter.setExporterOutput(output);
			exporter.exportReport();
			ReportViewerServlet.setFileToShow(new Xls("filename" + ".xlsx", baos.toByteArray()));
		} catch (Exception e) {
			throw new LnwException("No se pudo generar el Reporte Web", e);
		}
	}
	
	public static void buildWebPdf(String fileName,Map params,LnwQuery query,String dataSource) {
		buildWebPdf(fileName, params, query, dataSource, null);
	}
	public static void buildWebPdf(String fileName,Map params,LnwQuery query) {
		buildWebPdf(fileName, params, query, null,null);
	}
	
	public static void buildWebPdf(String fileName,Map params,LnwQuery query,List<String> subreports) {
		buildWebPdf(fileName, params, query, null,subreports);
	}
	
	private static JasperPrint generate(String reportPath, Map params,LnwQuery query, String dataSource) throws Exception{
		LnwStoredProcedureJdbcTemplate spJdbcTemplate;
		if (dataSource==null){
			spJdbcTemplate = new LnwStoredProcedureJdbcTemplate(SpringUtils.getJdbcTemplate());
		}
		else{
			spJdbcTemplate = new LnwStoredProcedureJdbcTemplate(SpringUtils.getJdbcTemplate(dataSource));
		}
		final Connection currentConnection = spJdbcTemplate.createNewConnection();
		try{
			Connection nativeConnection = SpringUtils.getJdbcTemplate().getNativeJdbcExtractor().getNativeConnection(currentConnection);
			URI uri = LnwDatabaseReportBuilder.class.getClassLoader().getResource(reportPath).toURI();
			File file = new File(uri);
			
			JasperReport jasperReport = compileReport(file,query);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, nativeConnection);
			return jasperPrint;
		}
		finally{
			try {
				currentConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static JasperReport compileReport(File file, LnwQuery query) {
		JasperDesign jasperdesign = null;
		JRDesignQuery jrDesignQuery = new JRDesignQuery();
		if (query!=null){
			PreparedStatement statement = query.buildQuery();
			String sentencia = statement.getSentencia().trim();
			for ( Object param :statement.getParams()){
				sentencia=sentencia.replaceFirst("\\?", getFormatedParameter(param));
			}
			jrDesignQuery.setText(sentencia);
			System.out.println(sentencia);
		}
		try {
			//Cargo la plantilla y la compilo
			jasperdesign = JRXmlLoader.load(file);
			if (query!=null){
				jasperdesign.setQuery(jrDesignQuery);
			}
			
			return JasperCompileManager.compileReport(jasperdesign);
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se ha podido compilar el reporte", jrexception)
				.addInfo("templateName", file);
		}
	}
	
	private static void compileReportToFile(File file, LnwQuery query,String destFile) {
		JasperDesign jasperdesign = null;
		JRDesignQuery jrDesignQuery = new JRDesignQuery();
		if (query!=null){
			PreparedStatement statement = query.buildQuery();
			String sentencia = statement.getSentencia().trim();
			for ( Object param :statement.getParams()){
				sentencia=sentencia.replaceFirst("\\?", getFormatedParameter(param));
			}
			jrDesignQuery.setText(sentencia);
			System.out.println(sentencia);
		}
		try {
			//Cargo la plantilla y la compilo
			jasperdesign = JRXmlLoader.load(file);
			if (query!=null){
				jasperdesign.setQuery(jrDesignQuery);
			}
			
			JasperCompileManager.compileReportToFile(jasperdesign,destFile);
		}
		catch (JRException jrexception) {
			throw LnwException.wrap("No se ha podido compilar el reporte", jrexception)
				.addInfo("templateName", file);
		}
	}
	
	private static void agregarSubReports(Map params,List<String> subreports) {
		if (subreports!=null){
			for (String subR:subreports){
				try {
					String subTemplateLocation = path + "/" + subR + extension;
					URI uriSubr;
						uriSubr = LnwDatabaseReportBuilder.class.getClassLoader().getResource(subTemplateLocation).toURI();
					File fileSubr = new File(uriSubr);
					String destFile=fileSubr.getAbsolutePath().replace(".jrxml", ".jasper");
//					JasperReport compiledReport = compileReport(fileSubr,null);
					compileReportToFile(fileSubr,null,destFile);
					params.put(subR,destFile);
				} catch (URISyntaxException e) {
					throw new RuntimeException("No se encontró el subreport" + subR,e);
				}
			}
		}
		
	}

	//ES UN ASCO, PERO NO ENCONTRAMOS OTRA MANERA
	private static String getFormatedParameter(Object param) {
		String paramString = param.toString();
		if (param instanceof String){
			return "'" + paramString.replace("'", "''") + "'";
		}
		else if (param instanceof Date){
			return "'" + GwtFormatUtils.formatDateAndHour((Date) param).replace("- ", "") + "'";
		}
		else
		{
			return paramString;
		}
	}
	
}
