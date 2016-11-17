package net.latin.client.widget.button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import net.latin.client.widget.base.GwtPage;

/**
 * Boton que vuelve a la page anterior registrada en el stack, cuando
 * hacen click sobre el.
 *
 */
public class GwtBackButton extends GwtButton implements ClickHandler {

//	private static final String BUTTON_CSS = "GwtBackButton";

	private GwtPage page;

	public GwtBackButton( String text, GwtPage page ) {
		super( text );
		this.page = page;
		this.addClickHandler( this );
//		setStyleName(BUTTON_CSS);
	}

	public void onClick(ClickEvent event) {
		goBackward();
	}

	/**
	 * Regresa a la page anterior registrada en el stack
	 */
	private void goBackward() {
		page.goBackward();
	}

}
