package net.latin.client.widget.base;


/**
 * Interfaz para métodos generales a todos los widgets
 * del framework
 *
 * @author Matias Leone
 */
public interface LnwWidget {

	/**
	 * Limpia el contenido del widget.
	 * Cada widget sabe como limpiarse.
	 */
	public void resetWidget();

	/**
	 * Setea el foco en el widget.
	 * Cada widget sabe como hacerse focus
	 */
	public void setFocus();

}
