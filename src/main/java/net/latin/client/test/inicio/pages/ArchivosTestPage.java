package net.latin.client.test.inicio.pages;

import net.latin.client.rpc.GwtRpc;
import net.latin.client.test.inicio.rpc.InicioTestClientAsync;
import net.latin.client.widget.base.GwtPage;
import net.latin.client.widget.panels.GwtVerticalPanel;
import net.latin.client.widget.uploader.GwtFileUploader;

public class ArchivosTestPage extends GwtPage {

	private InicioTestClientAsync server;

	public ArchivosTestPage() {
		server = (InicioTestClientAsync)GwtRpc.getInstance().getServer( "InicioTestCase" );
		
		GwtFileUploader upload=new GwtFileUploader();
		GwtVerticalPanel panel = new GwtVerticalPanel("Subir Archivos",true);
		panel.add(upload);
		this.add(panel);
	}
	
}
