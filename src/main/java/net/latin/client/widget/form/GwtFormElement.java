package net.latin.client.widget.form;

import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.panels.GwtHorizontalPanel;

/**
 * Representa un elemento para ser agregado al GwtForm
 *
 */
public interface GwtFormElement extends LnwWidget {

	/**
	 * @return Panel que engloba a todo el elemento
	 */
	public GwtHorizontalPanel getElementPanel();

	/**
	 * @return ID única del elemento
	 */
	public String getElementId();

	/**
	 * Construye el elemento
	 */
	public void buildElement(GwtForm form);

	/**
	 * Muestra u oculta el elemento
	 */
	public void setVisible(boolean flag);

	/**
	 * Informa si el elemento se encuentra visible
	 */
	public boolean isVisible();

}
