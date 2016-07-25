package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * Like Verifica si dos caracteres son coincidentes Ej. columna Like "djskdj"
 * <--Valor
 *
 * @author Santiago Aimetta
 */
public class LnwLike implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor = null;

	public LnwLike() {
	}

	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwLike(String columna, Object valor) {
		super();
		this.columna = columna;
		this.valor = LnwSqlParameter.adaptData(valor);
	}

	public String build(List<Object> parametros) {
		parametros.add(LnwSqlParameter.recoverData(valor));

		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " LIKE ?" );
		return buffer.toString();
	}

}
