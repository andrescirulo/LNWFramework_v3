package net.latin.client.widget.date;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;

import gwt.material.design.client.constants.DatePickerLanguage;
import gwt.material.design.client.ui.MaterialDatePicker;
import net.latin.client.widget.base.LnwWidget;

public class GwtDatePicker extends MaterialDatePicker implements LnwWidget {

	public GwtDatePicker() {
//		setI18n(getLocale());
		
		addAttachHandler(new Handler() {
			public void onAttachOrDetach(AttachEvent event) {
				setLanguage(DatePickerLanguage.ES);
				reinitialize();
			}
		});
	}
	
	@Override
	public void resetWidget() {
		clear();
	}

	@Override
	public void setFocus() {
		setFocus(true);
	}

}
