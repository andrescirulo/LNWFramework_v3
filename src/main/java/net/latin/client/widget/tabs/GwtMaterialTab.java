package net.latin.client.widget.tabs;

import java.util.List;

import gwt.material.design.client.ui.MaterialPanel;
import net.latin.client.widget.msg.GwtMensajesHandler;

public class GwtMaterialTab extends MaterialPanel implements GwtMensajesHandler {

	private GwtMensajesHandler handler;
	public GwtMaterialTab() {
	}
	
	public GwtMaterialTab(GwtMensajesHandler handler) {
		this.handler = handler;
	}
	
	public void addAllErrorMessages(List<String> messagesList) {
		handler.addAllErrorMessages(messagesList);
	}
	public void clearMessages() {
		handler.clearMessages();
	}
	public void addErrorMessage(String text) {
		handler.addErrorMessage(text);
	}
	public void addAlertMessage(String text) {
		handler.addAlertMessage(text);
	}
	public void addOkMessage(String text) {
		handler.addOkMessage(text);
	}
	public void addLoadingMessage(String text) {
		handler.addLoadingMessage(text);
	}
	
	
}
