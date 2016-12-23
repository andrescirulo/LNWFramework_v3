package net.latin.client.widget.button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Focusable;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.base.LnwWidget;

/**
 * Button con estilo predefinido
 *
 */
public class GwtIconButton extends MaterialButton implements LnwWidget {

	public static final String BUTTON_CSS = "GwtIconButton";

	
	public GwtIconButton() {
		super();
		setStyleName(BUTTON_CSS);
		addStyleName("waves-effect");
	}

	public GwtIconButton(IconType icon, ClickHandler handler) {
		super(ButtonType.RAISED);
		setIconType(icon);
		addClickHandler(handler);
		setStyleName(BUTTON_CSS);
		addStyleName("waves-effect");
	}

	public GwtIconButton(IconType icon) {
		super(ButtonType.RAISED);
		setIconType(icon);
		setStyleName(BUTTON_CSS);
		addStyleName("waves-effect");
	}
	
	public void resetWidget() {
	}

	public void setFocus() {
		GwtWidgetUtils.setFocus((Focusable)this);
	}

	public void setIcon(IconType icon){
		setIconType(icon);
	}
}
