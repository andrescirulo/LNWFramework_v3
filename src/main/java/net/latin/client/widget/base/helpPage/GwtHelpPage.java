package net.latin.client.widget.base.helpPage;

/**
 * Interface para la visualizacion de contenido de ayuda dentro de la aplicacion
 *
 * @author Matias Leone
 */
public interface GwtHelpPage {


	/**
	 * Mostrar el HTML de documentacion para un caso de uso en particular.
	 * @param useCaseName nombre del caso de uso segun la clase UseCaseNames
	 * @param pageName nombre de la pagina en la que actualmente se encuentra el
	 * usuario correspondiente al nombre registrado en el PageGroup de ese caso de uso.
	 * @param documentation string HTML de la documentacion a mostrar
	 */
	public void showCaseDocumentation(String useCaseName, String pageName, String documentation);

}
