package net.latin.server.utils.reports.tableDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.latin.server.utils.fileTypes.FileToShowOnClient;

public interface LnwTableDocumentGeneratorListener {

	public ByteArrayOutputStream makeDocument( LnwTableDocumentData data ) throws IOException;

	public FileToShowOnClient createAssociation( String fileName, ByteArrayOutputStream output );
	
}
