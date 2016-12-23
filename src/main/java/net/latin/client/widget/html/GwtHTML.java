package net.latin.client.widget.html;

import com.google.gwt.user.client.ui.HTML;

import net.latin.client.widget.base.LnwWidget;

public class GwtHTML extends HTML implements LnwWidget{

	private boolean resetDisabled;
	
	public GwtHTML(String html, boolean wordWrap) {
		super(html, wordWrap);
	}
	public GwtHTML(String html) {
		super(html);
	}
	public GwtHTML() {
	}
	public void resetWidget() {
		if(!resetDisabled) {
			resetExteneralWidget(this);
		}
	}
	
	/**
	 * @return the resetDisabled
	 */
	public boolean isResetDisabled() {
		return resetDisabled;
	}
	
	public static void resetExteneralWidget(HTML html) {
		html.setText("");
	}

	/**
	 * En true, evita que el label se limpie, al llamar a resetWidget()
	 */
	public void setResetDisabled(boolean flag) {
		this.resetDisabled = flag;
	}

	public void setFocus() {
	}
}
