package net.latin.server.utils.fileTypes;



/**
 * Representa a un archivo PDF.
 *
 * Tiene un serialVersionUID dado que BaseFile implementa java.io.Serializable
 * para que pueda ser, por ejemplo, guardado en session o enviado por RPC.
 *
 * @author Martin D'Aloia
 */
public class Pdf extends BaseFile {

	private static final long serialVersionUID = -2936363020909477195L;

	public final static String PDF_CONTENT_TYPE = "application/pdf";

	/**
	 * Crea un PDF con un nombre y un contenido.
	 *
	 * @param name Nombre que se le quiere dar al PDF.
	 * @param content Contenido (como array de bytes) del PDF.
	 */
	public Pdf(String name, byte[] content) {
		super(name, content);
	}

	/**
	 * @return Devuelve el MIME type de un archivo PDF.
	 */
	public String getContentType() {
		return PDF_CONTENT_TYPE;
	}

}
