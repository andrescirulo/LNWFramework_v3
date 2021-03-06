package net.latin.client.widget.button;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import net.latin.client.widget.base.LnwWidget;

/**
 * Button con estilo predefinido
 *
 */
public class GwtButton extends MaterialButton implements LnwWidget {


	public GwtButton() {
		super();
		setType(ButtonType.RAISED);
		setWaves(WavesType.DEFAULT);
	}

	public GwtButton(String html, ClickHandler handler) {
		super(html);
		addClickHandler(handler);
		setType(ButtonType.RAISED);
		setWaves(WavesType.DEFAULT);
	}

	public void setColor(Color backColor, Color textColor) {
		setBackgroundColor(backColor);
		setTextColor(textColor);
	}

	public GwtButton(String html) {
		super(html);
		setType(ButtonType.RAISED);
		setWaves(WavesType.DEFAULT);
	}
	

	public void resetWidget() {
	}

	public void setFocus() {
		setFocus(true);
	}

	public String getText() {
		return this.getElement().getInnerHTML();
	}

	public void setText(String html) {
		this.getElement().setInnerHTML(html);
	}
	
	public void click(){
		buttonClick(this.getElement());
	}
	private native void buttonClick(Element button) /*-{
	    button.click();
	}-*/;

}
