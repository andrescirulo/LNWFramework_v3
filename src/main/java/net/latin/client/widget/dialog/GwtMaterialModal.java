package net.latin.client.widget.dialog;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.window.MaterialWindow;

public class GwtMaterialModal extends MaterialWindow implements ResizeHandler{

	public GwtMaterialModal() {
//		this.getElement().getStyle().setProperty("width", "auto");
//		this.getElement().getStyle().setProperty("left", "initial");
//		this.getElement().getStyle().setProperty("rigth", "initial");
//		addOpenHandler(new OpenHandler<MaterialModal>() {
//			public void onOpen(OpenEvent<MaterialModal> event) {
//				centrarHorizontal();
//			}
//		});
//		Window.addResizeHandler(this);
	}

	protected void centrarHorizontal() {
		getElement().getStyle().setProperty("left", "initial");
		getElement().getStyle().setProperty("rigth", "initial");
		int modalWidth = getOffsetWidth();
		float clientWidth = Window.getClientWidth();
		int left=(int) ((clientWidth-modalWidth)/2);
		getElement().getStyle().setLeft(left,Unit.PX);
		getElement().getStyle().setRight(left,Unit.PX);
	}

	//CUANDO CAMBIA EL TAMAÑO DE LA VENTANA
	public void onResize(ResizeEvent event) {
		centrarHorizontal();
	}
	
}
