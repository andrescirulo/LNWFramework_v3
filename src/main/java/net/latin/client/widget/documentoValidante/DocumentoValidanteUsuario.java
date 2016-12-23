package net.latin.client.widget.documentoValidante;

import net.latin.client.rpc.GwtAsyncCallback;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.msg.GwtMsgHandler;

public class DocumentoValidanteUsuario extends DocumentoValidante {

	public DocumentoValidanteUsuario(DocumentoValidanteDisplay display, GwtMsgHandler msglistener,
			GwtDocumentoValidanteListener doclistener) {
		super(display, msglistener, doclistener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void buscarEnServidor() {
		
		server.searchLetradoUsuario(currentPresenter.getValue(), new GwtAsyncCallback<CustomBean>() {
			public void onSuccess(CustomBean result) {
				//TODO verificar si lo encontro
				if(result.getBoolean("rta").booleanValue()) {
					setBuscar(false);
					doclistener.onLetradoFound(result.getBean("letrado"));
				}else {
					addError(result.getString("msg"));
					doclistener.onLetradoFound(null);
				}
			}
		});
		
	}

}
