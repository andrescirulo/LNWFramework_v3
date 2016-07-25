package net.latin.client.widget.panels;

import java.util.Iterator;

import net.latin.client.widget.base.LnwWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

public class GwtHorizontalPanel extends HorizontalPanel implements LnwWidget {
	
	public void resetWidget() {
		WidgetCollection widgets = this.getChildren();
		for (Iterator<Widget> iterator = widgets.iterator(); iterator.hasNext();) {
			Widget widget = iterator.next();
			// Intento realizar el casteo.
			try {
				((LnwWidget) widget).resetWidget();
			} catch (ClassCastException e) {
				// No debo realizar nada.
			}
		}
	}

	public void setFocus() {
		//FIXME delegar foco!
	}
	
	public HandlerRegistration addClickHandler(ClickHandler listener) {
		return this.addDomHandler(listener, ClickEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler){
		return this.addDomHandler(handler, MouseOverEvent.getType());
	}
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler){
		return this.addDomHandler(handler, MouseOutEvent.getType());
	}
}
