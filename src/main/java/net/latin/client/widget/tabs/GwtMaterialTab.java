package net.latin.client.widget.tabs;

import java.util.List;

import gwt.material.design.client.ui.MaterialPanel;
import net.latin.client.widget.msg.GwtMensajesHandler;

public class GwtMaterialTab extends MaterialPanel implements GwtMensajesHandler {

	private GwtMensajesHandler msgHandler;
	public GwtMaterialTab() {
	}
	
	public GwtMaterialTab(GwtMensajesHandler handler) {
		this.msgHandler = handler;
	}
	
	public void addAllErrorMessages(List<String> messagesList) {
		msgHandler.addAllErrorMessages(messagesList);
	}
	public void clearMessages() {
		msgHandler.clearMessages();
	}
	public void addErrorMessage(String text) {
		msgHandler.addErrorMessage(text);
	}
	public void addAlertMessage(String text) {
		msgHandler.addAlertMessage(text);
	}
	public void addOkMessage(String text) {
		msgHandler.addOkMessage(text);
	}
	public void addLoadingMessage(String text) {
		msgHandler.addLoadingMessage(text);
	}

	public void setMsgHandler(GwtMensajesHandler msgHandler) {
		this.msgHandler = msgHandler;
	}
	
	
}
