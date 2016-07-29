package net.latin.client.widget.textBox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.textBox.rules.GwtTextBoxAlphaNumericValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxAlphabeticValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxAutoCompleteValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxDecimalValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxIntegerValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxIntervalNumericValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxLengthValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxLowerCaseValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxNumericDecimalValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxNumericIntegerValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxNumericNaturalValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxNumericValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxRequiredValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxTrimValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxUpperCaseValidation;
import net.latin.client.widget.textBox.rules.GwtTextBoxValidationRule;
import net.latin.client.widget.validation.GwtValidationUtils;

/**
 * TextBox que posee validaciones sobre los datos que carga el usuario.
 * El TextBox puede ser cargado con reglas de validacion (GwtTextBoxValidationRule)
 * Existen dos formas de validacion de la entrada del usuario:
 * 1) Validar cada caracter que ingresa, permitiendolo o ignorandolo
 * 2) Formatear o anular el texto que quedo cargado, luego de que el textBox perdio
 * el foco. En este caso se muestra un popup de error a la derecha del widget, con un
 * mensaje de error que puede ser configurado.
 * Las reglas de validacion deben ser cargadas en orden correcto.
 *
 *
 * <p>
 * Ejemplo 1: validar Float
 * <pre>
 * GwtValidateTextBox textBox = new GwtValidateTextBox();
 * textBox.setValidationText("Debe cargar correctamente este campo");
 * textBox.setTrim();
 * textBox.setRequired();
 * textBox.setDecimal('.', "##00.00##");
 * textBox.setMaxNumeric(5000);
 * textBox.setMinNumeric(15);
 * </pre>
 *
 * <p>
 * Ejemplo 2: validar String
 * <pre>
 * GwtValidateTextBox textBox = new GwtValidateTextBox();
 * textBox.setValidationText("Debe cargar correctamente este campo");
 * textBox.setTrim();
 * textBox.setRequired();
 * textBoxValid.setAlphabetic();
 * textBoxValid.setLength(3, 7);
 *
 *
 */
public class GwtValidateTextBox extends GwtTextBox implements KeyDownHandler,BlurHandler,FocusHandler  {

	private final static String CSS_REQUIRED_OK = "GwtTextBoxRequiredValidationOk";
	private final static String CSS_REQUIRED_FALSE = "GwtTextBoxRequiredValidationError";

	private final List<GwtTextBoxValidationRule> validationRules;
	private boolean validationsEnabled;
	private Boolean popUpEnabled = true;
	private LnwWidget nextFocus;

	public GwtValidateTextBox(boolean trim) {
		this.validationRules = new ArrayList<GwtTextBoxValidationRule>();
		this.addDomHandler(this, KeyDownEvent.getType());
		this.addDomHandler(this, BlurEvent.getType());
		this.addDomHandler(this, FocusEvent.getType());
//		addKeyDownHandler(this);
//		addFocusListener(this);
		validationsEnabled = true;
		if (trim){
			setTrim();
		}
	}
	
	public GwtValidateTextBox() {
		this(false);
	}

