package net.latin.server.persistence.user;

import net.latin.client.widget.base.CustomBean;

/**
 * <p>
 * Intefaz del usuario de sistema utilizado por el framework.
 * Participa en la comprobación de permisos y accesos en una aplicacion.
 * Contiene los datos que se quieren mantener en memoria (session) por cada
 * usuario.
 *
 * <p>
 * La implementación default es <code>LnwDefaultUser</code>
 *
 * <p>
 * La implementación de usuario de cada sistema se puede configurar
 * en <code>GeneralConfig.xml</code>
 *
 * @author Matias Leone
 */
public interface LnwUser {

	/**
	 * @return id del usuario
	 */
	public Long getId();

	/**
	 * Setea la id del usuario
	 */
	public void setId( Long id );

	/**
	 * @return id del perfil de usuario
	 */
	public Long getPerfilId();

	/**
	 * Setea la id del perfil de usuario
	 */
	public void setPerfilId( Long id );

	/**
	 * Agrega un nuevo acceso permitido al usuario
	 */
	public void addAccess(String accessId);

	/**
	 * Informa si el usuario puede utilizar o no un acceso determinado
	 */
	public boolean hasAccess(String accessId);

	/**
	 * Setea el nodo al cual pertenece el usuario
	 */
	public void setNodo(Long nodo);

	/**
	 * @return nodo del usuario
	 */
	public Long getNodo();


	/****************
	 *metodos usuados
	 *para el menu
	 ****************/
	/**
	 * Setea la descripcion de la categoria del usuario
	 * @param descripcion
	 */
	public void setCategoriaDescripcion(String descripcion);

	/**
	 * Setea la descripcion del perfil del usuario
	 * @param perfilDescripcion
	 */
	public void setPerfilDescripcion(String descripcion);

	/**
	 * Obtiene la descripcion de la categoria del usuario
	 * @param descripcion
	 */
	public String getCategoriaDescripcion();

	/**
	 * Obtiene la descripcion de la categoria del usuario
	 * @param descripcion
	 */
	public String getPerfilDescripcion();

	/**
	 * Setea el nombre completo del usuario
	 * @param
	 */
	public void setNombreCompleto(String nombreCompleto);

	/**
	 * Obtiene el nombre completo del usuario
	 *
	 */
	public String getNombreCompleto();

	/**
	 * setea el id de la categoria
	 */
	public void setCategoriaId(Long id);

	/**
	 * Obtiene el valor de la categoria
	 * @return valor de la categoria
	 */
	public Long getCategoriaId();
	/**
	 * setea el valor del password
	 * @param valor del password
	 */
	public void setPassword(String pass);
	/**
	 * Obtiene el valor del password
	 * @return valor del password
	 */
	public String getPassword();

	public void setUserName(String userName);

	public String getUserName();

	/**
	 * @return CustomBean con propiedades propias de cada aplicacion
	 */
	public CustomBean getCustomProperties();

	/**
	 * Borrar los permisos de acceso del usuario
	 */
	public void clearAccess();
}
