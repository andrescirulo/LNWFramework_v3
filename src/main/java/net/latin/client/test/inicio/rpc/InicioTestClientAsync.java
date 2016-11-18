package net.latin.client.test.inicio.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.latin.client.rpc.GwtRpcInterfaceAsync;
import net.latin.client.test.inicio.pages.domain.Persona;
import net.latin.client.widget.base.RespuestRPC;
import net.latin.client.widget.base.SimpleRespuestRPC;

public interface InicioTestClientAsync extends GwtRpcInterfaceAsync{

	void getTextoInicial(AsyncCallback<SimpleRespuestRPC> callback);

	void getListaPersonas(AsyncCallback<RespuestRPC<Persona>> callback);
}
