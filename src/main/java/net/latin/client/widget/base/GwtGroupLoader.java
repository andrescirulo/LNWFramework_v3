package net.latin.client.widget.base;

import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

public abstract class GwtGroupLoader {

	public abstract void loadGroup(String pageGroup,GwtController controller);

	public abstract class GwtRunAsyncCallback implements RunAsyncCallback{

		@Override
		public void onFailure(Throwable reason) {
			Window.alert("TUF!");
		}
	}


}
