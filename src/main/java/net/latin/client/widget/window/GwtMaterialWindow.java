package net.latin.client.widget.window;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.window.MaterialWindow;

public class GwtMaterialWindow extends MaterialWindow implements ResizeHandler{

	
	public GwtMaterialWindow() {
		Window.addResizeHandler(this);
	}
	
	protected void centrarHorizontal() {
		getElement().getStyle().setProperty("left", "initial");
		getElement().getStyle().setProperty("right", "initial");
		int modalWidth = getOffsetWidth();
		float clientWidth = Window.getClientWidth();
		int left=(int) ((clientWidth-modalWidth)/2);
		getElement().getStyle().setLeft(left,Unit.PX);
		getElement().getStyle().setRight(left,Unit.PX);
	}

	//CUANDO CAMBIA EL TAMAÑO DE LA VENTANA
	public void onResize(ResizeEvent event) {
		centrarHorizontal();
		onWindowResize();
	}
	
	protected void onWindowResize() {
	}

	/**
	 * SACA EL BOTON MAXIMIZAR
	 */
	public void setNotMaximize(){
		getToolbar().remove(2);
	}
}
