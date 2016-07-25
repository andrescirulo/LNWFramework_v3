package net.latin.client.rpc.commonUseCase;

import net.latin.client.rpc.GwtRpcInterface;

/**
 * Interfaz RPC para utilidades comunes del server y de widgets en general
 *
 * @author Matias Leone
 */
public interface CommonUseCaseClient extends GwtRpcInterface {

	/**
	 * Gets the menu enties and description of category and perfil for the current user
	 */
	public InitialInfo getInitialInfo();

	/**
	 * Check for the current user the specified service
	 */
	public Boolean checkAccess(String serviceName);

	/**
	 * Genera un documento a partir de la tabla
	 */
	public void generateTableDocument( String fileName, String fileType, LnwTableDocumentData data );

	/**
	 * Cuando el usuario cierra su ventana del browser.
	 */
	public void onUserExit();

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
