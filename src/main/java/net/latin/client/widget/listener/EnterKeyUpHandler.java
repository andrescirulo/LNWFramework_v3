package net.latin.client.widget.listener;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

/**
 * Handler simple de ENTER en KeyPress
 *
 */
public abstract class EnterKeyUpHandler implements KeyUpHandler{

	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			accionEnter(event);
		}
	}

	protected abstract void accionEnter(KeyUpEvent event);

}
