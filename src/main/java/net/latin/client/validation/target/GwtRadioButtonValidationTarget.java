package net.latin.client.validation.target;

import net.latin.client.widget.radioButton.GwtRadioButton;

public class GwtRadioButtonValidationTarget extends ValidationTarget {

	private GwtRadioButton radio;

	public GwtRadioButtonValidationTarget(GwtRadioButton gwtRadioButton) {
		this.radio = gwtRadioButton;
	}

	public GwtRadioButtonValidationTarget validElementSelected(String errorMsg) {
		if (isOk()) {
			if (radio.getSelectedChild() == null) {
				this.errorMsg = errorMsg;
			}
		}
		return this;

	}

}
