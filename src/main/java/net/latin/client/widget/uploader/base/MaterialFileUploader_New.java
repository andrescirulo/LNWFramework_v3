/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.latin.client.widget.uploader.base;

import static gwt.material.design.jquery.client.api.JQuery.$;

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.MaterialAddins;
import gwt.material.design.addins.client.base.constants.AddinsCssName;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.addins.client.fileuploader.MaterialUploadPreview;
import gwt.material.design.addins.client.fileuploader.base.HasFileUpload;
import gwt.material.design.addins.client.fileuploader.base.UploadResponse;
import gwt.material.design.addins.client.fileuploader.constants.FileMethod;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent;
import gwt.material.design.addins.client.fileuploader.events.CanceledEvent;
import gwt.material.design.addins.client.fileuploader.events.CompleteEvent;
import gwt.material.design.addins.client.fileuploader.events.CurrentUploadProgressEvent;
import gwt.material.design.addins.client.fileuploader.events.ErrorEvent;
import gwt.material.design.addins.client.fileuploader.events.MaxFilesExceededEvent;
import gwt.material.design.addins.client.fileuploader.events.MaxFilesReachedEvent;
import gwt.material.design.addins.client.fileuploader.events.RemovedFileEvent;
import gwt.material.design.addins.client.fileuploader.events.SendingEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.TotalUploadProgressEvent;
import gwt.material.design.addins.client.fileuploader.events.UnauthorizedEvent;
import gwt.material.design.addins.client.fileuploader.js.Dropzone;
import gwt.material.design.addins.client.fileuploader.js.File;
import gwt.material.design.addins.client.fileuploader.js.JsFileUploaderOptions;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.events.DragEndEvent;
import gwt.material.design.client.events.DragEnterEvent;
import gwt.material.design.client.events.DragLeaveEvent;
import gwt.material.design.client.events.DragOverEvent;
import gwt.material.design.client.events.DragStartEvent;
import gwt.material.design.client.events.DropEvent;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.jscore.client.api.JsObject;
import net.latin.client.widget.uploader.GwtUploadFile;

//@formatter:off

/**
 * Custom file uploader with Dnd support with the help of dropzone.js. It has multiple
 * feature just like the GWT File Uploader core widget.
 * <p>
 * <h3>XML Namespace Declaration</h3>
 * <pre>
 * {@code
 * xmlns:ma='urn:import:gwt.material.design.addins.client'
 * }
 * </pre>
 * <p>
 * <h3>UiBinder Usage:</h3>
 * <pre>
 * {@code
 * <ma:fileuploader.MaterialFileUploader url="/file/upload"/>
 * }
 * </pre>
 *
 * @author kevzlou7979
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#fileuploader">File Uploader</a>
 */
//@formatter:on
public class MaterialFileUploader_New extends MaterialWidget implements HasFileUpload<GwtUploadFile> {

    static {
        if (MaterialAddins.isDebug()) {
            MaterialDesignBase.injectDebugJs(MaterialFileUploaderDebugClientBundle.INSTANCE.dropzoneJsDebug());
            MaterialDesignBase.injectCss(MaterialFileUploaderDebugClientBundle.INSTANCE.dropzoneCssDebug());
        } else {
            MaterialDesignBase.injectJs(MaterialFileUploaderClientBundle.INSTANCE.dropzoneJs());
            MaterialDesignBase.injectCss(MaterialFileUploaderClientBundle.INSTANCE.dropzoneCss());
        }
    }

    private boolean preview = true;
    private boolean initialize = false;
    private int totalFiles = 0;
    private String globalResponse = "";
    private Dropzone uploader;
    private JsFileUploaderOptions options;
    private MaterialUploadPreview uploadPreview = new MaterialUploadPreview();

    public MaterialFileUploader_New() {
        super(Document.get().createDivElement(), AddinsCssName.FILEUPLOADER);
        setId(AddinsCssName.ZDROP);
        add(uploadPreview);
        options = getDefaultOptions();
    }

    public MaterialFileUploader_New(String url, FileMethod method) {
        this();
        setUrl(url);
        setMethod(method);
    }

    public MaterialFileUploader_New(String url, FileMethod method, int maxFileSize, String acceptedFiles) {
        this(url, method);
        setMaxFiles(maxFileSize);
        setAcceptedFiles(acceptedFiles);
    }

    protected JsFileUploaderOptions getDefaultOptions() {
        JsFileUploaderOptions options = new JsFileUploaderOptions();
        options.clickable = "";
        options.autoQueue = true;
        options.maxFilesize = 20;
        options.maxFiles = 100;
        options.method = FileMethod.POST.getCssName();
        options.withCredentials = false;
        options.acceptedFiles = "";
        return options;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (!isInitialize()) {
            initDropzone();
            setInitialize(true);
        }
    }

