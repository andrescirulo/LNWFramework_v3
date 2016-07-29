package net.latin.client.widget.textBox.rules;

/**
 * Convierte el texto en mayusculas al perder el foco
 *
 * @author Matias Leone
 */
public class GwtTextBoxUpperCaseValidation extends GwtTextBoxValidationRule{

	public String validateOnLostFocus(String text) {
		return text.toUpperCase();
	}

}
