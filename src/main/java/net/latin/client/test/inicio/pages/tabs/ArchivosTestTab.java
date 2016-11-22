package net.latin.client.test.inicio.pages.tabs;

import net.latin.client.rpc.GwtRpc;
import net.latin.client.test.inicio.rpc.InicioTestClientAsync;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.panels.GwtVerticalPanel;
import net.latin.client.widget.tabs.GwtMaterialTab;
import net.latin.client.widget.uploader.GwtFileUploader;

public class ArchivosTestTab extends GwtMaterialTab {

	private InicioTestClientAsync server;

	public ArchivosTestTab(GwtMensajesHandler handler) {
		super(handler);
		server = (InicioTestClientAsync)GwtRpc.getInstance().getServer( "InicioTestCase" );
		
		GwtFileUploader upload=new GwtFileUploader(this);
		GwtVerticalPanel panel = new GwtVerticalPanel("Subir Archivos",true);
		panel.add(upload);
		this.add(panel);
		upload.setAcceptedFiles("application/pdf");
		upload.setViewServer(server);
		upload.setDownloadServer(server);
	}
	
}
