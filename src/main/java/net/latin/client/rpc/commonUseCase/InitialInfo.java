package net.latin.client.rpc.commonUseCase;

import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.base.GwtBusinessObject;
import net.latin.client.widget.menu.data.MenuItem;


/**
 * Informacion general de un usuario en particular, que llega a la parte cliente
 * de la aplicacion. Contiene las entradas de menu.
 *
 * <br>
 * Se pueden agregar mas datos en el atributo <code>additionalInfo</code>
 *
 */
public class InitialInfo extends GwtBusinessObject {

	private String aplicacionDescripcion;
	private String categoriaDescripcion;
	private String perfilDescripcion;
	private String nombreCompletoUsuario;
	private MenuItem menu;
	private String sessionIdSau;
	private String sauUrl;

	/**
	 * Informacion adicional customizable
	 */
	private CustomBean additionalInfo;

	public String getCategoriaDescripcion() {
		return categoriaDescripcion;
	}

	public void setCategoriaDescripcion(String categoriaDescripcion) {
		this.categoriaDescripcion = categoriaDescripcion;
	}

	public String getPerfilDescripcion() {
		return perfilDescripcion;
	}

	public void setPerfilDescripcion(String perfilDescripcion) {
		this.perfilDescripcion = perfilDescripcion;
	}

	public MenuItem getMenu() {
		return menu;
	}

	public void setMenu(MenuItem menu) {
		this.menu = menu;
	}

	public String getNombreCompletoUsuario() {
		return nombreCompletoUsuario;
	}

	public void setNombreCompletoUsuario(String nombreCompletoUsuario) {
		this.nombreCompletoUsuario = nombreCompletoUsuario;
	}

	public CustomBean getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(CustomBean info) {
		this.additionalInfo = info;
	}

	public String getSessionIdSau() {
		return sessionIdSau;
	}

	public void setSessionIdSau(String sessionIdSau) {
		this.sessionIdSau = sessionIdSau;
	}

	public String getSauUrl() {
		return sauUrl;
	}

	public void setSauUrl(String sauUrl) {
		this.sauUrl = sauUrl;
	}

	public String getAplicacionDescripcion() {
		return aplicacionDescripcion;
	}

	public void setAplicacionDescripcion(String aplicacionDescripcion) {
		this.aplicacionDescripcion = aplicacionDescripcion;
	}

}
