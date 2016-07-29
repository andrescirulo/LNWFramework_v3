package net.latin.client.widget.textBox.rules;


/**
 * Auto-completa el texto ingresado con los caracteres especificados, hasta que
 * se alcanze la longitud minima requerida
 *
 * @author Matias Leone
 */
public class GwtTextBoxAutoCompleteValidation extends GwtTextBoxValidationRule{

	public final static int AUTO_COMPLETE_LEFT = 0;
	public final static int AUTO_COMPLETE_RIGHT = 1;

	private char autoCompleteCharacter;
	private int minLength;
	private int autoCompleteSide;

	/**
	 *
	 * @param autoCompleteCharacter caracter con el cual se va a auto-completar
	 * @param minLength longitud minima del texto hasta la cual se va a auto-completar
	 * @param autoCompleteSide lado sobre el cual se va a auto-completar: AUTO_COMPLETE_LEFT o AUTO_COMPLETE_RIGHT
	 */
	public GwtTextBoxAutoCompleteValidation(char autoCompleteCharacter, int minLength, int autoCompleteSide) {
		this.autoCompleteCharacter = autoCompleteCharacter;
		this.minLength = minLength;

		if(autoCompleteSide != AUTO_COMPLETE_LEFT && autoCompleteSide != AUTO_COMPLETE_RIGHT) {
			this.autoCompleteSide = AUTO_COMPLETE_RIGHT;
		} else {
			this.autoCompleteSide = autoCompleteSide;
		}

	}


	public String validateOnLostFocus(String text) {
		//ver si falta completar algo hasta llegar a la longitud minima
		String completedText = text;
		int diff = minLength - text.length();
		if( diff > 0 ) {
			//generar cadena de auto-completado con los caracteres faltantes
			StringBuilder autoComplete = new StringBuilder();
			for (int i = 0; i < diff; i++) {
				autoComplete.append( autoCompleteCharacter );
			}

			//agregar a derecha
			if(autoCompleteSide == AUTO_COMPLETE_RIGHT) {
				completedText = completedText + autoComplete.toString();

			//agregar a izquierda
			} else if(autoCompleteSide == AUTO_COMPLETE_LEFT){
				completedText = autoComplete.toString() + completedText;
			}
		}
		return completedText;
	}


}
