package net.latin.client.widget.base.mensajes;

import java.util.List;

import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.msg.GwtMensajesHandler;

public class DefaultPageMensajesHandler implements GwtMensajesHandler {

	
	private GwtController controller;
	
	public DefaultPageMensajesHandler() {
		controller=GwtController.instance;
	}

	public void addAllErrorMessages(List<String> messagesList) {
		controller.addAllErrorMessages(messagesList);
	}
	public void addAllAlertMessages(List<String> messagesList) {
		controller.addAllAlertMessages(messagesList);
	}
	public void addAllOkMessages(List<String> messagesList) {
		controller.addAllOkMessages(messagesList);
	}

	public void clearMessages() {
		controller.clearMessages();
	}

	@Override
	public void addErrorMessage(String text) {
		controller.addErrorMessage(text);
	}

	public void addAlertMessage(String text) {
		controller.addAlertMessage(text);

	}

	public void addOkMessage(String text) {
		controller.addOkMessage(text);
	}

	public void addLoadingMessage(String text) {
		controller.addLoadingMessage(text);
	}

}
