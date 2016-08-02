package net.latin.client.widget.listener;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

/**
 * Handler simple de ENTER en KeyPress
 *
 */
public abstract class EnterKeyPressHandler implements KeyPressHandler{

	public void onKeyPress(KeyPressEvent event) {
		if(event.getCharCode() == KeyCodes.KEY_ENTER){
			accionEnter(event);
		}
	}

	protected abstract void accionEnter(KeyPressEvent event);

}
