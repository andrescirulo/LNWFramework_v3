package net.latin.client.widget.uploader;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
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
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.animate.MaterialAnimator;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.jquery.client.api.Functions.Func;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;

public class GwtFileUploader extends MaterialFileUploader implements LnwWidget {

	private static final String DEFAULT_TEXT = "Arrastra los archivos aquí";
	private static final String UPLOAD_URL=GwtController.instance.getBasePath() + "__fileUploader";
	private static final String DEFAULT_WIDTH = "500px";
	private static Long nextComponentId=1L;
	private Long nextId=1L;
	private Long componentId=0L;
	private MaterialProgress progress;
	private MaterialLabel label;
	private GwtFileUploader cardUploader;
	private MaterialButton btnUpload;
	private MaterialButton btnRemove;
	private MaterialButton btnView;
	private MaterialButton btnDownload;
	
	public GwtFileUploader() {
		setUrl(UPLOAD_URL);
		
		label = new MaterialLabel();
		label.setText(DEFAULT_TEXT);
		componentId=nextComponentId;
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
		
		btnDownload = crearBoton(10,125,IconType.CLOUD_DOWNLOAD,ButtonSize.MEDIUM);
		btnDownload.setBackgroundColor(Color.GREEN);
		btnDownload.setTitle("Descargar archivo");
		
		btnRemove.setVisible(false);
		btnView.setVisible(false);
		btnDownload.setVisible(false);
		
		card.add(content);
		content.add(label);
		content.add(progress);
		content.add(btnUpload);
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
		this.addAddedFileHandler(new AddedFileHandler<UploadFile>() {
			public void onAddedFile(AddedFileEvent<UploadFile> event) {
				label.setText(event.getTarget().getName());
				progress.setVisible(true);
			}
		});
		this.setPreview(false);
		this.setWidth(DEFAULT_WIDTH);
		this.setHeight("auto");
		cardUploader=this;
		addDragOverHandler(new DragOverEvent.DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				MaterialAnimator.animate(Transition.RUBBERBAND, cardUploader, 0);
			}
		});
		addSuccessHandler(new SuccessHandler<UploadFile>() {
			public void onSuccess(SuccessEvent<UploadFile> event) {
				MaterialAnimator.animate(Transition.FADEOUT,progress,500,new Func() {
					public void call() {
						progress.setVisible(false);
					}
				});
				MaterialAnimator.animate(Transition.FADEOUT,btnUpload,0,new Func() {
					public void call() {
						btnUpload.setVisible(false);
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
		label.setText(DEFAULT_TEXT);
		btnUpload.setVisible(true);
		btnRemove.setVisible(false);
		btnView.setVisible(false);
		btnDownload.setVisible(false);
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
