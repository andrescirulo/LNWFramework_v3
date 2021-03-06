package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * ILike Verifica si una cadena incluye a la otra Ej. columna Like "%djskdj%"
 * <--Valor
 *
 * @author Santiago Aimetta
 */
public class LnwIlike implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor = null;

	public LnwIlike() {
	}

	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwIlike(String columna, Object valor) {
		super();
		this.columna = columna;
		this.valor = LnwSqlParameter.adaptData(valor);
	}

	public String build(List<Object> parametros) {
		parametros.add(LnwSqlParameter.recoverData(valor));

		StringBuffer buffer = new StringBuffer();
		buffer.append( "lower(" )
			.append( columna )
			.append( ") LIKE lower(?)" );
		return buffer.toString();
	}

}
