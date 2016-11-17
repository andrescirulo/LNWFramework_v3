package net.latin.client.widget.list;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyPressEvent;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.html.Option;
import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.listener.EnterKeyPressHandler;

/**
 * ComboBox that holds a reference to object which represent each text shown in
 * the list
 * 
 */
public class GwtComboBox<T> extends MaterialComboBox<T> implements LnwWidget {
	
	private LnwWidget nextFocus;
	private GwtListBoxAdapter<T> adapter;
	
	public GwtComboBox() {
		addDomHandler(new EnterKeyPressHandler(){
			protected void accionEnter(KeyPressEvent event) {
				if (nextFocus != null) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							nextFocus.setFocus();
						}
					});
				}
			}} , KeyPressEvent.getType());
	}

	public void resetWidget() {
		this.setValue(null);
	}

	public void setFocus() {
		this.setFocus(true);
	}
	
	public void setAdapter(GwtListBoxAdapter<T> adapter) {
		this.adapter = adapter;
	}

	
	/*
	 * El valor de retorno es siempre null
	 * Está puesto solo para poder hacer override
	 * */
	@Override
	public Option addItem(T value) {
		super.addItem(adapter.getListBoxDescription(value),value);
		return null;
	}
}
