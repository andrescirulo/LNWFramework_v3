package net.latin.server.utils.fileTypes;

import java.io.Serializable;

/**
 * Representa a un archivo a ser mostrado en el cliente.
 * Contiene la informacion basica necesaria.
 *
 * Implementa java.io.Serializable para que pueda ser, por ejemplo, guardado en
 * session o enviado por RPC.
 *
 * @author Martin D'Aloia
 */
public abstract class BaseFile implements FileToShowOnClient, Serializable{

	private String name;
	private byte[] content;

	public abstract String getContentType();

	public BaseFile(String name, byte[] content) {
		this.name = name;
		this.content = content;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getLength() {
		return content.length;
	}

	public String getName() {
		return name;
	}

	public byte[] getContent() {
		return content;
	}
	@Override
	public String toString() {
		return this.name + " : " + getContentType() + "("+this.getLength()+" bytes)";
	}

}
