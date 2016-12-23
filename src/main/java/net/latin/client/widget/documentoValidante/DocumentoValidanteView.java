package net.latin.client.widget.documentoValidante;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.documentoValidante.DocumentoValidante.DocumentoValidanteDisplay;
import net.latin.client.widget.separator.GwtHorizontalSpace;

public class DocumentoValidanteView extends Composite implements DocumentoValidanteDisplay {
	
	private HorizontalPanel panel;
	private HorizontalPanel container;
	private GwtButton boton;
	
	public DocumentoValidanteView() {
		panel = new HorizontalPanel();
		container = new HorizontalPanel();
		panel.add(container);
		container.add(new Label("Cargando componente..."));
		panel.add(new GwtHorizontalSpace(20));
		boton = new GwtButton();
		panel.add(boton);
		boton.setVisible(false);
		initWidget(panel);
	}
	
	public Widget asWidget() {
		return this;
	}
	
	public HasClickHandlers getButton() {
		return boton;
	}
	
	public void setDocumentoValidante(Widget widget) {
		container.clear();
		container.add(widget);
		boton.setVisible(true);
	}
	
	public void setBotonEnabled(boolean b) {
		this.boton.setEnabled(b);
	}
	
	public void setBotonText(String texto) {
		this.boton.setText(texto);
	}
	
}
