package net.latin.client.widget.documentoValidante;

import net.latin.client.widget.msg.GwtMsgHandler;

public class DocumentoValidanteFactory {

	public static DocumentoValidante newDocumentoValidante(GwtMsgHandler msglistener, GwtDocumentoValidanteListener doclistener) {
		return new DocumentoValidante(new DocumentoValidanteView(), msglistener, doclistener);
	}
	
	public static DocumentoValidante newDocumentoValidanteUsuario(GwtMsgHandler msglistener, GwtDocumentoValidanteListener doclistener) {
		return new DocumentoValidanteUsuario(new DocumentoValidanteView(), msglistener, doclistener);
	}
	
}
