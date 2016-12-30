package net.latin.client.widget.title;

import com.google.gwt.user.client.ui.Label;

import net.latin.client.widget.base.LnwWidget;

/**
 * Titulo de LNW
 *
 */
public class GwtTitle extends Label implements LnwWidget {

	public final static String CCS_TITLE = "gwt-title";

	public GwtTitle( String title ) {
		super( title );
		this.setStyleName( CCS_TITLE );
	}

	public void resetWidget() {
	}

	public void setFocus() {
	}

}