	public void onKeyDown(KeyDownEvent event) {
		int keyCode=event.getNativeKeyCode();
		if(!validationsEnabled) return;

		if (keyCode==KeyCodes.KEY_ENTER)
		{
			if(nextFocus!=null)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						nextFocus.setFocus();
					}
				});
			}
		}
		
		//evitamos llamar a las reglas de validacion para las teclas especiales
		if(GwtValidationUtils.isSpecialKey(keyCode, event))
			return;

		//aplicamos todas las reglas de validacion para keyDown
		int selLen = this.getSelectionLength();
		boolean result;
		String currentText = this.getValue();
		for (GwtTextBoxValidationRule rule : validationRules) {
			//si la validacion no pasa correctamente, cancelamos la tecla presionada
			if(!(selLen == this.getMaxlength() && rule.getClass().equals(GwtTextBoxLengthValidation.class))){
				result = rule.validateOnKeyDown(currentText, keyCode, event);
				if(!result) {
					event.preventDefault();
//					this.cancelKey();
					return;
				}
			}
		}
	}

	
	public void onBlur(BlurEvent event) {
		if(!validationsEnabled) return;

		//aplicamos todas las reglas de validacion para lostFocus
		String result = this.getValue();
		String lastResult;
		for (GwtTextBoxValidationRule rule : validationRules) {
			lastResult = result;
			result = rule.validateOnLostFocus(result);
			if(result == null) {
				this.setStyleName( CSS_REQUIRED_FALSE );
				this.setErrorMessage("error en la validacion");
				this.setValue(lastResult);
				return;
			}
		}
		this.setValue(result);
	}
	
	public void onFocus(FocusEvent event) {
		this.setStyleName( CSS_REQUIRED_OK );
	}

	/**
	 * Agrega una regla de validacion
	 */
	public void addValidationRule( GwtTextBoxValidationRule rule ) {
		validationRules.add( rule );
	}

	/**
	 * Quita una regla de validacion agregada
	 */
	public void removeValidationRule( GwtTextBoxValidationRule rule ) {
		validationRules.remove( rule );
	}

	/**
	 * Elimina todas las reglas de validacion agregadas
	 */
	public void clearValidationRules() {
		validationRules.clear();
	}


	/**
	 * Solo permite letras
	 */
	public void setAlphabetic() {
		this.addValidationRule( new GwtTextBoxAlphabeticValidation() );
	}

	/**
	 * Solo permite letras y numeros
	 */
	public void setAlphaNumeric() {
		this.addValidationRule( new GwtTextBoxAlphaNumericValidation() );
	}

	/**
	 * Solo permite numeros (de cualquier tipo)
	 */
	public void setNumeric() {
		this.addValidationRule( new GwtTextBoxNumericValidation() );
	}
	/**
	 * Solo permite naturales
	 */
	public void setNumericNatural() {
		this.addValidationRule( new GwtTextBoxNumericNaturalValidation() );
	}
	/**
	 * Solo permite enteros
	 */
	public void setNumericInteger() {
		this.addValidationRule( new GwtTextBoxNumericIntegerValidation() );
	}
	/**
	 * Solo permite decimales
	 */
	public void setNumericDecimal() {
		this.addValidationRule( new GwtTextBoxNumericDecimalValidation() );
	}

	/**
	 * Convierte el texto en minuscula al perder el foco
	 */
	public void setLowerCase() {
		this.addValidationRule( new GwtTextBoxLowerCaseValidation() );
	}

	/**
	 * Convierte el texto en mayusculas al perder el foco
	 */
	public void setUpperCase() {
		this.addValidationRule( new GwtTextBoxUpperCaseValidation() );
	}

	/**
	 * Quita los espacios en blanco laterales del texto, al perder el foco
	 */
	public void setTrim() {
		this.addValidationRule( new GwtTextBoxTrimValidation() );
	}

	/**
	 * El valor ingresado debe ser menor al numero especificado, inclusive
	 */
	public void setMinNumeric( double min ) {
		this.addValidationRule( new GwtTextBoxIntervalNumericValidation( min, Double.MAX_VALUE ));
	}

	/**
	 * El valor ingresado debe ser mayor al numero especificado, inclusive
	 */
	public void setMaxNumeric( double max ) {
		this.addValidationRule( new GwtTextBoxIntervalNumericValidation( Double.MIN_VALUE, max ));
	}

	/**
	 * El valor ingresado debe estar dentro del rango especificado, inclusive
	 */
	public void setMinMaxNumeric( double min, double max ) {
		this.addValidationRule( new GwtTextBoxIntervalNumericValidation( min, max ));
	}

	/**
	 * El campo debe ser completado con alguna entrada
	 */
	public void setRequired() {
		this.addValidationRule( new GwtTextBoxRequiredValidation() );
	}

	/**
	 * El texto ingresado debe tener una longitud dentro del intervalo especificado,
	 * inclusive
	 */
	public void setLength(int minLength, int maxLength) {
		this.addValidationRule( new GwtTextBoxLengthValidation(minLength, maxLength) );
	}

	/**
	 * El texto ingresado debe tener una longitud mayor o igual al minimo especificado, inclusive
	 */
	public void setMinLength(int minLength) {
		this.addValidationRule( new GwtTextBoxLengthValidation(minLength, GwtTextBoxLengthValidation.DEFAULT_MAX) );
	}

	/**
	 * El texto ingresado debe tener una longitud menor o igual al maximo especificado
	 */
	public void setMaxLength(int maxLength) {
		super.setMaxlength(maxLength);
		this.addValidationRule( new GwtTextBoxLengthValidation(GwtTextBoxLengthValidation.DEFAULT_MIN, maxLength) );
	}

	/**
	 * El texto ingresado deber ser un numero decimal que se formatea con el patron
	 * especificado
	 * @param decimalSymbol simbolo que delimita los numeros enteros de los decimales (normalmente '.')
	 * @param decimalPattern patron de formateo del numero. Ej: 00.00##
	 */
	public void setDecimal(char decimalSymbol, String decimalPattern) {
		this.addValidationRule( new GwtTextBoxDecimalValidation(decimalSymbol,decimalPattern) );
	}

	/**
	 * El texto ingresado debe ser un numero entero
	 */
	public void setInteger() {
		this.addValidationRule( new GwtTextBoxIntegerValidation() );
	}

	/**
	 * Auto-completa a izquierda el texto ingresado con los caracteres especificados, hasta que
	 * se alcanze la longitud minima requerida
	 * @param autoCompleteCharacter caracter con el cual se va a auto-completar
	 * @param minLength longitud minima del texto hasta la cual se va a auto-completar
	 */
	public void setAutoCompleteLeft(char autoCompleteCharacter, int minLength) {
		this.addValidationRule( new GwtTextBoxAutoCompleteValidation(autoCompleteCharacter, minLength, GwtTextBoxAutoCompleteValidation.AUTO_COMPLETE_LEFT) );
	}

	/**
	 * Auto-completa a derecha el texto ingresado con los caracteres especificados, hasta que
	 * se alcanze la longitud minima requerida
	 * @param autoCompleteCharacter caracter con el cual se va a auto-completar
	 * @param minLength longitud minima del texto hasta la cual se va a auto-completar
	 */
	public void setAutoCompleteRight(char autoCompleteCharacter, int minLength) {
		this.addValidationRule( new GwtTextBoxAutoCompleteValidation(autoCompleteCharacter, minLength, GwtTextBoxAutoCompleteValidation.AUTO_COMPLETE_RIGHT) );
	}


	/**
	 * Informa si las validaciones cargadas al textBox se encuentran habilitadas
	 */
	public boolean isValidationsEnabled() {
		return validationsEnabled;
	}

	/**
	 * Activa o desactiva momentaneamente las reglas de validacion agregadas al
	 * TextBox.
	 */
	public void setValidationsEnabled(boolean validationsEnabled) {
		this.validationsEnabled = validationsEnabled;
	}

	public void enablePopup(Boolean flag){
		this.popUpEnabled  = flag;
	}
	public void resetWidget(){
		super.resetWidget();
		this.setStyleName( CSS_REQUIRED_OK );
	}

	
	public void setNextFocus(LnwWidget widget)
	{
		nextFocus = widget;
	}

	public Integer getInt() {
		try
		{
			return Integer.parseInt(this.getValue());
		}
		catch(RuntimeException ex)
		{
			return Integer.MIN_VALUE;			
		}
		
	}

	public Long getLong() {
		try
		{
			return Long.parseLong(this.getValue());
		}
		catch(RuntimeException ex)
		{
			return null;			
		}
	}

	
}
