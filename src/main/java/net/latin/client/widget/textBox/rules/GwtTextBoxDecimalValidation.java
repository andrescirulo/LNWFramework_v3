package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.i18n.client.NumberFormat;

import net.latin.client.validation.GwtValidationUtils;

/**
 * El texto ingresado deber ser un numero decimal que se formatea con el patron
 * especificado
 *
 * @author Matias Leone
 */
public class GwtTextBoxDecimalValidation extends GwtTextBoxValidationRule{

	public static final char DEFAULT_DECIMAL_SYMBOL = '.';

	private char decimalSymbol;
	private NumberFormat numberFormat;


	public GwtTextBoxDecimalValidation(char decimalSymbol, String decimalPattern) {
		this.decimalSymbol = decimalSymbol;
		numberFormat = NumberFormat.getFormat(decimalPattern);
	}

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode, KeyEvent<?> event) {
		//si es numero, esta ok
		if(GwtValidationUtils.validateNumeric(keyCode)) {
			return true;
		}

		//si es el separador decimal, hay que ver que ya no se halla puesto otro
		if(keyCode == decimalSymbol && currentText.indexOf(decimalSymbol) == -1) {
			return true;
		}

		return false;
	}

	public String validateOnLostFocus(String text) {
		if(GwtValidationUtils.validateDouble(text)) {
			double number = GwtValidationUtils.getDouble(text);
			return numberFormat.format(number);
		}

		return null;
	}



}
