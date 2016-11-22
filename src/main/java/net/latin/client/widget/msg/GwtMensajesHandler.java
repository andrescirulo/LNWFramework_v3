package net.latin.client.widget.msg;


/**
 * Listener for messages manipulation
 *
 * @author Matias Leone
 */
public interface GwtMensajesHandler extends GwtMsgHandler {

	public void addErrorMessage(String text);
	public void addAlertMessage(String text);
	public void addOkMessage(String text);
	public void addLoadingMessage(String text);

}
