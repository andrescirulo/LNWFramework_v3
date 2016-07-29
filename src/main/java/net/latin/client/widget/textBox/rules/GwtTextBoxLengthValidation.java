package net.latin.client.widget.textBox.rules;

import com.google.gwt.event.dom.client.KeyEvent;


/**
 * El texto ingresado debe tener una longitud dentro del intervalo especificado,
 * inclusive
 *
 * @author Matias Leone
 */
public class GwtTextBoxLengthValidation extends GwtTextBoxValidationRule{

	public final static int DEFAULT_MIN = -1;
	public final static int DEFAULT_MAX = 999999999;

	private int min;
	private int max;

	public GwtTextBoxLengthValidation( int min, int max ) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean validateOnKeyDown(String currentText, int keyCode,KeyEvent<?> event) {
		if(currentText.length() >= max) {
			return false;
		}
		return true;
	}
	
	public String validateOnLostFocus(String text) {
		int length = text.length();
		if( length < min || length > max ) {
			return null;
		}
		return text;
	}


}
