package net.latin.client.widget.textBox.rules;

import net.latin.client.validation.GwtValidationUtils;

/**
 * El valor ingresado debe estar dentro del rango especificado, inclusive
 *
 * @author Matias Leone
 */
public class GwtTextBoxIntervalNumericValidation extends GwtTextBoxValidationRule{

	private double min;
	private double max;

	public GwtTextBoxIntervalNumericValidation( double min, double max ) {
		this.min = min;
		this.max = max;
	}

	public String validateOnLostFocus(String text) {
		if( GwtValidationUtils.validateDouble(text) ) {
			double n = GwtValidationUtils.getDouble(text);
			if( n < this.min || n > this.max ) {
				return null;
			}
		}
		return text;
	}


}
