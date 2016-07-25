package net.latin.server.persistence.sql.core.restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;

/**
 * Restriccion de texto libre para crear una condicion con el SQL que
 * sea necesario.
 * Solo utilizar en caso de que el resto de las restricciones no alcancen
 * para cumplir la consulta.
 * El texto debe ser debe pasar los parametros del SQL con signos de pregunta.
 * Ej: funcionX(?,?) > 5
 *
 * @author Matias Leone
 */
public class LnwQueryText implements LnwRestriction {

	private String sqlText;
	private List<LnwSqlParameter> values = new ArrayList<LnwSqlParameter>();

	public LnwQueryText() {
	}

	public LnwQueryText(String sqlText) {
		setText(sqlText);
	}

	/**
	 * Texto SQL de la restriccion. Debe pasar los parametros con signos de pregunta
	 */
	public LnwQueryText setText(String sqlText) {
		this.sqlText = sqlText;
		return this;
	}

	/**
	 * Parametro del SQL. Debe coincidir con un signo de pregunta del SQL.
	 * Se deben agregar en el orden correcto
	 */
	public LnwQueryText addParam(Object param) {
		values.add( LnwSqlParameter.adaptData(param) );
		return this;
	}

	/**
	 * Parametros del SQL. Deben coincidir con los signos de pregunta del SQL.
	 * Se deben agregar en el orden correcto
	 */
	public void addParams(Object[] params) {
		for (int i = 0; i < params.length; i++) {
			addParam(params[i]);
		}
	}

	/**
	 * Parametros del SQL. Deben coincidir con los signos de pregunta del SQL.
	 * Se deben agregar en el orden correcto
	 */
	public void addParams(Collection params) {
		for (Iterator iterator = params.iterator(); iterator.hasNext();) {
			Object param = (Object) iterator.next();
			addParam(param);
		}
	}


	public String build(List<Object> parametros) {
		parametros.addAll( LnwSqlParameter.recoverData(values) );
		return sqlText;
	}

}
