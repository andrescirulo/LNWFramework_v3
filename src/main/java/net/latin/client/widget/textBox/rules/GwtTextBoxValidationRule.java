package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;

public abstract class GwtTextBoxValidationRule {


	public boolean validateOnKeyDown( String currentText, int keyCode, KeyEvent<?> event) {
		return true;
	}

	public String validateOnLostFocus( String text ) {
		return text;
	}

}
