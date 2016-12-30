package net.latin.server.fileUpload;

import org.apache.commons.io.FilenameUtils;

import net.latin.server.utils.fileTypes.BaseFile;

public class FileToShowOnClientImpl extends BaseFile {

	private static final long serialVersionUID = -880395092477077120L;
	private String contentType;

	public FileToShowOnClientImpl(String name, byte[] content) {
		super(name, content);
		setContentType(ContentTypes.getContentType(FilenameUtils.getExtension(name)));
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
