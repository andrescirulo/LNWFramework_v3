package net.latin.client.widget.uploader;

import com.google.gwt.core.client.JavaScriptObject;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent.AddedFileHandler;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;

public class GwtFileUploader extends MaterialFileUploader implements LnwWidget {

	private static final String UPLOAD_URL=GwtController.instance.getBasePath() + "__fileUploader";
	private static Long nextComponentId=1L;
	private Long nextId=1L;
	private Long componentId=0L;
	
	public GwtFileUploader() {
		setUrl(UPLOAD_URL);
//		addUploadRequestHandler(this);
//		addUploadBeforeHandler(this);
//		setTarget(UPLOAD_URL);
//		setI18n(getLocale());
		MaterialUploadLabel label = new MaterialUploadLabel();
		label.setTitle("Arrastra los archivos aquí");
		label.setDescription("Alguna descripción");
		add(label);
		componentId=nextComponentId;
		nextComponentId++;
		
	}
	
	private native JavaScriptObject getLocale()/*-{
		var i18n=new Object(); 
		i18n.dropFiles= {
		    one: 'Suelta el archivo aqui...',
		    many: 'Suelta los archivos aqui...'
		};
		i18n.addFiles= {
		    one: 'Selecciona el archivo',
		    many: 'Agregar archivos'
		};
		i18n.cancel= 'Cancelar';
		i18n.error= {
		    tooManyFiles: 'Demasiados archivos.',
		    fileIsTooBig: 'El archivo es muy grande.',
		    incorrectFileType: 'El tipo de archivo es incorrecto.'
		};
		i18n.uploading= {
		    status: {
		      connecting: 'Conectando...',
		      stalled: 'Frenado.',
		      processing: 'Procesando archivo...'
		    },
		    remainingTime: {
		      prefix: 'Tiempo pendiente: ',
		      unknown: 'Tiempo pendiente desconocido'
		    },
		    error: {
		      serverUnavailable: 'Servidor no disponible',
		      unexpectedServerError: 'Error de servidor inesperado',
		      forbidden: 'No permitido'
		    }
		  };
		  
		 i18n.units={
		    size: ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
		    sizeBase : 1024
		 };
		 return i18n;
	}-*/;
	
	@Override
	public void resetWidget() {
		super.reset();
	}

	@Override
	public void setFocus() {

	}

//	@Override
//	public void onUploadRequest(UploadRequestEvent event) {
//		JavaScriptObject xhr=event.getXhr();
//		FileToUpload file = event.getFile().cast();
//		setFileId(file.getFileId(),xhr);
//	}
//
//	@Override
//	public void onUploadBefore(UploadBeforeEvent event) {
//		FileToUpload file = event.getFile().cast();
//		file.setFileId("_#FILE_" + componentId + "_" + nextId);
//		nextId++;
//	}

	
}
