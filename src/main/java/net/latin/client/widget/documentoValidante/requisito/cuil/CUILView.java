package net.latin.client.widget.documentoValidante.requisito.cuil;


import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.documentoValidante.requisito.display.CUILDisplay;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.textBox.GwtTextBox;

public class CUILView extends Composite implements CUILDisplay{

	private static final String TEXTBOX_WIDTH = "100px";
	private static final int CUIL_MAX_LENGTH = 11;
	
	private GwtTextBox cuil = new GwtTextBox();
	
	public CUILView () {
		cuil.setWidth(TEXTBOX_WIDTH);
		cuil.setMaxLength( CUIL_MAX_LENGTH );
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(cuil);
		panel.add(new GwtHorizontalSpace(20));
		initWidget(panel);
	}
	
	public Widget asWidget() {
		return this;
	}

	public HasText getCUIL() {
		return this.cuil;
	}

	public void setCUILEnabled(boolean b) {
		this.cuil.setEnabled(b);
	}

	public HasKeyUpHandlers getWidgetForNextFocus(){
		return cuil;
	}

	public Focusable getFirtsFocusWidget() {
		return cuil;
	}
}
