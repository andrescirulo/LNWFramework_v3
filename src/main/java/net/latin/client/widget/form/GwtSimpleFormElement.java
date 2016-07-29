package net.latin.client.widget.form;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.base.LnwWidget;

public class GwtSimpleFormElement implements GwtFormElement {

	/**
	 * ID del Elemento
	 * Es el texto de la descripcion
	 * Debe ser único
	 */
	protected String id;

	/**
	 * Widget del elemento
	 */
	protected Widget widget;

	/**
	 * HorizontalPanel que envuelve a todo el elemento
	 */
	protected HorizontalPanel elementPanel;

	public void buildElement(GwtForm form) {
		elementPanel = new HorizontalPanel();
//		elementPanel.setHorizontalAlignment( HorizontalPanel.ALIGN_CENTER );
		elementPanel.setWidth( "100%" );
		elementPanel.add( widget );
	}


	public String getElementId() {
		return id;
	}


	public HorizontalPanel getElementPanel() {
		return elementPanel;
	}


	public void resetWidget() {
		if(widget instanceof LnwWidget) {
			((LnwWidget)widget).resetWidget();
		}

	}

	public void setFocus() {
		if(widget instanceof LnwWidget) {
			((LnwWidget)widget).setFocus();
		}
	}

	/**
	 * @return the widget
	 */
	public Widget getWidget() {
		return widget;
	}

	/**
	 * @param widget the widget to set
	 */
	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public void setElementId(String id) {
		this.id = id;
	}

	public boolean isVisible() {
		return elementPanel.isVisible();
	}

	public void setVisible(boolean flag) {
		elementPanel.setVisible(flag);
	}

}
