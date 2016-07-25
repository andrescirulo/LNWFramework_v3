package net.latin.server.persistence.sql.core;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Sql Stored Procedure.
 *
 * Ejemplo: call storedProcedure( param1, param2 )
 *
 * @author Matias Leone
 */
public interface LnwStoredProcedure extends IsSerializable {

	public List getValues();

	public String getProcedure();

	/**
	 * Setea el nombre de la funcion a llamar
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwStoredProcedure setProcedure( String procedure );

	/**
	 * Agrega un parametro al procedimiento
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwStoredProcedure addValue( Object value );

	/**
	 * Agrega un parametro del tipo coleccion al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addCollection( String collectionType, Collection collection );

	/**
	 * Agrega un parametro del tipo array al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addArray( String collectionType, Object[] array );

	/**
	 * Agrega un parametro Custom Type al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addCustomType( LnwCustomType customType ) ;

	/**
	 * Agrega una lista de parametros Custom Type al procedimiento
	 * @param mapper
	 * @return
	 */
	public LnwStoredProcedure addCustomTypeList( String collectionType, String objectType, Collection collection, LnwCustomTypeMapper mapper ) ;
}
