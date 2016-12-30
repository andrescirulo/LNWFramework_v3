package net.latin.client.widget.base.mensajes;

import static gwt.material.design.jquery.client.api.JQuery.$;

import java.util.List;

import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.JQueryElement;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.msg.GwtMsgTypeEnum;

public class ToastPageMensajesHandler implements GwtMensajesHandler {


	public void addAllErrorMessages(List<String> messagesList) {
		for (String message:messagesList){
			addErrorMessage(message);
		}
	}
	public void addAllAlertMessages(List<String> messagesList) {
		for (String message:messagesList){
			addAlertMessage(message);
		}
	}
	public void addAllOkMessages(List<String> messagesList) {
		for (String message:messagesList){
			addOkMessage(message);
		}
	}

	public void clearMessages() {
		//MaterialToast.fireToast(msg);
		JQueryElement container = $("#toast-container");
		try{
			while (container.firstChild()!=null){
				container.removeChild(container.firstChild());
			}
		}
		catch (Exception e){
			//Simplemente no tiro error
		}
	}

	@Override
	public void addErrorMessage(String text) {
		MaterialToast.fireToast(text,GwtMsgTypeEnum.ERROR.getBgColor().getCssName() + " toast-text");
	}

	public void addAlertMessage(String text) {
		MaterialToast.fireToast(text,GwtMsgTypeEnum.ALERT.getBgColor().getCssName() + " toast-text");
	}

	public void addOkMessage(String text) {
		MaterialToast.fireToast(text,50000,GwtMsgTypeEnum.OK.getBgColor().getCssName() + " toast-text");
	}

	public void addLoadingMessage(String text) {
		MaterialToast.fireToast(text,GwtMsgTypeEnum.LOADING.getBgColor().getCssName() + " toast-text");
	}

}
