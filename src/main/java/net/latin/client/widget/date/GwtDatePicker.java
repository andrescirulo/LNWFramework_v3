package net.latin.client.widget.date;

import java.util.Date;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;

import gwt.material.design.client.constants.DatePickerLanguage;
import gwt.material.design.client.ui.MaterialDatePicker;
import net.latin.client.validation.GwtValidationUtils;
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
	
	/**
	 * Retorna la fecha en un string con el formado yyyymmdd, si no es valida la
	 * fecha devuelve null
	 *
	 * @return
	 */
	public String getStringDate() {
		String[] dateArray = GwtValidationUtils.getDateArray(getDate(), SEPARATOR);
		if (dateArray==null){
			return null;
		}
		return dateArray[2]+dateArray[1]+dateArray[0];
	}

	
	@SuppressWarnings("deprecation")
	public void setStringDate(String date) {
		if (date == null) {
			resetWidget();
		} else {
			int dia=Integer.parseInt(date.substring(6, 8));
			int mes=Integer.parseInt(date.substring(4, 6))-1;
			int anio=Integer.parseInt(date.substring(0, 4))-1900;
			setDate(new Date(anio,mes,dia));
		}
	}
}
