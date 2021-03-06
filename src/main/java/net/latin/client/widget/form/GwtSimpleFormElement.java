package net.latin.client.widget.form;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.panels.GwtHorizontalPanel;

public class GwtSimpleFormElement implements GwtFormElement {

	/**
	 * ID del Elemento
	 * Es el texto de la descripcion
	 * Debe ser único
	 */
	protected String id;

	protected String footer;
	
	/**
	 * Widget del elemento
	 */
	protected Widget widget;

	/**
	 * HorizontalPanel que envuelve a todo el elemento
	 */
	protected GwtHorizontalPanel elementPanel;

	public void buildElement(GwtForm form) {
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth( "100%" );
		vPanel.add(widget);
		
		if (footer!=null){
			GwtFooterLabel footerLabel=new GwtFooterLabel(footer);
			vPanel.add(footerLabel);
		}
		
		elementPanel = new GwtHorizontalPanel();
		elementPanel.setWidth( "100%" );
		elementPanel.add( vPanel );
	}


	public String getElementId() {
		return id;
	}


	public GwtHorizontalPanel getElementPanel() {
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


	public String getFooter() {
		return footer;
	}


	public void setFooter(String footer) {
		this.footer = footer;
	}

}
