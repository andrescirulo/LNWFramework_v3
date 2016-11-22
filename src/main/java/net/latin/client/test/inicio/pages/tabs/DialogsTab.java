package net.latin.client.test.inicio.pages.tabs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.dialog.GwtConfirmAbstractListener;
import net.latin.client.widget.dialog.GwtConfirmDialogAceptar;
import net.latin.client.widget.dialog.GwtConfirmDialogSiNo;
import net.latin.client.widget.dialog.GwtConfirmDialogSiNoCancelar;
import net.latin.client.widget.fileviewer.GwtMaterialFileViewer;
import net.latin.client.widget.form.GwtForm;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.panels.GwtHorizontalPanel;
import net.latin.client.widget.tabs.GwtMaterialTab;

public class DialogsTab extends GwtMaterialTab{

	public DialogsTab(GwtMensajesHandler handler) {
		super(handler);
		agregarDialogs();
	}
	
	private void agregarDialogs() {
		GwtHorizontalPanel panel=new GwtHorizontalPanel("Dialogs (Panel horizontal)",true);
		GwtConfirmDialogAceptar aceptarDialog=new GwtConfirmDialogAceptar(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Aceptar de prueba", "Te estoy mostrando un mensaje");
		panel.add(new GwtButton("Dialog Aceptar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				aceptarDialog.showCentered();
			}
		}));
		GwtConfirmDialogSiNo siNoDialog=new GwtConfirmDialogSiNo(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Si/No de prueba", "¿Te estoy mostrando un mensaje?");
		panel.add(new GwtButton("Dialog Si/No", new ClickHandler() {
			public void onClick(ClickEvent event) {
				siNoDialog.showCentered();
			}
		}));
		
		GwtConfirmDialogSiNoCancelar siNoCancelarDialog=new GwtConfirmDialogSiNoCancelar(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Si/No/Cancelar de prueba", "¿Te estoy mostrando un mensaje?");
		panel.add(new GwtButton("Dialog Si/No/Cancelar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				siNoCancelarDialog.showCentered();
			}
		}));
		
		GwtMaterialFileViewer fileViewer=new GwtMaterialFileViewer();
		panel.add(new GwtButton("Ver PDF", new ClickHandler() {
			public void onClick(ClickEvent event) {
				fileViewer.open();
			}
		}));
		
		add(panel);
	}
}
