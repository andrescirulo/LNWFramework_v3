package net.latin.client.widget.base.listener;

import net.latin.client.widget.msg.GwtMensajesListener;

public interface GwtUploadPanelManager extends GwtMensajesListener{
	public void addOnVisibleListener(GwtOnVisibleListener listener);
	public void addOnUseCaseExitListener(GwtBeforeUseCaseExitListener listener);

}
