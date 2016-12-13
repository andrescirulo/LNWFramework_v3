package net.latin.server.utils.resources;

import java.io.File;
import java.net.URL;

import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.exceptions.LnwException;

/**
 * Utilidades para encontrar recursos
 * 
 * @author Matias Leone
 */
public class ResourceFinder {
	
	private ResourceFinder() {
	}
	
	/**
	 * Busca un recurso a través del ClassPath
	 * 
	 * @param path
	 *            ruta de paquetes donde se encuentra el recurso. Ejemplo
	 * 
	 *            <pre>
	 * ResourceFounder.get(com / images / myIcon.gif)
	 * </pre>
	 * @return URL del recurso
	 */
	public static URL getUrl(String path) {
		// FIXME quizas debería ser asi:
		// URL resource = ResourceFounder.class.getClassLoader().getResource(
		// path );
		URL resource = ResourceFinder.class.getResource(path);
		if (resource == null) {
			throw new LnwException("No se encontro el resource: " + path);
		} else {
			return resource;
		}
	}
	
	/**
	 * Busca un recurso a través del ClassPath
	 * 
	 * @param path
	 *            ruta de paquetes donde se encuentra el recurso. Ejemplo
	 * 
	 *            <pre>
	 * ResourceFounder.get(com / images / myIcon.gif)
	 * </pre>
	 * @return File del recurso
	 */
	public static File getFile(String path) {
		return new File(getUrl(path).getFile());
	}
	
	/**
	 * Retorna el path del proyecto
	 * 
	 * @return
	 */
	public static String getLocalProjectPath() {
		String path = new File("").getAbsolutePath();
		if (!path.toLowerCase().endsWith("\\" + LnwGeneralConfig.getInstance().getApplicationName().toLowerCase())) {
			path = path + "\\" + LnwGeneralConfig.getInstance().getApplicationName();
		}
		return path;
	}
	
	/**
	 * Si no existe retorna false
	 * 
	 * @param path
	 * @return
	 */
	public static boolean exist(String path) {
		URL resource = ResourceFinder.class.getResource(path);
		return (resource != null);
	}
	
}
