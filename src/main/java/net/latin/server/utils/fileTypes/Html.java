package net.latin.server.utils.fileTypes;

import java.io.Serializable;

/**
 * Representa un archivo HTML que va a ser mostrado.
 * Posee el contenido del archivo en String
 *
 * @author Matias Leone
 */
public class Html implements FileToShowOnClient , Serializable{
	private static final long serialVersionUID = -2425078775812879932L;
	
	private String name;
	private String content;

	public Html(String name, String content) {
		this.name = name;
		this.content = content;
	}



	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	public int getLength() {
		return content.length();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setStringContent(String content) {
		this.content = content;
	}

	@Override
	public byte[] getContent() {
		return content.getBytes();
	}

	public String getStringContent(){
		return content;
	}

	public String toString() {
		return this.name + " : " + getContentType() + "("+this.getLength()+" bytes)";
	}

}
