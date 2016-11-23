package net.latin.client.widget.date;

import gwt.material.design.addins.client.timepicker.MaterialTimePicker;
import net.latin.client.widget.base.LnwWidget;

public class GwtTimePicker extends MaterialTimePicker implements LnwWidget {

	public GwtTimePicker() {
		setHour24(true);
		setDetectOrientation(true);
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
