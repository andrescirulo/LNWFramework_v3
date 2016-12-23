package net.latin.client.widget.documentoValidante;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TiposDocumentoValidante implements IsSerializable {
	
	DNI(2,"DNI"),
	MATRICULA_PERITO(3,"Perito Matrícula"),
	NULL(6,"NULL"),
	TOMO_FOLIO(11,"Letrado Tomo/Folio"),
	MATRICULA_FEDERAL(17,"Letrado Matrícula"),
	CUIL(19,"CUIL/CUIT"),
	MATRICULA_CORTE(20,"Matricula otorgada por CSJN"),
	CUIT_PROVEEDOR(21,"CUIT Proveedor"),
	MATRICULA_PROVINCIAL(28,"Matricula Provincial");
	
	private int id;
	private String documentoValidanteString;
	
	private TiposDocumentoValidante(Integer id, String documentoValidanteString) {
		this.id = id;
		this.documentoValidanteString = documentoValidanteString;
	}
	
	public Integer getId() {
		return id;
	}
	
	public static TiposDocumentoValidante getForId(Integer id) {
		for (TiposDocumentoValidante tipo:TiposDocumentoValidante.values()){
			if (tipo.getId().equals(id)){
				return tipo;
			}
		}
		return null;
	}

	public String getDocumentoValidanteString() {
		return documentoValidanteString;
	}


}
