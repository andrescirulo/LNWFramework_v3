package net.latin.client.widget.date;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;

import gwt.material.design.client.constants.DatePickerLanguage;
import gwt.material.design.client.ui.MaterialDatePicker;
import net.latin.client.widget.base.LnwWidget;

public class GwtDatePicker extends MaterialDatePicker implements LnwWidget {

	public static final String SEPARATOR = "/";
	private boolean inicializado=false;
	public GwtDatePicker() {
		inicializado=false;
		setDetectOrientation(true);
		addAttachHandler(new Handler() {
			public void onAttachOrDetach(AttachEvent event) {
				if (!inicializado){
					setLanguage(DatePickerLanguage.ES);
					reinitialize();
					inicializado=true;
				}
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
