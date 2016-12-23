package net.latin.client.widget.uploader;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent.AddedFileHandler;
import gwt.material.design.addins.client.fileuploader.events.ErrorEvent;
import gwt.material.design.addins.client.fileuploader.events.ErrorEvent.ErrorHandler;
import gwt.material.design.addins.client.fileuploader.events.MaxFilesReachedEvent;
import gwt.material.design.addins.client.fileuploader.events.MaxFilesReachedEvent.MaxFilesReachedHandler;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent.SuccessHandler;
import gwt.material.design.addins.client.fileuploader.events.TotalUploadProgressEvent;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.events.DragOverEvent;
import gwt.material.design.client.events.DropEvent;
import gwt.material.design.client.events.DropEvent.DropHandler;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.jquery.client.api.Functions.Func;
import net.latin.client.rpc.GwtRespuestAsyncCallback;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.client.widget.fileviewer.GwtMaterialFileViewer;
import net.latin.client.widget.link.GwtExternalLink;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.uploader.base.MaterialFileUploader_New;
import net.latin.client.widget.uploader.rpc.GwtDownloadFileClientAsync;
import net.latin.client.widget.uploader.rpc.GwtViewFileClientAsync;

public class GwtFileUploader extends MaterialFileUploader_New implements LnwWidget {

	private static final String DEFAULT_TEXT = "Haz click en el botón o arrastra el archivo aquí";
	private static final String UPLOAD_URL=GwtController.instance.getBasePath() + "__fileUploader";
	public static final String DOWNLODER_SERVER_NAME = "__fileDownloader";
	private static final String DEFAULT_WIDTH = "500px";
	private static Long nextComponentId=1L;
	private Long nextId=1L;
	private String componentId="";
	private MaterialProgress progress;
	private MaterialLabel labelNombre;
	private GwtFileUploader cardUploader;
	private MaterialButton btnUpload;
	private MaterialButton btnRemove;
	private MaterialButton btnView;
	private MaterialButton btnDownload;
	private MaterialButton btnUploaded;
	private GwtViewFileHandler viewHandler;
	private GwtDownloadFileHandler downloadHandler;
	private GwtDownloadFileClientAsync downloadServer;
	private GwtViewFileClientAsync viewServer;
	private GwtUploadFile file;
	private GwtMensajesHandler msgHandler;
	private GwtMaterialFileViewer fileViewPopUp;
	private int DEFAULT_MAX_FILE_SIZE=5;
	private MaterialLabel labelProgreso;
	private MaterialButton btnCancelar; 
	private Boolean allowDownload;
	private Boolean allowView;
	private Boolean isUploading;
	
