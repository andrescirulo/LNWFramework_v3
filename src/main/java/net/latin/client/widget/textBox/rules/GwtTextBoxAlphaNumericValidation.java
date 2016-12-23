package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;

import net.latin.client.validation.GwtValidationUtils;

/**
 * Solo permite letras y numeros
 *
 * @author Matias Leone
 */
public class GwtTextBoxAlphaNumericValidation extends GwtTextBoxValidationRule{

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode,KeyEvent<?> event) {
		if(event.isShiftKeyDown()){
			return GwtValidationUtils.validateAlphabetic( keyCode );
		}
		return GwtValidationUtils.validateNumeric( keyCode ) || GwtValidationUtils.validateAlphabetic( keyCode );
	}
	
}
