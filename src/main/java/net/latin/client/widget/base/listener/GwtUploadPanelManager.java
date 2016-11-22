package net.latin.client.widget.base.listener;

import net.latin.client.widget.msg.GwtMensajesHandler;

public interface GwtUploadPanelManager extends GwtMensajesHandler{
	public void addOnVisibleListener(GwtOnVisibleListener listener);
	public void addOnUseCaseExitListener(GwtBeforeUseCaseExitListener listener);

}