	public GwtFileUploader(GwtMensajesHandler msgHandler) {
		this.msgHandler = msgHandler;
		allowDownload=true;
		allowView=true;
		isUploading=false;
		setUrl(UPLOAD_URL);
		labelNombre = new MaterialLabel();
		labelNombre.setText(DEFAULT_TEXT);
		
		labelProgreso = new MaterialLabel();
		labelProgreso.setVisible(false);
		labelProgreso.setLayoutPosition(Position.ABSOLUTE);
		labelProgreso.setBottom(7);
		labelProgreso.setFontSize("11px");
		labelProgreso.setLeft(35);
		
		componentId="FileUploader_" + nextComponentId;
		nextComponentId++;
		
		MaterialCard card=new MaterialCard();
		MaterialCardContent content=new MaterialCardContent();
		content.setLayoutPosition(Position.RELATIVE);
		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(5);
		progress.setLeft(0);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		
		btnUpload = crearBoton(0,25,IconType.CLOUD_UPLOAD,ButtonSize.LARGE);
		btnUpload.setId("cardUpload");
		btnUpload.setTitle("Subir archivo");
		
		btnCancelar = crearBoton(0,25,IconType.CLEAR,ButtonSize.LARGE);
		btnCancelar.setBackgroundColor(Color.RED);
		btnCancelar.setTitle("Cancelar");
		btnCancelar.setVisible(false);
		btnCancelar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				isUploading=false;
				reset();
			}
		});
		
		btnUploaded = crearBoton(0,25,IconType.CHECK,ButtonSize.LARGE);
		btnUploaded.setBackgroundColor(Color.GREEN);
		btnUploaded.setVisible(false);
		
		
		btnRemove = crearBoton(10,25, IconType.DELETE,ButtonSize.MEDIUM);
		btnRemove.setBackgroundColor(Color.RED);
		btnRemove.setTitle("Eliminar archivo");
		btnRemove.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				accionEliminar();
			}
		});
		
		btnView = crearBoton(10,75,IconType.VISIBILITY,ButtonSize.MEDIUM);
		btnView.setTitle("Ver archivo");
		btnView.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (viewServer!=null){
					accionVer();
				}
				else{
					if (viewHandler!=null){
						viewHandler.onViewRequested(file);
					}
				}
			}
		});
		
		btnDownload = crearBoton(10,125,IconType.CLOUD_DOWNLOAD,ButtonSize.MEDIUM);
		btnDownload.setBackgroundColor(Color.GREEN);
		btnDownload.setTitle("Descargar archivo");
		btnDownload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (viewServer!=null){
					accionDescargar();
				}
				else{
					if (downloadHandler!=null){
						downloadHandler.onDownloadRequested(file);
					}
				}
			}
		});
		
		btnRemove.setVisible(false);
		btnView.setVisible(false);
		btnDownload.setVisible(false);
		
		card.add(content);
		content.add(labelNombre);
		content.add(progress);
		content.add(btnUpload);
		content.add(btnCancelar);
		content.add(btnUploaded);
		content.add(btnRemove);
		content.add(btnView);
		content.add(btnDownload);
		content.add(labelProgreso);
		this.add(card);
		this.setClickable("cardUpload");
		
		this.addTotalUploadProgressHandler(new TotalUploadProgressEvent.TotalUploadProgressHandler() {
			 public void onTotalUploadProgress(TotalUploadProgressEvent event) {
				 progress.setPercent(event.getProgress());
				 setTextoProgreso(event.getProgress());
			 }
		});
		this.addAddedFileHandler(new AddedFileHandler<GwtUploadFile>() {
			public void onAddedFile(AddedFileEvent<GwtUploadFile> event) {
				msgHandler.clearMessages();
				isUploading=true;
				labelNombre.setText(event.getTarget().getName());
				file=event.getTarget();
				file.setId(componentId + "_" + nextId);
				setFileId(file.getId());
				nextId++;
				labelProgreso.setVisible(true);
				progress.setVisible(true);
				
				MaterialAnimation buAnim = new MaterialAnimation();
				buAnim.transition(Transition.ROTATEOUT);
				buAnim.delayMillis(0);
				buAnim.animate(btnUpload, new Func(){
					public void call() {
						btnUpload.setVisible(false);
					}
					
				});
				btnCancelar.setOpacity(0);
				btnCancelar.setVisible(true);
				MaterialAnimation bcAnim = new MaterialAnimation();
				bcAnim.transition(Transition.ROTATEIN);
				bcAnim.delayMillis(0);
				bcAnim.animate(btnCancelar,new Func(){
					public void call() {
						btnCancelar.setOpacity(1);
					}
				});
			}
		});
		this.setPreview(false);
		this.setWidth(DEFAULT_WIDTH);
		this.setHeight("auto");
		cardUploader=this;
		addDropHandler(new DropHandler() {
			public void onDrop(DropEvent event) {
				resetWidget();
			}
		});
		addDragOverHandler(new DragOverEvent.DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				MaterialAnimation cuAnim = new MaterialAnimation();
				cuAnim.transition(Transition.RUBBERBAND);
				cuAnim.delayMillis(0);
				cuAnim.animate(cardUploader);
			}
		});
		this.addSuccessHandler(new SuccessHandler<GwtUploadFile>() {
			public void onSuccess(SuccessEvent<GwtUploadFile> event) {
				isUploading=false;
				MaterialAnimation pAnim = new MaterialAnimation();
				pAnim.transition(Transition.FADEOUT);
				pAnim.delayMillis(500);
				pAnim.animate(progress,new Func() {
					public void call() {
						progress.setVisible(false);
					}
				});
				MaterialAnimation bcAnim = new MaterialAnimation();
				bcAnim.transition(Transition.ROTATEOUT);
				bcAnim.delayMillis(0);
				bcAnim.animate(btnCancelar,new Func(){
					public void call() {
						btnCancelar.setVisible(false);
					}
					
				});
				btnUploaded.setOpacity(0);
				btnUploaded.setVisible(true);
				MaterialAnimation buAnim = new MaterialAnimation();
				buAnim.transition(Transition.ROTATEIN);
				buAnim.delayMillis(0);
				buAnim.animate(btnUploaded,new Func(){
					public void call() {
						btnUploaded.setOpacity(1);
						//LO HAGO DEFERRED PARA QUE LA NUEVA ANIMACION 
						//NO SE LLAME DENTRO DEL CALLBACK DE LA PRIMERA
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							public void execute() {
								showButtons();
							}
						});
					}
				});
				MaterialAnimation lpAnim = new MaterialAnimation();
				lpAnim.transition(Transition.FADEOUT);
				lpAnim.delayMillis(0);
				lpAnim.animate(labelProgreso,new Func(){
					public void call() {
						labelProgreso.setVisible(false);
					}
				});
			}
		});
		
		addMaxFilesReachHandler(new MaxFilesReachedHandler<GwtUploadFile>() {
			public void onMaxFilesReached(MaxFilesReachedEvent<GwtUploadFile> event) {
				msgHandler.addErrorMessage("La cantidad de archivos o el tamaño es incorrecto");
			}
		});
		addErrorHandler(new ErrorHandler<GwtUploadFile>() {
			public void onError(ErrorEvent<GwtUploadFile> event) {
				msgHandler.addErrorMessage(event.getResponse().getBody());
				isUploading=false;
				resetWidget();
			}
		});
		fileViewPopUp= new GwtMaterialFileViewer();
		setMaxFileSize(DEFAULT_MAX_FILE_SIZE);
	}
	
	protected void setTextoProgreso(double progreso) {
		String total=((int)(file.getFileSize() * 1024)) + "KBs";
		String actual=((int)(file.getFileSize()*progreso/100.0 * 1024)) + "KBs";
		labelProgreso.setText(actual + " / " + total);
	}

	@Override
	public void initDropzone() {
		super.initDropzone();
		setOptionValue("dictFallbackMessage","Tu navegador no soporta subir archivos arrastrando archivos.");
		setOptionValue("dictFallbackText","Por favor utiliza la modalidad antigua para subir archivos.");
		setOptionValue("dictFileTooBig","El archivo es muy grande ({{filesize}}MiB). Tamaño máximo:{{maxFilesize}}MiB.");
		setOptionValue("dictInvalidFileType","No puedes subir archivos de ese tipo.");
		setOptionValue("dictResponseError","El servidor respondió con el código {{statusCode}} .");
		setOptionValue("dictCancelUpload","Cancelar subida");
		setOptionValue("dictCancelUploadConfirmation","¿Estas seguro que deseas cancelar la subida?");
		setOptionValue("dictRemoveFile","Remover archivo");
		setOptionValue("dictMaxFilesExceeded","No puedes subir más archivos.");
	}

	protected void accionDescargar() {
		downloadServer.prepareForDownload(file.getId(), new GwtRespuestAsyncCallback<SimpleRespuestRPC>(msgHandler) {
			public void onOk(SimpleRespuestRPC result) {
				GwtExternalLink.openLocation(GwtController.instance.getBasePath() + DOWNLODER_SERVER_NAME);
			}
		});
	}


	protected void accionVer() {
		viewServer.prepareForView(file.getId(), new GwtRespuestAsyncCallback<SimpleRespuestRPC>(msgHandler) {
			public void onOk(SimpleRespuestRPC result) {
				fileViewPopUp.open();
			}
		});
	}


	private void showButtons(){
		MaterialAnimation buAnim = new MaterialAnimation();
		buAnim.transition(Transition.FADEOUT);
		buAnim.delayMillis(300);
		buAnim.animate(btnUploaded,new Func() {
			public void call() {
				btnUploaded.setVisible(false);
				btnRemove.setOpacity(0);
				btnView.setOpacity(0);
				btnDownload.setOpacity(0);
				btnRemove.setVisible(true);
				MaterialAnimation brAnim = new MaterialAnimation();
				brAnim.transition(Transition.FADEIN);
				brAnim.delayMillis(0);
				brAnim.animate(btnRemove,new Func() {
					public void call() {
						btnRemove.setOpacity(1);
					}
				});
				if (allowView){
					btnView.setVisible(true);
					MaterialAnimation bvAnim = new MaterialAnimation();
					bvAnim.transition(Transition.FADEIN);
					bvAnim.delayMillis(100);
					bvAnim.animate(btnView,new Func() {
						public void call() {
							btnView.setOpacity(1);
						}
					});
				}
				if (allowDownload){
					btnDownload.setVisible(true);
					MaterialAnimation bdAnim = new MaterialAnimation();
					bdAnim.transition(Transition.FADEIN);
					bdAnim.delayMillis(200);
					bdAnim.animate(btnDownload,new Func() {
						public void call() {
							btnDownload.setOpacity(1);
						}
					});
				}
			}
		});
	}
	
	protected void accionEliminar() {
		resetWidget();
	}

	private MaterialButton crearBoton(int top,int right, IconType iconType,ButtonSize size) {
		MaterialButton btn = new MaterialButton(ButtonType.FLOATING);
		btn.setSize(size);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setTop(top);
		btn.setRight(right);
		btn.setIconType(iconType);
		btn.setIconColor(Color.WHITE);
		btn.setBackgroundColor(Color.LIGHT_BLUE);
		return btn;
	}

	@Override
	public void resetWidget() {
		super.reset();
		file=null;
		labelNombre.setText(DEFAULT_TEXT);
		btnUpload.setVisible(true);
		btnRemove.setVisible(false);
		btnView.setVisible(false);
		btnDownload.setVisible(false);
		progress.setVisible(false);
		labelProgreso.setText("");
		labelProgreso.setVisible(false);
	}

	@Override
	public void setFocus() {

	}

	public void setViewHandler(GwtViewFileHandler viewHandler) {
		this.viewHandler = viewHandler;
	}

	public void setDownloadHandler(GwtDownloadFileHandler downloadHandler) {
		this.downloadHandler = downloadHandler;
	}


	public void setDownloadServer(GwtDownloadFileClientAsync downloadServer) {
		this.downloadServer = downloadServer;
	}


	public void setViewServer(GwtViewFileClientAsync viewServer) {
		this.viewServer = viewServer;
	}

	/**
	 * No utilizar este método, cada control acepta un solo archivo
	 */
	@Override
	@Deprecated
	public void setMaxFiles(int maxFiles) {
		super.setMaxFiles(1);
	}

	public Boolean getAllowDownload() {
		return allowDownload;
	}

	public void setAllowDownload(Boolean allowDownload) {
		this.allowDownload = allowDownload;
	}

	public Boolean getAllowView() {
		return allowView;
	}

	public void setAllowView(Boolean allowView) {
		this.allowView = allowView;
	}

	public GwtUploadFile getFile() {
		return file;
	}

	public void setFile(GwtUploadFile file) {
		this.file = file;
	}

	public Boolean isUploading() {
		return isUploading;
	}
	
}
