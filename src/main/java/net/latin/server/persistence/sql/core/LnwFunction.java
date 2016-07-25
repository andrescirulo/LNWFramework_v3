package net.latin.server.persistence.sql.core;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Interfaz para crear una funcion llamable
 * @author Fernando Diaz
 *
 */
public interface LnwFunction extends IsSerializable {


	/**
	 * Construye la funcion
	 * @return sentencia lista para ser ejecutada
	 */
	public PreparedStatement buildFunction();

	/**
	 * Setea el nombre de la funcion a llamar
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwFunction setFunction( String procedure );

	/**
	 * Agrega un parametro a la funcion
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwFunction addValue( Object value );

	/**
	 * Agrega un parametro del tipo coleccion a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addCollection( String collectionType, Collection collection );

	/**
	 * Agrega un parametro del tipo array a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addArray( String collectionType, Object[] array );

	/**
	 * Agrega un parametro Custom Type a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addCustomType( LnwCustomType customType );

	/**
	 * Agrega una lista de parametros Custom Type a la funcion
	 * @param mapper
	 * @return
	 */
	public LnwFunction addCustomTypeList( String collectionType, String objectType, Collection collection, LnwCustomTypeMapper mapper );

	/**
	 * Valida que el stored procedure este bien formado
	 */
	public void validate();
}


