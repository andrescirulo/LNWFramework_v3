package net.latin.client.test.inicio;

import com.google.gwt.core.client.GWT;

import net.latin.client.rpc.GwtRpc;
import net.latin.client.rpc.GwtServerCreator;
import net.latin.client.test.inicio.pages.InicioTestPage;
import net.latin.client.test.inicio.rpc.InicioTestClient;
import net.latin.client.widget.base.GwtPageGroup;

public class InicioTestGroup extends GwtPageGroup implements GwtServerCreator{

	private final String PAGINA_INICIO="Inicio";
	
	@Override
	protected void registerRpcServers() {
		GwtRpc.getInstance().addServer( "InicioTestCase", "inicio.gwt" , this );
	}

	@Override
	protected void registerPages() {
		addPage(PAGINA_INICIO, new InicioTestPage());
	}

	@Override
	protected String registerFirstPage() {
		return PAGINA_INICIO;
	}

	@Override
	public Object createServer() {
		return GWT.create(InicioTestClient.class);
	}

}
