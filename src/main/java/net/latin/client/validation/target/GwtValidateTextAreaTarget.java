package net.latin.client.validation.target;

import net.latin.client.validation.GwtValidationUtils;
import net.latin.client.widget.textArea.GwtValidateTextArea;

public class GwtValidateTextAreaTarget extends ValidationTarget {

	private GwtValidateTextArea textArea;

	public GwtValidateTextAreaTarget(GwtValidateTextArea textArea) {

		this.textArea = textArea;
	}

	public GwtValidateTextAreaTarget required(String errorMsg) {

		if (isOk()) {
			if (textArea.getText().equals("")) {
				this.errorMsg = errorMsg;
			}
		}
		return this;

	}

	/**
	 * Marca que todas las validaciones que vengan después de esto, solo se validen
	 * si el campo es requerido
	 */
	public GwtValidateTextAreaTarget conditionalRequired() {
		if( isOk() ) {
			if( !GwtValidationUtils.validateRequired( textArea.getText() ) ) {
				this.conditionalState = true;
			}
		}
		return this;
	}
	
	public GwtValidateTextAreaTarget maxLength(int maxLength, String errorMsg) {

		if (isOk()) {
			if (textArea.getLength() > maxLength) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	public GwtValidateTextAreaTarget maxWordLength(int maxWordLength, String errorMsg) {

		if (isOk()) {
			String[] words = textArea.getText().split("\\s");
			for (String word : words) {
				if (word.length() > maxWordLength) {
					this.errorMsg = errorMsg;
					break;
				}
			}
		}
		return this;

	}

	public GwtValidateTextAreaTarget maxLines(int maxLines, String errorMsg) {

		if (isOk()) {
			if (textArea.getCantidadDeLineas() > maxLines) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

}
