package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * NOT LIKE
 * Verifica si dos caracteres son coincidentes
 * Ej. columna NOT Like "djskdj" <--Valor
 *
 * @author Santiago Aimetta
 */
public class LnwNLike implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor = null;

	public LnwNLike() {
	}

	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwNLike(String columna, Object valor) {
		super();
		this.columna = columna;
		this.valor = LnwSqlParameter.adaptData(valor);
	}

	public String build(List<Object> parametros) {
		parametros.add(LnwSqlParameter.recoverData(valor));

		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " NOT LIKE ?" );
		return buffer.toString();
	}

}
