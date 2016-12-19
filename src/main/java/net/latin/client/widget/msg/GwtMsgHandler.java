package net.latin.client.widget.msg;

import java.util.List;

public interface GwtMsgHandler {

	public void addAllErrorMessages(List<String> messagesList);
	public void addAllAlertMessages(List<String> messagesList);
	public void addAllOkMessages(List<String> messagesList);

	public void clearMessages();

}
