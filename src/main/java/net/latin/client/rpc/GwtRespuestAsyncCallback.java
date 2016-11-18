package net.latin.client.rpc;

import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.client.widget.modal.GwtModal;
import net.latin.client.widget.msg.GwtMensajesListener;



/**
 * Callback with some helps for GwtPage
 *
 */
public abstract class GwtRespuestAsyncCallback<S extends SimpleRespuestRPC> extends GwtAsyncCallback<S>{
	
	private GwtMensajesListener msgs;

	public GwtRespuestAsyncCallback(GwtMensajesListener msgs) {
		this.msgs = msgs;
	}
	
	@Override
	public void onSuccess(S result) {
		if (result.getRespuesta()){
			onOk(result);
		}
		else{
			onNoOk(result);
			msgs.addErrorMessage(result.getMensaje());
			GwtModal.unblockScreen();
		}
	}
	
	public abstract void onOk(S result);
	
	public void onNoOk(S result){
		
	}
}
