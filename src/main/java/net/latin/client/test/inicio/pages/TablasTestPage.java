package net.latin.client.test.inicio.pages;

import net.latin.client.rpc.GwtRpc;
import net.latin.client.test.inicio.rpc.InicioTestClientAsync;
import net.latin.client.widget.base.GwtPage;

public class TablasTestPage extends GwtPage {

	private InicioTestClientAsync server;

	public TablasTestPage() {
		server = (InicioTestClientAsync)GwtRpc.getInstance().getServer( "InicioTestCase" );
		
		
	}
	
}
