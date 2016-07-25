package net.latin.client.widget.button;

import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.base.LnwWidget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.polymer.paper.widget.PaperButton;

/**
 * Button con estilo predefinido
 *
 */
public class GwtButton extends PaperButton implements LnwWidget {

//	public static final String BUTTON_CSS = "GwtButton";
//	public static final String BUTTON_SELECTED_CSS = "GwtButtonSelected";
//	public String BUTTON_OVER_CSS = "";

	public GwtButton() {
		super();
//		setStyleName(BUTTON_CSS);
		setRaised(true);
	}

	public GwtButton(String html, ClickHandler handler) {
		super(html);
		addClickHandler(handler);
//		setStyleName(BUTTON_CSS);
		setRaised(true);
	}

	public GwtButton(String html) {
		super(html);
//		setStyleName(BUTTON_CSS);
		setRaised(true);
	}
	

	public void resetWidget() {
	}

	public void setFocus() {
		GwtWidgetUtils.setFocus(this);
	}

	public String getText() {
		return this.getElement().getInnerHTML();
	}

	public void setText(String html) {
		this.getElement().setInnerHTML(html);
	}

}
