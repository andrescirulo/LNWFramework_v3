package net.latin.client.widget.form;

import com.google.gwt.user.client.ui.Label;

import net.latin.client.widget.base.LnwWidget;

/**
 * Subtitulo para meter dentro de un GwtForm
 *
 */
public class GwtFormSubtitle extends Label implements LnwWidget {

	public GwtFormSubtitle() {
		this("");
	}

	public GwtFormSubtitle( String text ) {
		this.setStyleName( "form-subtitle" );
		this.setText( text );
	}

	public void resetWidget() {
	}

	public void setFocus() {
	}

}