    public void initDropzone() {
        String previews = DOM.createUniqueId();
        uploadPreview.getUploadCollection().setId(previews);
        if (options.clickable.isEmpty()) {
            String clickable = DOM.createUniqueId();
            if (getWidget(1) instanceof MaterialUploadLabel) {
                MaterialUploadLabel label = (MaterialUploadLabel) getWidget(1);
                label.getIcon().setId(clickable);
            } else {
                getWidget(1).getElement().setId(clickable);
            }
            setClickable(clickable);
        }

        if (!isPreview()) {
            uploadPreview.setDisplay(Display.NONE);
        }

        initDropzone(getElement(),
                uploadPreview.getUploadCollection().getItem().getElement(),
                previews,
                uploadPreview.getElement(),
                uploadPreview.getUploadHeader().getUploadedFiles().getElement());
    }

    /**
     * Intialize the dropzone component with element and form url to provide a
     * dnd feature for the file upload
     *
     * @param e
     */
    protected void initDropzone(Element e, Element template, String previews, Element uploadPreview, Element uploadedFiles ) {
        JQueryElement previewNode = $(template);
        previewNode.asElement().setId("");
        String previewTemplate = previewNode.parent().html();
        options.previewTemplate = previewTemplate;
        options.previewsContainer = "#" + previews;
        uploader = new Dropzone(e, options);

        uploader.on("drop", event -> {
            fireDropEvent();
            if (preview) {
                $(e).removeClass(CssName.ACTIVE);
            }
            return true;
        });

        uploader.on("dragstart", event -> {
            DragStartEvent.fire(this);
            return true;
        });

        uploader.on("dragend", event -> {
            DragEndEvent.fire(this);
            return true;
        });

        uploader.on("dragenter", event -> {
            DragEnterEvent.fire(this, null);
            if (preview) {
                $(e).addClass(CssName.ACTIVE);
            }
            return true;
        });

        uploader.on("dragover", event -> {
            DragOverEvent.fire(this);
            return true;
        });

        uploader.on("dragleave", event -> {
            DragLeaveEvent.fire(this, null);
            if (preview) {
                $(e).removeClass(CssName.ACTIVE);
            }
            return true;
        });

        uploader.on("addedfile", file -> {
            AddedFileEvent.fire(this, convertUploadFile(file));
            totalFiles++;

            if (isPreview()) {
                $(uploadPreview).css("visibility", "visible");
                $(uploadedFiles).html("Uploaded files " + totalFiles);
                getUploadPreview().getUploadHeader().getProgress().setPercent(0);
            }
        });

        uploader.on("removedfile", file -> {
            RemovedFileEvent.fire(this, convertUploadFile(file));
            totalFiles -= 1;
            $(uploadedFiles).html("Uploaded files " + totalFiles);
        });

        uploader.on("error", (file, response) -> {
            String code = "200";
            if (file.xhr != null) {
                code = file.xhr.status;
            }

            if (response.indexOf("401") >= 0) {
                response = "Unautharized. Probably Your's session expired. Log in and try again.";
                globalResponse = response;
                UnauthorizedEvent.fire(this, convertUploadFile(file), new UploadResponse(file.xhr.status, file.xhr.statusText, response));
            }

            if (response.indexOf("404") >= 0) {
                response = "There's a problem uploading your file.";
                globalResponse = response;
            }

            if (response.indexOf("500") >= 0) {
                response = "There's a problem uploading your file.";
                globalResponse = response;
            }

            $(file.previewElement).find("#error-message").html(response);
            if (file.xhr!=null){
            	ErrorEvent.fire(this, convertUploadFile(file), new UploadResponse(file.xhr.status, file.xhr.statusText, response));
        	}
	        else{
	        	ErrorEvent.fire(this, convertUploadFile(file), new UploadResponse(null, null, response));
	        }
        });

        uploader.on("totaluploadprogress", (progress, file, response) -> {
            TotalUploadProgressEvent.fire(this, progress);
            if (isPreview()) {
                getUploadPreview().getUploadHeader().getProgress().setPercent(progress);
            }
        });

        uploader.on("uploadprogress", (progress, file, response) -> {
            CurrentUploadProgressEvent.fire(this, progress);
            if ($this != null) {
                $this.find(".progress .determinate").css("width", progress + "%");
            }
        });

        uploader.on("sending", file -> {
            SendingEvent.fire(this, convertUploadFile(file), new UploadResponse(file.xhr.status, file.xhr.statusText));
        });

        uploader.on("success", (file, response) -> {
            globalResponse = response;
            SuccessEvent.fire(this, convertUploadFile(file), new UploadResponse(file.xhr.status, file.xhr.statusText, response));
        });

        uploader.on("complete", file -> {
            if (file.xhr!=null){
            	CompleteEvent.fire(this, convertUploadFile(file), new UploadResponse(file.xhr.status, file.xhr.statusText, globalResponse));
        	}
	        else{
	        	CompleteEvent.fire(this, convertUploadFile(file), new UploadResponse(null, null, globalResponse));
	        }
        });

        uploader.on("canceled", file -> {
            CanceledEvent.fire(this, convertUploadFile(file));
        });

        uploader.on("maxfilesreached", file -> {
            MaxFilesReachedEvent.fire(this, convertUploadFile(file));
        });

        uploader.on("maxfilesexceeded", file -> {
            MaterialToast.fireToast("You have reached the maximum files to be uploaded.");
            MaxFilesExceededEvent.fire(this, convertUploadFile(file));
        });
    }

