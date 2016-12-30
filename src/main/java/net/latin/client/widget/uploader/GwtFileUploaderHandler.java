package net.latin.client.widget.uploader;

import gwt.material.design.addins.client.fileuploader.js.File;

public interface GwtFileUploaderHandler {
	
	public void onUploadComplete(String fileId,String fileName);
	
	/**
	 * Para cuando se elimina algún archivo subido en el flash uploader.
	 * @param fileId: El id del archivo que se solicitó eliminar. El archivo se busca por ese id en la session
	 * (o sino en la base) (ver el método saveFiles de FlashFileUploaderServlet)
	 * @param fileName: Nombre del archivo (para buscarlo en la base generalmente)
	 * @param componentId: Id del componente para saber a qué componente pertenece el archivo que solicitaron
	 */
	public void onFileCanceled(String fileId, String fileName, String componentId);
	
	
	/**
	 * Valida que el File se pueda subir (como por ejemplo para el caso en el que no se quiera que haya 2 archivos
	 * con el mismo nombre)
	 * @param fileToAttemptUpload: file que se quiere subir
	 * @return
	 */
	public boolean isValidFileToUpload(File fileToAttemptUpload);

}
