package net.latin.server.persistence.sql.core.restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
/**
 * Hace un "column IN (value1,value2,value3)".
 *
 * Ejemplo: select * from clientes where id IN (15,233,42,233);
 *
 * @author Santiago Aimetta
 */
public class LnwInArray implements LnwRestriction {

	private String column;
	private List<LnwSqlParameter> values = new ArrayList<LnwSqlParameter>();

	public LnwInArray() {
	}

	/**
	 * Hace un "column IN (value1,value2,value3)".
     * Ejemplo: select * from clientes where id IN (15,233,42,233);
     * @param column columna a comparar en el IN
	 */
	public LnwInArray( String column ) {
		this.column = column;
	}

	/**
	 * Agrega un valor al array del IN
	 */
	public LnwInArray add( Object value ) {
		values.add( LnwSqlParameter.adaptData(value) );
		return this;
	}

	/**
	 * Agrega muchos valores al array del IN
	 */
	public LnwInArray addAll( Collection<Object> collection ) {
		values.addAll( LnwSqlParameter.adaptData(collection) );
		return this;
	}

	public String build(List<Object> parametros) {
		if( values.size() == 0) {
			return "";
		}

		parametros.addAll(LnwSqlParameter.recoverData(values));

		final StringBuffer buffer = new StringBuffer();
		buffer.append( " " );
		buffer.append( column );
		buffer.append( " IN (" );
		for (int i = 0; i < values.size(); i++) {
			buffer.append( " ?," );
		}
		buffer
			.deleteCharAt( buffer.length() - 1 )
			.append( ") " );
		return buffer.toString();
	}

}
