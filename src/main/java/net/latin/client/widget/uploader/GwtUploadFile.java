package net.latin.client.widget.uploader;

import java.util.Date;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;

public class GwtUploadFile extends UploadFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5875068400632275366L;
	private String id;

	public GwtUploadFile(String name, Date lastModified, Double fileSize, String type) {
		setName(name);
		setLastModified(lastModified);
		setFileSize(fileSize==null?-1:fileSize);
		setType(type);
    }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
