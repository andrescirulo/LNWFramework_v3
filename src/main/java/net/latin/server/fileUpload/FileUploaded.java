package net.latin.server.fileUpload;

import org.apache.commons.fileupload.FileItem;

/**
 * 
 * @author pjn
 *
 */
public class FileUploaded {
	
	private String id;
	private String componentKey;
	private String extraParameter;
	private FileItem file;
	
	public String getId() {
		return id;
	}

	public String getComponentKey() {
		return componentKey;
	}

	public FileItem getFile() {
		return file;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setComponentKey(String componentKey) {
		this.componentKey = componentKey;
	}

	public void setFile(FileItem file) {
		this.file = file;
	}

	public String getExtraParameter() {
		return extraParameter;
	}

	public void setExtraParameter(String extraParameter) {
		this.extraParameter = extraParameter;
	}

}
