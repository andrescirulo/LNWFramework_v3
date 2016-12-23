package net.latin.client.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import net.latin.client.validation.target.DoubleDateTarget;
import net.latin.client.validation.target.DoubleStringTarget;
import net.latin.client.validation.target.GwtRadioButtonValidationTarget;
import net.latin.client.validation.target.GwtValidateTextAreaTarget;
import net.latin.client.validation.target.ListBoxValidationTarget;
import net.latin.client.validation.target.MultipleStringTarget;
import net.latin.client.validation.target.TextBoxBaseValidationTarget;
import net.latin.client.validation.target.ValidationTarget;
import net.latin.client.validation.target.ValidationTargetImpl;
import net.latin.client.validation.target.ValidationTargetListener;
import net.latin.client.widget.date.GwtDatePicker;
import net.latin.client.widget.msg.GwtMsgHandler;
import net.latin.client.widget.radioButton.GwtRadioButton;
import net.latin.client.widget.textArea.GwtValidateTextArea;
import net.latin.client.widget.textBox.GwtTextBox;

/**
 * Tool for creating validation rules for targets
 * 
 * @author Matias Leone
 * 
 */
public class GwtValidator {
	
	protected List<ValidationTarget> targets;
	private List<String> errors;
	
	// aux
	private TextBox textBoxAux;
	
	/**
	 * Creates a new validator. It must be done before every session of
	 * validation
	 */
	public GwtValidator() {
		this.targets = new ArrayList<ValidationTarget>();
		this.errors = new ArrayList<String>();
	}
	
	public TextBoxBaseValidationTarget addTarget(String text) {
		textBoxAux = new TextBox();
		textBoxAux.setText(text);
		return this.addTarget(textBoxAux);
	}
	
	/**
	 * Add a new target to validate
	 * 
	 * @param widget
	 * @return
	 */
	public TextBoxBaseValidationTarget addTarget(TextBoxBase widget) {
		TextBoxBaseValidationTarget target = new TextBoxBaseValidationTarget(widget);
		this.targets.add(target);
		return target;
	}
	/**
	 * Add a new target to validate
	 * 
	 * @param widget
	 * @return
	 */
	public TextBoxBaseValidationTarget addTarget(GwtTextBox widget) {
		textBoxAux = new TextBox();
		textBoxAux.setText(widget.getText());
		return this.addTarget(textBoxAux);
	}
	
	/**
	 * Add a new target to validate
	 * 
	 * @param listBox
	 * @return
	 */
	public ListBoxValidationTarget addTarget(ListBox listBox) {
		ListBoxValidationTarget target = new ListBoxValidationTarget(listBox);
		this.targets.add(target);
		return target;
	}
	
	/**
	 * Add a new target to validate
	 * 
	 * @param widget
	 * @return
	 */
	public TextBoxBaseValidationTarget addTarget(GwtDatePicker widget) {
		textBoxAux = new TextBox();
		textBoxAux.setText(GwtValidationUtils.getDate(widget.getDate()));
		return this.addTarget(textBoxAux);
	}
	
	/**
	 * Add a new target to validate
	 * 
	 * @return
	 */
	public DoubleStringTarget addTarget(String text1, String text2) {
		DoubleStringTarget target = new DoubleStringTarget(text1, text2);
		this.targets.add(target);
		return target;
	}
	
	/**
	 * Add a new target dates to validate
	 * 
	 * @return
	 */
	public DoubleDateTarget addTarget(GwtDatePicker date1, GwtDatePicker date2) {
		DoubleDateTarget target = new DoubleDateTarget(date1, date2);
		this.targets.add(target);
		return target;
	}
	
	/**
	 * Add a new target to validate
	 * 
	 * @return
	 */
	public MultipleStringTarget addTarget(String[] texts) {
		MultipleStringTarget target = new MultipleStringTarget(texts);
		this.targets.add(target);
		return target;
	}
	
	/**
	 * Executes all validations
	 * 
	 * @return the result of the validation. If one or more failed, then it
	 *         returns false.
	 */
	public boolean validateAll() {
		ValidationTarget target;
		for (int i = 0; i < this.targets.size(); i++) {
			target = (ValidationTarget) this.targets.get(i);
			target.fillWithErrors(this.errors);
		}
		if (this.errors.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Executes all validations and show the error in the page
	 * 
	 * @return the result of the validation. If one or more failed, then it
	 *         returns false.
	 */
	public boolean validateAll(GwtMsgHandler listener) {
		boolean result = this.validateAll();
		listener.addAllErrorMessages(this.errors);
		return result;
	}
	
	/**
	 * Executes all validations and fills a GwtValidationResult object with the
	 * validations results
	 * 
	 * @return a GwtValidationResult object with the validations results
	 */
	public GwtValidationResult fillResult() {
		GwtValidationResult result = new GwtValidationResult();
		result.setValid(this.validateAll());
		result.setErrors(this.errors);
		return result;
	}
	
	/**
	 * List of errors. It must be asked after executing validateAll()
	 * 
	 * @return list of errors. It could be empty.
	 */
	public List<String> getErrors() {
		return this.errors;
	}
	
	/**
	 * Add a new error to the errors list
	 * 
	 * @param errorText
	 */
	public void addError(String errorText) {
		this.errors.add(errorText);
	}
	
	/**
	 * Add a list of errors to the errors list
	 * 
	 * @param errors
	 */
	public void addErrors(List<String> errors) {
		this.errors.addAll(errors);
	}
	
	/**
	 * Append a validation result to this validator
	 * 
	 * @param result
	 */
	public void addResult(GwtValidationResult result) {
		if (!result.isValid()) {
			this.errors.addAll(result.getErrors());
		}
	}
	
	
	public void removeAllTargets() {
		this.targets.clear();
	}
	
	public GwtRadioButtonValidationTarget addTarget(GwtRadioButton gwtRadioButton) {
		GwtRadioButtonValidationTarget target = new GwtRadioButtonValidationTarget(gwtRadioButton);
		this.targets.add(target);
		return target;
	}
	
	public GwtValidateTextAreaTarget addTarget(GwtValidateTextArea validateTextArea) {
		GwtValidateTextAreaTarget target = new GwtValidateTextAreaTarget(validateTextArea);
		this.targets.add(target);
		return target;
		
	}
	
	public ValidationTargetImpl addTarget(ValidationTargetListener listener) {
		ValidationTargetImpl target = new ValidationTargetImpl(listener);
		this.targets.add(target);
		return target;
	}
	
}
