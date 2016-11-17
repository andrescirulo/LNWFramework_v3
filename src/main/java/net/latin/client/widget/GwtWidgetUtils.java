package net.latin.client.widget;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.widget.base.LnwWidget;

public class GwtWidgetUtils {

	public static final int KEY_SPACEBAR = 32;

	/**
	 * Hace focus muchas veces, porque a veces IE no te da pelota
	 * @param widget
	 */
	public static final void  setFocus( FocusWidget widget ) {
		widget.setFocus( true );
		widget.setFocus( true );
		widget.setFocus( true );
	}
	
	/**
	 * Hace focus muchas veces, porque a veces IE no te da pelota
	 * @param widget
	 */
	public static final void  setFocus( LnwWidget widget ) {
		widget.setFocus();
		widget.setFocus();
		widget.setFocus();
	}


	public static void addStyleOver(HasAllMouseHandlers widget, final String style) {
		widget.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				((Widget)event.getSource()).addStyleName(style);
			}
		});
		widget.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				((Widget)event.getSource()).removeStyleName(style);
			}
		});
	}

}
