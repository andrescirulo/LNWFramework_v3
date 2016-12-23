package net.latin.client.widget.documentoValidante.requisito.cuitProveedor;


import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.documentoValidante.requisito.display.CUITProveedorDisplay;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.textBox.GwtTextBox;

public class CUITProveedorView extends Composite implements CUITProveedorDisplay{

	private static final String TEXTBOX_WIDTH = "100px";
	private static final int CUIT_MAX_LENGTH = 11;
	
	private GwtTextBox cuit = new GwtTextBox();
	
	public CUITProveedorView () {
		cuit.setWidth(TEXTBOX_WIDTH);
		cuit.setMaxLength( CUIT_MAX_LENGTH );
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(cuit);
		panel.add(new GwtHorizontalSpace(20));
		initWidget(panel);
	}
	
	public Widget asWidget() {
		return this;
	}

	public HasText getCUIT() {
		return this.cuit;
	}

	public void setCUITEnabled(boolean b) {
		this.cuit.setEnabled(b);
	}

	public HasKeyUpHandlers getWidgetForNextFocus(){
		return cuit;
	}

	public Focusable getFirtsFocusWidget() {
		return cuit;
	}
}
