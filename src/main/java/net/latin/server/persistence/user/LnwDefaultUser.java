package net.latin.server.persistence.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.latin.client.widget.base.CustomBean;

/**
 * <p>
 * Implementación default del usuario de sistema.
 *
 * <p>
 * Extender esta clase para agregar más datos por usuario y
 * luego configurarla en el <code>GeneralConfig.xml</code>
 *
 * @author Matias Leone
 */
public class LnwDefaultUser implements LnwUser,Serializable {

	protected final Map<String,String> accessMap = new HashMap<String, String>();
	protected Long id;
	protected Long perfilId;
	protected Long nodo;
	protected String categoriaDescripcion;
	protected String perfilDescripcion;
	protected String nombreCompleto;
	protected Long categoriaId;
	protected String password;
	protected String userName;
	protected CustomBean customProperties;

	public LnwDefaultUser() {
	}

	public LnwDefaultUser(Long id, Long perfilId) {
		this.id = id;
		this.perfilId = perfilId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Override
	public void addAccess(String accessId) {

		accessMap.put(accessId, accessId);
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Long getPerfilId() {
		return perfilId;
	}

	@Override
	public boolean hasAccess(String accessId) {
		return accessMap.containsKey(accessId);
	}



	@Override
	public void setId( Long id ) {
		this.id = id;
	}

	@Override
	public void setPerfilId( Long id ) {
		this.perfilId = id;
	}


	@Override
	public Long getNodo() {
		return nodo;
	}


	@Override
	public void setNodo(Long nodo) {
		this.nodo = nodo;
	}


	@Override
	public String getPerfilDescripcion() {
		return perfilDescripcion;
	}


	@Override
	public void setPerfilDescripcion(String perfilDescripcion) {
		this.perfilDescripcion = perfilDescripcion;
	}


	@Override
	public String getCategoriaDescripcion() {
		return categoriaDescripcion;
	}


	@Override
	public void setCategoriaDescripcion(String categoriaDescripcion) {
		this.categoriaDescripcion = categoriaDescripcion;
	}

	@Override
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	@Override
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}



	public Map<String, String> getAccessMap() {
		return accessMap;
	}


	@Override
	public Long getCategoriaId() {

		return categoriaId;
	}

	@Override
	public void setCategoriaId(Long idCategoria) {
		this.categoriaId = idCategoria;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String pass) {
		this.password = pass;
	}

	/**
	 * @return the customProperties
	 */
	public CustomBean getCustomProperties() {
		if(customProperties == null) customProperties = new CustomBean();
		return customProperties;
	}

	@Override
	public String toString() {
		return this.userName+"@" +this.perfilId +" [ " + this.nombreCompleto +"@" +this.perfilDescripcion +" ]";
	}

	@Override
	public void clearAccess() {
		this.getAccessMap().clear();
	}
}
