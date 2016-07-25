package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * Funciona como un  not equals de sql
 * Ej. Columna != Valor
 *
 * @author Santiago Aimetta
 */
public class LnwNEquals implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor = null;

	public LnwNEquals() {
	}

	public LnwNEquals(String column, Object value ) {
		columna = column;
		valor = LnwSqlParameter.adaptData(value);
	}

	public String build(List<Object> parametros) {
		parametros.add(LnwSqlParameter.recoverData(valor));

		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " != ?" );
		return buffer.toString();
	}
}
