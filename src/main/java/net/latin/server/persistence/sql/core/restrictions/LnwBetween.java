package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.PreparedStatement;
/**
 * Funciona como un between de sql
 * Ej. Columna BETWEEN Valor1 AND Valor2
 *
 * @author Santiago Aimetta
 */
public class LnwBetween implements LnwRestriction {

	private String columna = "";
	private LnwSqlParameter valor1 = null;
	private LnwSqlParameter valor2 = null;
	private LnwQuery subquery1 = null;
	private LnwQuery subquery2 = null;

	public LnwBetween() {
	}

	public LnwBetween(String column, Object value1, Object value2 ) {
		columna = column;
		this.valor1 = LnwSqlParameter.adaptData(value1);
		this.valor2 = LnwSqlParameter.adaptData(value2);
	}

	public LnwBetween(String column, Object value1, LnwQuery subquery ) {
		columna = column;
		this.valor1 = LnwSqlParameter.adaptData(value1);
		this.subquery2 = subquery;
	}

	public LnwBetween(String column, LnwQuery subquery, Object value ) {
		columna = column;
		this.subquery1 =subquery;
		this.valor2 = LnwSqlParameter.adaptData(value);
	}

	public LnwBetween(String column, LnwQuery subquery1, LnwQuery subquery2 ) {
		columna = column;
		this.subquery1 = subquery1;
		this.subquery2 = subquery2;
	}

	public String build(List<Object> parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " BETWEEN ")
			.append(getFirstParameter(parametros))
			.append("AND ")
			.append(getSecondParameter(parametros));
		return buffer.toString();
	}

	private Object getSecondParameter(List<Object> parametros) {
		return getParameter(parametros, subquery2, valor2);
	}

	private String getFirstParameter(List<Object> parametros) {
		return getParameter(parametros, subquery1, valor1);
	}

	private String getParameter(List<Object> parametros, LnwQuery sub, Object val) {
		if(sub != null) {
			//construir subQuery
			PreparedStatement buildQuery = sub.buildQuery();

			//cargar parámetros
			parametros.addAll( LnwSqlParameter.adaptData( buildQuery.getParams() ) );
			//cargar sentencia
			return ( " ( " + buildQuery.getSentencia() + " ) " );
		}else {
			parametros.add(LnwSqlParameter.recoverData(val));
			return " ? ";
		}
	}
}
