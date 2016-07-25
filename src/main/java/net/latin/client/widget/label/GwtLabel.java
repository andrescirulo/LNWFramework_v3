package net.latin.client.widget.label;

import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;

import com.google.gwt.user.client.ui.Label;

/**
 * Label de LNW
 *
 */
public class GwtLabel extends Label implements LnwWidget {

	private boolean resetDisabled;

	public GwtLabel() {
		super();
		this.setWidth( getDefaultWidth() );
		this.setHeight( getDefaultHeight() );
	}

	public GwtLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		this.setWidth( getDefaultWidth() );
		this.setHeight( getDefaultHeight() );
	}

	public GwtLabel(String text) {
		super(text);
		this.setWidth( getDefaultWidth() );
		this.setHeight( getDefaultHeight() );
	}

	private String getDefaultHeight() {
		return GwtController.instance.getI18n().GwtLabel_height;
	}

	public void resetWidget() {
		if(!resetDisabled) {
			resetExteneralWidget(this);
		}
	}

	public void setFocus() {
		focusExternalWidget(this);
	}

	private final String getDefaultWidth() {
		return GwtController.instance.getI18n().GwtLabel_width;
	}

	public static void resetExteneralWidget(Label label) {
		label.setText("");
	}

	public static void focusExternalWidget(Label label) {
	}

	/**
	 * @return the resetDisabled
	 */
	public boolean isResetDisabled() {
		return resetDisabled;
	}

	/**
	 * En true, evita que el label se limpie, al llamar a resetWidget()
	 */
	public void setResetDisabled(boolean flag) {
		this.resetDisabled = flag;
	}

}
