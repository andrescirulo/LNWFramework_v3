package net.latin.client.widget.uploader;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent.AddedFileHandler;
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
import gwt.material.design.client.ui.animate.MaterialAnimator;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.jquery.client.api.Functions.Func;
import net.latin.client.rpc.GwtRespuestAsyncCallback;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.client.widget.fileviewer.GwtMaterialFileViewer;
import net.latin.client.widget.link.GwtExternalLink;
import net.latin.client.widget.msg.GwtMensajesListener;
import net.latin.client.widget.uploader.base.MaterialFileUploader_New;
import net.latin.client.widget.uploader.rpc.GwtDownloadFileClientAsync;
import net.latin.client.widget.uploader.rpc.GwtViewFileClientAsync;

public class GwtFileUploader extends MaterialFileUploader_New implements LnwWidget {

	private static final String DEFAULT_TEXT = "Arrastra los archivos aquí";
	private static final String UPLOAD_URL=GwtController.instance.getBasePath() + "__fileUploader";
	public static final String DOWNLODER_SERVER_NAME = "__fileDownloader";
	private static final String DEFAULT_WIDTH = "500px";
	private static Long nextComponentId=1L;
	private Long nextId=1L;
	private String componentId="";
	private MaterialProgress progress;
	private MaterialLabel label;
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
	private GwtMensajesListener msgHandler;
	private GwtMaterialFileViewer fileViewPopUp;
	
	public GwtFileUploader() {
		setUrl(UPLOAD_URL);
		label = new MaterialLabel();
		label.setText(DEFAULT_TEXT);
		componentId="FileUploader_" + nextComponentId;
		nextComponentId++;
		MaterialCard card=new MaterialCard();
		MaterialCardContent content=new MaterialCardContent();
		content.setLayoutPosition(Position.RELATIVE);
		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(0);
		progress.setLeft(0);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		
		btnUpload = crearBoton(0,25,IconType.CLOUD_UPLOAD,ButtonSize.LARGE);
		btnUpload.setId("cardUpload");
		btnUpload.setTitle("Subir archivo");
		
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
		content.add(label);
		content.add(progress);
		content.add(btnUpload);
		content.add(btnUploaded);
		content.add(btnRemove);
		content.add(btnView);
		content.add(btnDownload);
		this.add(card);
		this.setClickable("cardUpload");
		
		this.addTotalUploadProgressHandler(new TotalUploadProgressEvent.TotalUploadProgressHandler() {
			 public void onTotalUploadProgress(TotalUploadProgressEvent event) {
				 progress.setPercent(event.getProgress());
			 }
		});
		this.addAddedFileHandler(new AddedFileHandler<GwtUploadFile>() {
			public void onAddedFile(AddedFileEvent<GwtUploadFile> event) {
				label.setText(event.getTarget().getName());
				file=event.getTarget();
				file.setId(componentId + "_" + nextId);
				setFileId(file.getId());
				nextId++;
				progress.setVisible(true);
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
				MaterialAnimator.animate(Transition.RUBBERBAND, cardUploader, 0);
			}
		});
		this.addSuccessHandler(new SuccessHandler<GwtUploadFile>() {
			public void onSuccess(SuccessEvent<GwtUploadFile> event) {
				MaterialAnimator.animate(Transition.FADEOUT,progress,500,new Func() {
					public void call() {
						progress.setVisible(false);
					}
				});
				MaterialAnimator.animate(Transition.ROTATEOUT, btnUpload, 0,new Func(){
					public void call() {
						btnUpload.setVisible(false);
					}
					
				});
				btnUploaded.setOpacity(0);
				btnUploaded.setVisible(true);
				MaterialAnimator.animate(Transition.ROTATEIN, btnUploaded, 0,new Func(){
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
			}
		});
		
		fileViewPopUp= new GwtMaterialFileViewer();
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
		MaterialAnimator.animate(Transition.FADEOUT,btnUploaded,300,new Func() {
			public void call() {
				btnUploaded.setVisible(false);
				btnRemove.setOpacity(0);
				btnView.setOpacity(0);
				btnDownload.setOpacity(0);
				btnRemove.setVisible(true);
				btnView.setVisible(true);
				btnDownload.setVisible(true);
				MaterialAnimator.animate(Transition.FADEIN,btnRemove,0,new Func() {
					public void call() {
						btnRemove.setOpacity(1);
					}
				});
				MaterialAnimator.animate(Transition.FADEIN,btnView,100,new Func() {
					public void call() {
						btnView.setOpacity(1);
					}
				});
				MaterialAnimator.animate(Transition.FADEIN,btnDownload,200,new Func() {
					public void call() {
						btnDownload.setOpacity(1);
					}
				});
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
		label.setText(DEFAULT_TEXT);
		btnUpload.setVisible(true);
		btnRemove.setVisible(false);
		btnView.setVisible(false);
		btnDownload.setVisible(false);
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


	public void setDownloadServer(GwtDownloadFileClientAsync downloadServer,GwtMensajesListener msgHandler) {
		this.downloadServer = downloadServer;
		this.msgHandler = msgHandler;
	}


	public void setViewServer(GwtViewFileClientAsync viewServer,GwtMensajesListener msgHandler) {
		this.viewServer = viewServer;
		this.msgHandler = msgHandler;
	}

	/**
	 * No utilizar este método, cada control acepta un solo archivo
	 */
	@Override
	@Deprecated
	public void setMaxFiles(int maxFiles) {
		super.setMaxFiles(1);
	}
	
}
