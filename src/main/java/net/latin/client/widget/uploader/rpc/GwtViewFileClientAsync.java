package net.latin.client.widget.uploader.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.latin.client.rpc.GwtRpcInterfaceAsync;
import net.latin.client.widget.base.SimpleRespuestRPC;

public interface GwtViewFileClientAsync extends GwtRpcInterfaceAsync{

	void prepareForView(String fileId,AsyncCallback<SimpleRespuestRPC> callback);

}