    /**
     * Converts a Native File Object to Upload File object
     */
    protected GwtUploadFile convertUploadFile(File file) {
        Date lastModifiedDate = new Date();
        // Avoid parsing error on last modified date
        if (file.lastModifiedDate != null && !file.lastModifiedDate.isEmpty()) {
            lastModifiedDate = new Date(file.lastModifiedDate);
        }
        return new GwtUploadFile(file.name, lastModifiedDate, Double.parseDouble(file.size), file.type);
    }


    /**
     * Get the form url.
     */
    public String getUrl() {
        return options.url;
    }

    /**
     * Set the form url e.g /file/post.
     */
    public void setUrl(String url) {
        options.url = url;
    }

    /**
     * Get the maximum file size value of the uploader.
     */
    public int getMaxFileSize() {
        return options.maxFilesize;
    }

    /**
     * Set the maximum file size of the uploader, default 20(MB).
     */
    public void setMaxFileSize(int maxFileSize) {
        options.maxFilesize = maxFileSize;
    }

    /**
     * Check whether it's auto queue or not.
     */
    public boolean isAutoQueue() {
        return options.autoQueue;
    }

    /**
     * Set the auto queue boolean value.
     */
    public void setAutoQueue(boolean autoQueue) {
        options.autoQueue = autoQueue;
    }

    /**
     * Get the method param of file uploader.
     */
    public FileMethod getMethod() {
        return FileMethod.fromStyleName(options.method);
    }

    /**
     * Set the method param of file upload (POST or PUT), default POST.
     */
    public void setMethod(FileMethod method) {
        options.method = method.getCssName();
    }

    /**
     * Get the max number of files.
     */
    public int getMaxFiles() {
        return options.maxFiles;
    }

    /**
     * Set the max number of files.
     * Default 100 but if you want to accept only one file just set the max file to 1.
     * If the number of files you upload exceeds, the event maxfilesexceeded will be called.
     */
    public void setMaxFiles(int maxFiles) {
        options.maxFiles = maxFiles;
    }

    /**
     * Check whether it's withCredentials or not.
     */
    public boolean isWithCredentials() {
        return options.withCredentials;
    }

    /**
     * Set the withCredentials boolean value.
     */
    public void setWithCredentials(boolean withCredentials) {
        options.withCredentials = withCredentials;
    }

    /**
     * Get the accepted file string.
     */
    public String getAcceptedFiles() {
        return options.acceptedFiles;
    }

    /**
     * Set the default implementation of accept checks the file's mime type or extension against this list.
     * This is a comma separated list of mime types or file extensions. Eg.: image/*,application/pdf,.psd.
     */
    public void setAcceptedFiles(String acceptedFiles) {
        options.acceptedFiles = acceptedFiles;
    }

    public void fireDropEvent() {
        DropEvent.fire(this, null);
    }

