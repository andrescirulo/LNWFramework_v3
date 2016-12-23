package net.latin.client.widget.documentoValidante.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.latin.client.rpc.GwtRpcInterfaceAsync;
import net.latin.client.widget.base.CustomBean;

public interface IDocumentoValidanteServiceAsync extends GwtRpcInterfaceAsync {
	
	public void searchLetrado(CustomBean value, AsyncCallback<?> callback);
	
	public void getTipoDocumentoValidante(AsyncCallback<?> callback);
	
	public void searchLetradoUsuario(CustomBean value, AsyncCallback<?> callback);
	
	public void searchLetrado(CustomBean value, Long idRequisito, AsyncCallback<CustomBean> gwtAsyncCallback);
}
