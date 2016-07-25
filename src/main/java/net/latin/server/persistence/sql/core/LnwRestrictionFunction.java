package net.latin.server.persistence.sql.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Llamada a una funcion en el where
 *
 * @author Matias Leone
 */
public class LnwRestrictionFunction implements LnwRestriction {

	private String function;
	private List<LnwSqlParameter> values = new ArrayList<LnwSqlParameter>();

	public LnwRestrictionFunction() {
	}

	/**
	 * Crea una llamada a una funcion en el where
	 * @param functionName nombre de la funcion
	 */
	public LnwRestrictionFunction( String functionName ) {
		function = functionName;
	}

	/**
	 * Agrega un parametro a la funcion
	 * @param param valor del parametro
	 */
	public LnwRestrictionFunction addParam( Object param ) {
		values.add( LnwSqlParameter.adaptData(param) );
		return this;
	}

	public String build(List<Object> parametros) {
		parametros.addAll( LnwSqlParameter.recoverData(values) );

		final StringBuffer buffer = new StringBuffer();
		buffer.append( function )
			.append( "(" );

		for (int i = 0; i < values.size(); i++) {
			buffer.append( " ?," );
		}

		buffer
			.deleteCharAt( buffer.length() - 1 )
			.append( ") " );
		return buffer.toString();
	}

}
