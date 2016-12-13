package net.latin.server.utils.caseMaker.domain.compositeCarpetas;

import java.util.ArrayList;
import java.util.List;
/**
 * Vendría a ser el compuesto del composite.
 *
 * @author Santiago Aimetta
 */
public class CarpetaCompuesta implements CarpetaComponible {
	private String path ="";
	private List<CarpetaComponible> subCarpetas = null;

	public CarpetaCompuesta(String path) {
		this.path = path;
	}

	public void agregarSubCarpeta(CarpetaComponible carpeta) {
		if( subCarpetas == null ){
			subCarpetas = new ArrayList<CarpetaComponible>();
		}
		subCarpetas.add(carpeta);
	}


	public List<String> getPath() {
		if(this.subCarpetas == null){
			return null;
		}
		else{
			List<String> paths = new ArrayList<String>();
			for (CarpetaComponible carpeta : this.subCarpetas) {
				for (String subPath : carpeta.getPath()) {
					paths.add(path+"/"+subPath);
				}
			}
			return paths;
		}

	}

}
