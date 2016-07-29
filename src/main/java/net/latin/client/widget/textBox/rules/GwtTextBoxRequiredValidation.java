package net.latin.client.widget.textBox.rules;

import net.latin.client.widget.validation.GwtValidationUtils;

/**
 * El campo debe ser completado con alguna entrada
 *
 * @author Matias Leone
 */
public class GwtTextBoxRequiredValidation extends GwtTextBoxValidationRule{

	public String validateOnLostFocus(String text) {
		if(!GwtValidationUtils.validateRequired(text)) {
			return null;
		}
		return text;

	}

}
