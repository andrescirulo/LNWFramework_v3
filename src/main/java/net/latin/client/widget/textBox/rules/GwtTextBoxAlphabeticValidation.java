package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;

import net.latin.client.widget.validation.GwtValidationUtils;

/**
 * Solo permite letras
 *
 * @author Matias Leone
 */
public class GwtTextBoxAlphabeticValidation extends GwtTextBoxValidationRule{

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode,KeyEvent<?> event) {
		return GwtValidationUtils.validateAlphabetic( keyCode );
	}


}
