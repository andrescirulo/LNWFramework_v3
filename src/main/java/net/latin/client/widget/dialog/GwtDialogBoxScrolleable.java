package net.latin.client.widget.dialog;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;

/**
 * Igual al GwtDialogBox, pero con barra de Scroll.
 * En caso de no querer un tamaño fijo, permite setearle un tamaño maximo, y que se ajuste solo en caso de ser más chico
 *
 */
public class GwtDialogBoxScrolleable extends MaterialModal {

	protected static final String CSS_BODY = "GwtDialogBoxBody";
	protected MaterialNavBar toolbar;
	protected VerticalPanel mainPanel;
	protected boolean alreadyVisible;
//	protected boolean modal;
	protected ScrollPanel panelScrolleable;
	protected int maxHeightPx = 500;
	protected int maxWidthPx = 800;
	protected boolean staticHeight = false;
	protected boolean staticWidth = false;

	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBoxScrolleable() {
		this(true);
	}
	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBoxScrolleable(boolean modal) {
		super();
		this.alreadyVisible = false;
		this.mainPanel = new VerticalPanel();
		this.mainPanel.setHorizontalAlignment( VerticalPanel.ALIGN_CENTER );
		this.mainPanel.setVerticalAlignment( VerticalPanel.ALIGN_MIDDLE );
		this.mainPanel.setStyleName( CSS_BODY );
		this.mainPanel.setWidth( "100%" );
		this.panelScrolleable = getScrollablePanel();
//		this.panelScrolleable = new ScrollPanel();
		this.panelScrolleable.add(this.mainPanel);

		this.getElement().getStyle().setProperty( "zIndex", "11");

		this.close();
	}

	protected ScrollPanel getScrollablePanel() {
		return new ScrollPanel();
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
	public GwtDialogBoxScrolleable render() {
		this.add( this.panelScrolleable );
		return this;
	}

	public void show() {
		this.onBeforeShow();
		if( !this.alreadyVisible ) {
			super.open();
			this.alreadyVisible = true;
		}
		this.onAfterShow();
	}

	public void close() {
		this.onClose();
		if( this.alreadyVisible ) {
			super.close();
			this.alreadyVisible = false;
		}
	}

	/**
	 * Show or hide the Dialog depending on the current state of it
	 */
	public void showHide() {
		if( this.alreadyVisible ) {
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
		//si no le setearon un size estatico, se ajusta al tamaño de lo que ocupa (se achica en caso de ser mas chico que el maximo para que no quede feo a la vista)

		if(!this.staticHeight){
			int heightActual = this.panelScrolleable.getWidget().getOffsetHeight();

			if(heightActual > maxHeightPx){
				this.panelScrolleable.setHeight(maxHeightPx + "px");
			} else {
				this.panelScrolleable.setHeight(heightActual + "px");
			}
		}

		if(!this.staticWidth){
			int widthActual = this.panelScrolleable.getWidget().getOffsetWidth();

			if(widthActual > maxWidthPx){
				this.panelScrolleable.setWidth(maxWidthPx + "px");
			} else {
				this.panelScrolleable.setWidth(widthActual + "px");
			}
		}

	}

	public void setWidth(String width) {
		super.setWidth(width);
		this.panelScrolleable.setWidth(width);
		this.staticWidth = true;
	}

	public void setHeight(String height) {
		super.setHeight(height);
		this.panelScrolleable.setHeight(height);
		this.staticHeight = true;
	}

	public void setMaxWidth(int width) {
		this.maxWidthPx = width;
	}

	public void setMaxHeight(int height) {
		this.maxHeightPx = height;
	}
	
	public void setTitleText(String titleText){
		toolbar.add(new Label(titleText));
	}
}
