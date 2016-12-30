package net.latin.client.widget.documentoValidante.requisito.matriculaCorte;


import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.documentoValidante.requisito.display.MatriculaCorteDisplay;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.textBox.GwtTextBox;

public class MatriculaCorteView extends Composite implements MatriculaCorteDisplay{

	private static final String TEXTBOX_WIDTH = "50px";
	private static final int MATRICULA_MAX_LENGTH = 6;
	
	private GwtTextBox matriculaCorte = new GwtTextBox();
	
	public MatriculaCorteView () {
		matriculaCorte.setWidth(TEXTBOX_WIDTH);
		matriculaCorte.setMaxLength( MATRICULA_MAX_LENGTH );
		
		HorizontalPanel panel = new HorizontalPanel();

		panel.add(matriculaCorte);
		panel.add(new GwtHorizontalSpace(20));
		initWidget(panel);
	}
	
	public Widget asWidget() {
		return this;
	}

	public HasText getMatriculaCorte() {
		return this.matriculaCorte;
	}

	public void setMatriculaCorteEnabled(boolean b) {
		this.matriculaCorte.setEnabled(b);
	}

	public HasKeyUpHandlers getWidgetForNextFocus() {
		return matriculaCorte;
	}

	public Focusable getFirtsFocusWidget() {
		return matriculaCorte;
	}

	public String getWidth() {
		return "130px";
	}
}
