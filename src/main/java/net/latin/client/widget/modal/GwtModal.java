package net.latin.client.widget.modal;

import net.latin.client.widget.base.GwtController;

/**
 * Herramienta para bloquear o desbloquear la pantalla.
 *
 */
public class GwtModal {

	/**
	 * Generate a modal panel with a default message and disable all the widget behind
	 */
	public static void blockScreen() {
		GwtController.instance.blockScreen();
	}

	/**
	 * Generate a modal panel with a message and disable all the widget behind
	 */
	public static void blockScreen( String msg ) {
		GwtController.instance.blockScreen( msg );
	}

	/**
	 * Hide the modal panel
	 */
	public static void unblockScreen() {
		GwtController.instance.unblockScreen();
	}

	/**
	 * Setea el zIndex de la proxima vez que se muestra el Modal. Una vez que se usa se setea nuevamente en el valor por defecto
	 */
	public static void setNextModalZIndex(Integer zIndex){
		GwtController.instance.setNextModalZIndex(zIndex);
	}
}
