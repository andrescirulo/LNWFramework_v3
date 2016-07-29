package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;

import net.latin.client.widget.validation.GwtValidationUtils;

/**
 * El texto ingresado debe ser un numero entero
 *
 * @author Matias Leone
 */
public class GwtTextBoxIntegerValidation extends GwtTextBoxValidationRule{

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode,
			KeyEvent<?> event) {
		
		return GwtValidationUtils.validateNumeric(keyCode) && !event.isAnyModifierKeyDown();
	}
	
	public String validateOnLostFocus(String text) {
		if(GwtValidationUtils.validateInteger(text)) {
			return text;
		}
		return null;
	}

}
