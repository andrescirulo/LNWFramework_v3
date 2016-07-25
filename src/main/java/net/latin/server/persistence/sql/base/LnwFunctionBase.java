package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwCustomType;
import net.latin.server.persistence.sql.core.LnwCustomTypeMapper;
import net.latin.server.persistence.sql.core.LnwFunction;
import net.latin.server.persistence.sql.core.LnwStoredProcedureParameter;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;


/**
 * Sql Function Base
 *
 * Ejemplo: select funcion(param1,param2) from dual;
 *
 * Tiene las validaciones obligatorias, ej. tabla obligatoria,
 * insertar por lo menos un valor.
 *
 */
public class LnwFunctionBase  implements LnwFunction {

	protected final static String SELECT = " SELECT ";

	protected String procedure = "";
	protected List<LnwStoredProcedureParameter> values = new ArrayList<LnwStoredProcedureParameter>();

	/**
	 * Necesario por motivos de serializacion.
	 * No utilizar.
	 */
	public LnwFunctionBase() {
	}

	/**
	 * Setea el nombre de la funcion a llamar
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwFunction setFunction( String function ){
		this.procedure = function;
		return this;
	}

	/**
	 * Agrega un parametro a la funcion
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwFunction addValue( Object value ){
		if(value == null) {
			values.add(null);
		} else {
			values.add( new LnwStoredProcedureParameter(value) );
		}
		return this;
	}

	/**
	 * Agrega un parametro del tipo coleccion a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addCollection( String collectionType, Collection collection ) {
		if(collection == null) {
			values.add( LnwStoredProcedureParameter.buildForArray( new ArrayList(), collectionType ) );
		} else {
			values.add( LnwStoredProcedureParameter.buildForArray( collection, collectionType ) );
		}
		return this;
	}

	/**
	 * Agrega un parametro del tipo array a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addArray( String collectionType, Object[] array ) {
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
	 * Agrega un parametro Custom Type a la funcion
	 * @param collection
	 * @return
	 */
	public LnwFunction addCustomType( LnwCustomType customType ) {
		if(customType == null) {
			throw new LnwSqlBadStatementException( "No puede haber un custom type null. LnwFunction: " + procedure );
		}
		return addValue(customType);
	}

	/**
	 * Agrega una lista de parametros Custom Type a la funcion
	 * @param mapper
	 * @return
	 */
	public LnwFunction addCustomTypeList( String collectionType, String objectType, Collection collection, LnwCustomTypeMapper mapper ) {
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

	/**
	 * Construye la funcion
	 * @return sentencia lista para ser ejecutada
	 */
	public PreparedStatement buildFunction(){
		final List<Object> finalValues = new ArrayList<Object>();

		StringBuffer bufferStoredProcedure = new StringBuffer();
		armarLLamada(bufferStoredProcedure);

		//armo los parametros
		StringBuffer bufferParamentros = new StringBuffer();

		//sin parametros
		if( this.values.size() == 0 ) {
			bufferParamentros.append( "()" );

		//con parametros
		} else {
			armarParametros(finalValues, bufferParamentros);
		}

		//concatenar parametros
		bufferStoredProcedure.append( bufferParamentros.toString() );

		armarFrom(bufferStoredProcedure);

		//crear statement
		final PreparedStatement pStatement = new PreparedStatement(bufferStoredProcedure.toString(),finalValues);
		this.validate();
		return pStatement;
	}

	protected void armarParametros(final List<Object> finalValues,
			StringBuffer bufferParamentros) {
		bufferParamentros.append( "(" );

		LnwStoredProcedureParameter value;
		for (Iterator itValues = this.values.iterator(); itValues.hasNext();) {
			value = (LnwStoredProcedureParameter)itValues.next();

			//si es null, es un parametro mas pero en null
			if( value == null ) {
				bufferParamentros.append( " ?," );
				finalValues.add( value );

			//ver si es una collection
			} else if( value.isCollection() ) {

				//completamos los parametros y los values por extension
				armarColeccion(finalValues, bufferParamentros, value, (Collection) value.getObject());

			//ver si es un Custom Type
			} else if( value.isCustomType() ) {
				final LnwCustomType customType = (LnwCustomType) value.getObject();
				bufferParamentros.append( " " );
				bufferParamentros.append( customType.getBuildParameters() );
				finalValues.addAll( customType.getBuildValues() );

			//ver si es una coleccion de Custom Type
			} else if( value.isCustomTypeCollection() ) {
				Collection collection = (Collection)value.getObject();
				bufferParamentros.append( " " );
				bufferParamentros.append( value.getCollectionType() );
				bufferParamentros.append( " (" );
				for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
					LnwCustomType customType = (LnwCustomType) iterator.next();
					bufferParamentros.append( " " );
					bufferParamentros.append( customType.getBuildParameters() );
					finalValues.addAll( customType.getBuildValues() );
				}
				if(collection.size() > 0) {
					//quito la última ,
					bufferParamentros.deleteCharAt( bufferParamentros.length() - 1 )
					.append( " )," );
				} else {
					bufferParamentros.append( " )," );
				}

			//si es un valor comun
			} else {
				bufferParamentros.append( " ?," );
				finalValues.add( value.getObject() );
			}

		}
		//quito la última ,
		bufferParamentros
			.deleteCharAt( bufferParamentros.length() - 1 )
			.append( ")" );
	}

	protected void armarColeccion(final List<Object> finalValues,
			StringBuffer bufferParamentros, LnwStoredProcedureParameter value,
			final Collection array) {
		bufferParamentros.append( " " );
		bufferParamentros.append( value.getCollectionType() );
		bufferParamentros.append( " (" );
		for (Iterator iterator = array.iterator(); iterator.hasNext(); ) {
			bufferParamentros.append( " ?," );
			finalValues.add( iterator.next() );
		}

		if(array.size() > 0) {
			//quito la última ,
			bufferParamentros.deleteCharAt( bufferParamentros.length() - 1 )
			.append( " )," );
		} else {
			bufferParamentros.append( " )," );
		}
	}

	protected void armarLLamada(StringBuffer bufferStoredProcedure) {
		bufferStoredProcedure.append( SELECT )
			.append( this.procedure );
	}

	/**
	 * Valida que el stored procedure este bien formado
	 */
	public void validate(){
		//si no puso el procedure
		if( this.procedure.length() == 0 ){
			throw new LnwSqlBadStatementException("LnwFunction invalido: falta nombre del procedure");
		}
	}

	/**
	 * Parsea un mensaje de error lanzado por una excepcion de SQL
	 */
	public static String parseErrorMsg( Exception e ) {
		final String original = e.getMessage();
		final int n1 = original.indexOf( "ERROR:" );
		final String aux = original.substring( n1 );
		final int n2 = aux.indexOf( ";" );
		return aux.substring( 0, n2 );
	}

	protected void armarFrom(StringBuffer bufferStoredProcedure) {
	}
}
