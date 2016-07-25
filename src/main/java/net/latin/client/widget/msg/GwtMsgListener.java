package net.latin.client.widget.msg;

import java.util.List;

/**
 * Listener for messages manipulation
 *
 */
public interface GwtMsgListener {

	public void addAllErrorMessages(List<String> messagesList);

	public void clearMessages();

}
