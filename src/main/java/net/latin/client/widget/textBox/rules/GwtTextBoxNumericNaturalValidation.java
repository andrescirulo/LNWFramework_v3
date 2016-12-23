package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;

import net.latin.client.validation.GwtValidationUtils;

/**
 * Solo permite numeros naturales
 *
 * @author Axel Forgaral
 */
public class GwtTextBoxNumericNaturalValidation extends GwtTextBoxValidationRule{

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode,KeyEvent<?> event) {
		return !event.isShiftKeyDown() &&GwtValidationUtils.validateNumericNatural(keyCode);
	}


}
