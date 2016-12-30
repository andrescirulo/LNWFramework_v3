package net.latin.client.widget.documentoValidante.requisito.dni;


import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.documentoValidante.requisito.display.DNIDisplay;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.textBox.GwtTextBox;

public class DNIView extends Composite implements DNIDisplay{

	private static final String TEXTBOX_WIDTH = "65px";
	private static final int DNI_MAX_LENGTH = 8;
	
	private GwtTextBox dni = new GwtTextBox();
	
	public DNIView () {
		dni.setWidth(TEXTBOX_WIDTH);
		dni.setMaxLength( DNI_MAX_LENGTH );
		
		HorizontalPanel panel = new HorizontalPanel();

		panel.add(dni);
		panel.add(new GwtHorizontalSpace(20));
		initWidget(panel);
	}
	
	public Widget asWidget() {
		return this;
	}

	public HasText getDNI() {
		return this.dni;
	}

	public void setDNIEnabled(boolean b) {
		this.dni.setEnabled(b);
	}

	public HasKeyUpHandlers getWidgetForNextFocus() {
		return dni;
	}

	public Focusable getFirtsFocusWidget() {
		return dni;
	}

	public String getWidth() {
		return "130px";
	}
}
