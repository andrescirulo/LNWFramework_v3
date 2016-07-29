package net.latin.client.widget.textBox.rules;

/**
 * Quita los espacios en blanco laterales del texto, al perder el foco
 *
 * @author Matias Leone
 */
public class GwtTextBoxTrimValidation extends GwtTextBoxValidationRule{

	public String validateOnLostFocus(String text) {
		return text.trim();
	}

}
