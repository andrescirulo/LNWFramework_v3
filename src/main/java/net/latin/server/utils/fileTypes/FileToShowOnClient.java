package net.latin.server.utils.fileTypes;


/**
 * Interface que provee el acceso a la informacion necesaria a ser usada por un
 * servlet que se encargue de mostrar el archivo en cuestion.
 *
 * @author Martin D'Aloia
 */
public interface FileToShowOnClient {

	/**
	 * @return Devuelve el MIME type a ser usado por los servlets para setear el header
	 * 			de HTTP denominado Content-Type.
	 */
	public String getContentType();

	/**
	 * @return Tamaño del contenido del archivo.
	 */
	public int getLength();

	/**
	 * @return Nombre del archivo.
	 */
	public String getName();


	/**
	 * @return Contenido del archivo
	 */
	public byte[] getContent();



}