    @Override
    public HandlerRegistration addAddedFileHandler(final AddedFileEvent.AddedFileHandler<GwtUploadFile> handler) {
        return addHandler(new AddedFileEvent.AddedFileHandler<GwtUploadFile>() {
            @Override
            public void onAddedFile(AddedFileEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onAddedFile(event);
                }
            }
        }, AddedFileEvent.getType());
    }

    @Override
    public HandlerRegistration addRemovedFileHandler(final RemovedFileEvent.RemovedFileHandler<GwtUploadFile> handler) {
        return addHandler(new RemovedFileEvent.RemovedFileHandler<GwtUploadFile>() {
            @Override
            public void onRemovedFile(RemovedFileEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onRemovedFile(event);
                }
            }
        }, RemovedFileEvent.getType());
    }

    @Override
    public HandlerRegistration addErrorHandler(final ErrorEvent.ErrorHandler<GwtUploadFile> handler) {
        return addHandler(new ErrorEvent.ErrorHandler<GwtUploadFile>() {
            @Override
            public void onError(ErrorEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onError(event);
                }
            }
        }, ErrorEvent.getType());
    }

    @Override
    public HandlerRegistration addUnauthorizedHandler(final UnauthorizedEvent.UnauthorizedHandler<GwtUploadFile> handler) {
        return addHandler(new UnauthorizedEvent.UnauthorizedHandler<GwtUploadFile>() {
            @Override
            public void onUnauthorized(UnauthorizedEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onUnauthorized(event);
                }
            }
        }, UnauthorizedEvent.getType());
    }

    @Override
    public HandlerRegistration addTotalUploadProgressHandler(final TotalUploadProgressEvent.TotalUploadProgressHandler handler) {
        return addHandler(event -> {
            if (isEnabled()) {
                handler.onTotalUploadProgress(event);
            }
        }, TotalUploadProgressEvent.TYPE);
    }

    @Override
    public HandlerRegistration addCurrentUploadProgressHandler(CurrentUploadProgressEvent.CurrentUploadProgressHandler handler) {
        return addHandler(event -> {
            if (isEnabled()) {
                handler.onCurrentUploadProgress(event);
            }
        }, CurrentUploadProgressEvent.TYPE);
    }

    @Override
    public HandlerRegistration addSendingHandler(final SendingEvent.SendingHandler<GwtUploadFile> handler) {
        return addHandler(new SendingEvent.SendingHandler<GwtUploadFile>() {
            @Override
            public void onSending(SendingEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onSending(event);
                }
            }
        }, SendingEvent.getType());
    }

    @Override
    public HandlerRegistration addSuccessHandler(final SuccessEvent.SuccessHandler<GwtUploadFile> handler) {
        return addHandler(new SuccessEvent.SuccessHandler<GwtUploadFile>() {
            @Override
            public void onSuccess(SuccessEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onSuccess(event);
                }
            }
        }, SuccessEvent.getType());
    }

    @Override
    public HandlerRegistration addCompleteHandler(final CompleteEvent.CompleteHandler<GwtUploadFile> handler) {
        return addHandler(new CompleteEvent.CompleteHandler<GwtUploadFile>() {
            @Override
            public void onComplete(CompleteEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onComplete(event);
                }
            }
        }, CompleteEvent.getType());
    }

    @Override
    public HandlerRegistration addCancelHandler(final CanceledEvent.CanceledHandler<GwtUploadFile> handler) {
        return addHandler(new CanceledEvent.CanceledHandler<GwtUploadFile>() {
            @Override
            public void onCanceled(CanceledEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onCanceled(event);
                }
            }
        }, CanceledEvent.getType());
    }

    @Override
    public HandlerRegistration addMaxFilesReachHandler(final MaxFilesReachedEvent.MaxFilesReachedHandler<GwtUploadFile> handler) {
        return addHandler(new MaxFilesReachedEvent.MaxFilesReachedHandler<GwtUploadFile>() {
            @Override
            public void onMaxFilesReached(MaxFilesReachedEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onMaxFilesReached(event);
                }
            }
        }, MaxFilesReachedEvent.getType());
    }

    @Override
    public HandlerRegistration addMaxFilesExceededHandler(final MaxFilesExceededEvent.MaxFilesExceededHandler<GwtUploadFile> handler) {
        return addHandler(new MaxFilesExceededEvent.MaxFilesExceededHandler<GwtUploadFile>() {
            @Override
            public void onMaxFilesExceeded(MaxFilesExceededEvent<GwtUploadFile> event) {
                if (isEnabled()) {
                    handler.onMaxFilesExceeded(event);
                }
            }
        }, MaxFilesExceededEvent.getType());
    }

    public String getClickable() {
        return options.clickable.length()==0?options.clickable:options.clickable.substring(1);
    }

    public void setClickable(String clickable) {
        options.clickable = "#"+clickable;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    /**
     * Check whether the component has been initialized.
     */
    public boolean isInitialize() {
        return initialize;
    }

    /**
     * Set the initialization of the component.
     */
    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public void reset() {
        uploader.removeAllFiles();
    }

    public MaterialUploadPreview getUploadPreview() {
        return uploadPreview;
    }

    protected void setFileId(String fileId){
    	setFileId(fileId, uploader);
    }
    protected native void setFileId(String fileId,JsObject uploader)/*-{
		return uploader.options.headers={"File-Id": fileId};
	}-*/;
    
    protected void setOptionValue(String option,String value){
    	setOptionValue(option,value, uploader);
    }
    protected native void setOptionValue(String option,String value,JsObject uploader)/*-{
		return uploader.options[option]=value;
	}-*/;
}