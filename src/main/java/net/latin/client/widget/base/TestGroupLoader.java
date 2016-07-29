package net.latin.client.widget.base;

import com.google.gwt.core.client.GWT;

import net.latin.client.test.inicio.InicioTestGroup;

public class TestGroupLoader extends GwtGroupLoader {

	@Override
	public void loadGroup(String pageGroup, GwtController controller) {
		if ("InicioTestCase".equals(pageGroup)) {
			GWT.runAsync(new GwtRunAsyncCallback() {
				public void onSuccess() {
					controller.addPageGroup("InicioTestCase", new InicioTestGroup());
					controller.finishGroupLoading(pageGroup);
				}
			});
		}
	}

}
