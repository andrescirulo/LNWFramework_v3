package net.latin.server.utils.fileTypes;

/**
 * Representa a un archivo RTF (Microsoft Word).
 * 
 */
public class Rtf extends BaseFile {
	private static final long serialVersionUID = -7381019600529682999L;

	public final static String RTF_CONTENT_TYPE = "text/rtf";
	
	/**
	 * Crea un RTF con un nombre y un contenido.
	 *
	 * @param name Nombre que se le quiere dar al RTF.
	 * @param content Contenido (como array de bytes) del RTF.
	 */
	public Rtf(String name, byte[] content) {
		super(name, content);
	}

	/**
	 * @return Devuelve el MIME type de un archivo RTF.
	 */
	public String getContentType() {
		return RTF_CONTENT_TYPE;
	}

}
