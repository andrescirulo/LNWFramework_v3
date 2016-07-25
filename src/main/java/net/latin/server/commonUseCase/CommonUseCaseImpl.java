package net.latin.server.commonUseCase;

import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.menu.data.MenuItem;

public interface CommonUseCaseImpl {

	/**
	 * Se debe cargar la información general del usuario del pedido actual,
	 * que se desea enviar al cliente.
	 * Ejemplo: categoria, perfil, etc.
	 * Se puede customizar el menu a partir de esta informacion
	 */
	public CustomBean getInitialInfo();

	/**
	 * Obtiene la estructura del menu que sera usado para armar el menu
	 * correspondiente a los permisos del usuario logueado.
	 *
	 * El source de los datos puede ser cualquiera ya que depende de la
	 * implementacion de este metodo, por lo tanto pueden ser obtenidos desde
	 * un XML, desde la DB o cualquier otro data source.
	 */
	public MenuItem getGwtMenu();

	/**
	 * Pide el HTML de documentacion para un caso de uso en particular.
	 * @param useCaseName nombre del caso de uso segun la clase UseCaseNames
	 * @param pageName nombre de la pagina en la que actualmente se encuentra el
	 * usuario (por si se desea mostrar algun contenido diferente) correspondiente
	 * al nombre registrado en el PageGroup de ese caso de uso.
	 * @return string HTML de la documentacion a mostrar
	 */
	public String getCaseDocumentation(String useCaseName, String pageName);

}
