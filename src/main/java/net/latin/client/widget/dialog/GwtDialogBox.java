package net.latin.client.widget.dialog;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.window.MaterialWindow;

/**
 * Dialog Box de LNW.
 * Tiene eventos:
 * <code>onBeforeShow</code>
 * <code>onAfterShow</code>
 * <code>onHide</code>
 *
 *
 */
public class GwtDialogBox extends MaterialWindow implements ResizeHandler {

	private static final String CSS_BODY = "GwtDialogBoxBody";
	private VerticalPanel mainPanel;

	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBox() {
		this(true);
	}
	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBox(boolean modal) {
		super();
		
		this.mainPanel = new VerticalPanel();
		this.mainPanel.setHorizontalAlignment( VerticalPanel.ALIGN_CENTER );
		this.mainPanel.setVerticalAlignment( VerticalPanel.ALIGN_MIDDLE );
		this.mainPanel.setStyleName( CSS_BODY );
		this.mainPanel.setWidth( "100%" );
		
		//SACA EL BOTON MAXIMIZAR
		getToolbar().remove(2);
		
		getWindowContainer().getElement().getStyle().setProperty("width", "auto");
		getWindowContainer().getElement().getStyle().setProperty("left", "initial");
		getWindowContainer().getElement().getStyle().setProperty("right", "initial");
		Window.addResizeHandler(this);
		
		
		this.close();
	}

	protected void centrarHorizontal() {
		getWindowContainer().getElement().getStyle().setProperty("left", "initial");
		getWindowContainer().getElement().getStyle().setProperty("right", "initial");
		int modalWidth = getWindowContainer().getOffsetWidth();
		float clientWidth = Window.getClientWidth();
		int left=(int) ((clientWidth-modalWidth)/2);
		getWindowContainer().getElement().getStyle().setLeft(left,Unit.PX);
		getWindowContainer().getElement().getStyle().setRight(left,Unit.PX);
	}

	//CUANDO CAMBIA EL TAMAÑO DE LA VENTANA
	public void onResize(ResizeEvent event) {
		centrarHorizontal();
	}
	/**
	 * Add a widget to the Dialog
	 */
	public void add(Widget w) {
		this.mainPanel.add( w );
	}

	/**
	 * Remove a widget from the dialog
	 */
	public boolean remove(Widget w) {
		return this.mainPanel.remove( w );
	}

	/**
	 * Render the dialog and let it ready to be shown
	 */
	public GwtDialogBox render() {
		super.add( this.mainPanel );
		return this;
	}

	public void show() {
		this.onBeforeShow();
		if( !this.isOpen() ) {
			super.open();
			centrarHorizontal();
		}
		this.onAfterShow();
	}

	public void close() {
		this.onClose();
		if( isOpen() ) {
			super.close();
		}
	}

	/**
	 * Show or hide the Dialog depending on the current state of it
	 */
	public void showHide() {
		if( isOpen() ) {
			this.close();
		} else {
			this.show();
		}
	}

	/**
	 * Template method call before the Dialog become hidden
	 */
	protected void onClose() {
	}

	/**
	 * Template metod call before the Dialog become visible
	 */
	protected void onBeforeShow() {
	}

	/**
	 * Template metod call after the Dialog become visible
	 */
	protected void onAfterShow() {
	}

	public void setWidth(String width) {
		super.setWidth(width);
		this.mainPanel.setWidth(width);
	}

	public void setHeight(String height) {
		super.setHeight(height);
		this.mainPanel.setHeight(height);
	}

}






