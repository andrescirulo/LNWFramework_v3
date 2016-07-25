package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * ILike Verifica si una cadena no incluye a la otra
 * Ej. columna NOT Like "%djskdj%" <--Valor
 *
 * @author Santiago Aimetta
 */
public class LnwNILike implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor = null;

	public LnwNILike() {
	}

	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwNILike(String columna, Object valor) {
		super();
		this.columna = columna;
		this.valor = LnwSqlParameter.adaptData(valor);
	}

	public String build(List<Object> parametros) {
		parametros.add(LnwSqlParameter.recoverData(valor));

		StringBuffer bufferParametros = new StringBuffer();
		bufferParametros.append( "%" )
			.append( valor )
			.append( "%" );

		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append(" NOT LIKE ?" );
		return buffer.toString();
	}

}
