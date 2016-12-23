package net.latin.client.widget.documentoValidante.requisito;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Focusable;

import net.latin.client.widget.documentoValidante.mvp.WidgetDisplay;



public interface RequisitoValidanteDisplay extends WidgetDisplay{
	public HasKeyUpHandlers getWidgetForNextFocus();
	public Focusable getFirtsFocusWidget();
}
