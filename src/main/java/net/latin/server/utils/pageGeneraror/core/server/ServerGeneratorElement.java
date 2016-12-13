package net.latin.server.utils.pageGeneraror.core.server;

import java.util.List;

/**
 * Contiene la info requerida para instanciar
 * un generador de la parte server
 * @author Santiago Aimetta
 *
 */
public interface ServerGeneratorElement {

	/**
	 * Retorna el path del proyecto, donde se
	 * encuentra el gwt.xml
	 */
	public String getProjectPath();

	/**
	 * Retorna los metodos que se generarán se utilizaran
	 * para generar las rpc's y el case
	 *
	 * @return
	 */
	public List<ServerMethod> getServerMethods();

	/**
	 * Retorna el nombre del caso al cual se le generará
	 * la parte server
	 * @return
	 */
	public String getCaseName();

	/**
	 * Retorna una lista con los imports necesarios en el server
	 * @return
	 */
	public List<String> getServerImports();

	/**
	 * Retorna una lista con los imports necesarios en las interfaces rpc
	 * @return
	 */
	public List<String> getRpcImports();

	/**
	 * Retorna una lista con las interfaces que implementará el server.
	 * @return
	 */
	public List<String> getImplementedInterfaces();
}
