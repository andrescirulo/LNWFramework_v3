package net.latin.server.security.config;

import net.latin.client.widget.menu.data.LeafMenuItem;


/**
 * Interfaz para modulos internos y externos, para usar en menu
 *
 */
public interface LnwMenuItem {
	/**
	 * Crea un ítem de menu widget
	 *
	 */
	LeafMenuItem buildGwtMenu();
	String getId();
	String getTitle();
}
