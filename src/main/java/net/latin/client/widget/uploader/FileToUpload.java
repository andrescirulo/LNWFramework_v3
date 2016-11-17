package net.latin.client.widget.uploader;

import com.google.gwt.core.client.JavaScriptObject;

public class FileToUpload extends JavaScriptObject{
	protected FileToUpload(){}

    public final native void setFileId(String fileId)/*-{
        return this.fileId=fileId;
    }-*/; 

    public final native String getFileId()/*-{
        return this.fileId;
    }-*/;
}