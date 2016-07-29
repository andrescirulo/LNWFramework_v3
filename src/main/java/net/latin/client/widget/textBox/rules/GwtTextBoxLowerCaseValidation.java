package net.latin.client.widget.textBox.rules;

/**
 * Convierte el texto en minuscula al perder el foco
 *
 * @author Matias Leone
 */
public class GwtTextBoxLowerCaseValidation extends GwtTextBoxValidationRule{

	public String validateOnLostFocus(String text) {
		return text.toLowerCase();
	}

}
