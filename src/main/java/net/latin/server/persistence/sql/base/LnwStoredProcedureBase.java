package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwCustomType;
import net.latin.server.persistence.sql.core.LnwCustomTypeMapper;
import net.latin.server.persistence.sql.core.LnwStoredProcedure;
import net.latin.server.persistence.sql.core.LnwStoredProcedureParameter;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;

/**
 * Sql Stored Procedure.
 *
 * Ejemplo: call storedProcedure( param1, param2 )
 *
 * Siempre que se extienda de esta clase, ver configuracion LnwSpringStoredProcedure
 * en el proyecto LNWFramework
 *
 * @author Matias Leone
 */
public class LnwStoredProcedureBase implements LnwStoredProcedure {

	private String procedure = "";
	private List<LnwStoredProcedureParameter> values = new ArrayList<LnwStoredProcedureParameter>();

	/**
	 * Necesario por motivos de serializacion.
	 * No utilizar.
	 */
	public LnwStoredProcedureBase() {
	}

	/**
	 * Setea el nombre de la funcion a llamar
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwStoredProcedure setProcedure( String procedure ){
		this.procedure = procedure;
		return this;
	}

	/**
	 * Agrega un parametro al procedimiento
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwStoredProcedure addValue( Object value ){
		if(value == null) {
			values.add(null);
		} else {
			values.add( new LnwStoredProcedureParameter(value) );
		}
		return this;
	}

	/**
	 * Agrega un parametro del tipo coleccion al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addCollection( String collectionType, Collection collection ) {
		if(collection == null) {
			values.add( LnwStoredProcedureParameter.buildForArray( new ArrayList(), collectionType ) );
		} else {
			values.add( LnwStoredProcedureParameter.buildForArray( collection, collectionType ) );
		}
		return this;
	}

	/**
	 * Agrega un parametro del tipo array al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addArray( String collectionType, Object[] array ) {
		List<Object> list = new ArrayList<Object>();
		if( array == null ) {
			return addCollection(collectionType, list );
		} else {
			for (int i = 0; i < array.length; i++) {
				list.add( array[i] );
			}
			return addCollection(collectionType, list);
		}
	}

	/**
	 * Agrega un parametro Custom Type al procedimiento
	 * @param collection
	 * @return
	 */
	public LnwStoredProcedure addCustomType( LnwCustomType customType ) {
		if(customType == null) {
			throw new LnwSqlBadStatementException( "No puede haber un custom type null. Procedure: " + procedure );
		}
		return addValue(customType);
	}

	/**
	 * Agrega una lista de parametros Custom Type al procedimiento
	 * @param mapper
	 * @return
	 */
	public LnwStoredProcedure addCustomTypeList( String collectionType, String objectType, Collection collection, LnwCustomTypeMapper mapper ) {
		List<LnwCustomType> customTypes = new ArrayList<LnwCustomType>();
		if(collection == null) {
			values.add( LnwStoredProcedureParameter.buildForCustomTypeList(customTypes, collectionType) );
			return this;

		} else {
			for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
				Object objData = iterator.next();
				LnwCustomType customType = new LnwCustomType(objectType);
				mapper.mapCustomType( customType, objData );
				customTypes.add( customType );
			}
			values.add( LnwStoredProcedureParameter.buildForCustomTypeList(customTypes, collectionType) );

			return this;
		}

	}

	public List getValues() {
		return values;
	}

	public String getProcedure() {
		return procedure;
	}

}
