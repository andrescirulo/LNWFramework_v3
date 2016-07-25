package net.latin.server.utils.reports.tableDocument;

import java.io.ByteArrayOutputStream;

import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.latin.server.utils.fileTypes.FileToShowOnClient;
import net.latin.server.utils.fileTypes.Xls;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * Genera un xls a partir de los datos de la tabla
 * 
 * @author Leandro Ravanal
 *
 */
public class XlsGenerator extends JRXlsExporter implements LnwTableDocumentGeneratorListener {

	public ByteArrayOutputStream makeDocument( LnwTableDocumentData data ) {
		ByteArrayOutputStream output = new ByteArrayOutputStream(); ;
		
		try	{
		
			JasperPrint jasperPrint = DocumentGenerator.makeDocument( data );
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
			exporter.setParameter( JRExporterParameter.OUTPUT_STREAM, output );
			exporter.setParameter( JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE );
			exporter.exportReport();
			
		} catch ( JRException e ) {
			e.printStackTrace();
		} 
		
		return output;
	}

	public FileToShowOnClient createAssociation( String fileName, ByteArrayOutputStream output ) {
		return new Xls( fileName + ".xls", output.toByteArray() );
	}

}
