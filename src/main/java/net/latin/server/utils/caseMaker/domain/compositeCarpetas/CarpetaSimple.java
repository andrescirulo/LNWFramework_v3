package net.latin.server.utils.caseMaker.domain.compositeCarpetas;

import java.util.ArrayList;
import java.util.List;

/**
 * Carpeta simple, es la hoja del composite
 *
 * @author Santiago Aimetta
 */
public class CarpetaSimple implements CarpetaComponible {
	private String nombre;


	public CarpetaSimple(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * No hace nada, es una hoja
	 */
	public void agregarSubCarpeta(CarpetaComponible carpeta) {
	}

	public List<String> getPath() {
		List<String> nombre = new ArrayList<String>();
		nombre.add(this.nombre);
		return nombre ;
	}

}
