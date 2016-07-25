package net.latin.server.utils.reports.tableDocument;

import java.io.ByteArrayOutputStream;

import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.latin.server.utils.fileTypes.FileToShowOnClient;
import net.latin.server.utils.fileTypes.Rtf;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * Genera un xls a partir de los datos de la tabla
 * 
 * @author Leandro Ravanal
 *
 */
public class RtfGenerator extends JRXlsExporter implements LnwTableDocumentGeneratorListener {

	public ByteArrayOutputStream makeDocument( LnwTableDocumentData data ) {
		ByteArrayOutputStream output = new ByteArrayOutputStream(); ;
		
		try	{
		
			JasperPrint jasperPrint = DocumentGenerator.makeDocument( data );
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
			exporter.setParameter( JRExporterParameter.OUTPUT_STREAM, output );
			exporter.exportReport();
			
		} catch ( JRException e ) {
			e.printStackTrace();
		} 
		
		return output;
	}

	public FileToShowOnClient createAssociation( String fileName, ByteArrayOutputStream output ) {
		return new Rtf( fileName + ".rtf", output.toByteArray() );
	}

}
