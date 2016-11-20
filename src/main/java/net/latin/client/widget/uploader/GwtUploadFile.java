package net.latin.client.widget.uploader;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;

public class GwtUploadFile extends UploadFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5875068400632275366L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
