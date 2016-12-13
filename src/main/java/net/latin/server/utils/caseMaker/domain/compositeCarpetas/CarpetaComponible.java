package net.latin.server.utils.caseMaker.domain.compositeCarpetas;

import java.util.List;

/**
 * Interfaz para hacer un composite de carpetas
 *
 * @author Santiago Aimetta
 */
public interface CarpetaComponible {
	/**
	 * Agrega una subCarpeta
	 * @param CarpetaComponible carpeta
	 */
	public void agregarSubCarpeta( CarpetaComponible carpeta );
	/**
	 * Devuelve una lista con los paths absolutos de cada subcarpeta
	 * @return List<String>
	 */
	public List<String> getPath();
}
