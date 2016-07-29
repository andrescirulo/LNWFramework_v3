package net.latin.client.widget.form;

import com.google.gwt.user.client.ui.HTML;

public class GwtFooterLabel extends HTML {

	private static final String CSS_FOOTER_LABEL = "GwtFooterLabel";

	public GwtFooterLabel() {
		super();
	}

	public GwtFooterLabel(String text) {
		super();
		setHTML(text);
		setStyleName(CSS_FOOTER_LABEL);
	}
}
