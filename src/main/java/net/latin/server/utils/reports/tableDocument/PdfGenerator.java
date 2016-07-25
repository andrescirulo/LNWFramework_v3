package net.latin.server.utils.reports.tableDocument;

import java.io.ByteArrayOutputStream;

import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.latin.server.utils.fileTypes.FileToShowOnClient;
import net.latin.server.utils.fileTypes.Pdf;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Genera un pdf a partir de los datos de la tabla
 * 
 * @author Leandro Ravanal
 *
 */
public class PdfGenerator implements LnwTableDocumentGeneratorListener{

	public ByteArrayOutputStream makeDocument( LnwTableDocumentData data ) {
		ByteArrayOutputStream output = new ByteArrayOutputStream(); ;
		
		try	{
		
			JasperPrint jasperPrint = DocumentGenerator.makeDocument( data );
			JasperExportManager.exportReportToPdfStream( jasperPrint, output );
			
		} catch ( JRException e ) {
			e.printStackTrace();
		} 
		
		return output;
	}


	public FileToShowOnClient createAssociation( String fileName, ByteArrayOutputStream output ) {
		return new Pdf( fileName + ".pdf", output.toByteArray() );
	}

}
