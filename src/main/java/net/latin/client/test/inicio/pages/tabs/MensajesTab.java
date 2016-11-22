package net.latin.client.test.inicio.pages.tabs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.panels.GwtHorizontalPanel;
import net.latin.client.widget.tabs.GwtMaterialTab;

public class MensajesTab extends GwtMaterialTab {

	public MensajesTab(GwtMensajesHandler handler) {
		super(handler);

		GwtHorizontalPanel panelMensajes = new GwtHorizontalPanel("Mensajes",true);
		GwtButton btnError = new GwtButton("Agregar Error", new ClickHandler() {
			public void onClick(ClickEvent event) {
				addErrorMessage("Este es un mensaje de error de prueba");
			}
		});
		GwtButton btnOk = new GwtButton("Agregar Ok", new ClickHandler() {
			public void onClick(ClickEvent event) {
				addOkMessage("Este es un mensaje OK de prueba");
			}
		});
		GwtButton btnAlert = new GwtButton("Agregar Alerta", new ClickHandler() {
			public void onClick(ClickEvent event) {
				addAlertMessage("Este es un mensaje de alerta de prueba");
			}
		});
		GwtButton btnLoading = new GwtButton("Agregar Loading", new ClickHandler() {
			public void onClick(ClickEvent event) {
				addLoadingMessage("Este es un mensaje de loading de prueba");
			}
		});
		GwtButton btnLimpiar = new GwtButton("Limpiar mensajes", new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearMessages();
			}
		});
		panelMensajes.add(btnOk);
		panelMensajes.add(btnAlert);
		panelMensajes.add(btnError);
		panelMensajes.add(btnLoading);
		panelMensajes.add(btnLimpiar);
		
		this.add(panelMensajes);
	}
}
