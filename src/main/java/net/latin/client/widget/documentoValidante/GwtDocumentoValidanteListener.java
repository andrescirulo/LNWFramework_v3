package net.latin.client.widget.documentoValidante;

import net.latin.client.widget.base.CustomBean;

public interface GwtDocumentoValidanteListener {
	void onTipoDocumentoValidanteDetected(TiposDocumentoValidante doc, DocumentoValidante widget);
	void onLetradoFound(CustomBean letrado);
	void onBuscar(DocumentoValidante widget);
	void onCambiar(DocumentoValidante widget);
}
