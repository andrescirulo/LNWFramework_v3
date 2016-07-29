package net.latin.client.test.inicio.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.latin.client.rpc.GwtRpcInterfaceAsync;
import net.latin.client.widget.base.SimpleRespuestRPC;

public interface InicioTestClientAsync extends GwtRpcInterfaceAsync{

	void getTextoInicial(AsyncCallback<SimpleRespuestRPC> callback);

}
