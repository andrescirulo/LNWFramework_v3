package net.latin.server.utils.fileTypes;


/**
 * Representa a un archivo XLS (Microsoft Excel).
 *
 * Tiene un serialVersionUID dado que BaseFile implementa java.io.Serializable
 * para que pueda ser, por ejemplo, guardado en session o enviado por RPC.
 *
 * @author Martin D'Aloia
 */
public class Xls extends BaseFile {

	private static final long serialVersionUID = 910429991225541047L;

	public final static String XLS_CONTENT_TYPE = "application/vnd.ms-excel";

	/**
	 * Crea un XLS con un nombre y un contenido.
	 *
	 * @param name Nombre que se le quiere dar al XLS.
	 * @param content Contenido (como array de bytes) del XLS.
	 */
	public Xls(String name, byte[] content) {
		super(name, content);
	}

	/**
	 * @return Devuelve el MIME type de un archivo XLS.
	 */
	public String getContentType() {
		return XLS_CONTENT_TYPE;
	}
}
