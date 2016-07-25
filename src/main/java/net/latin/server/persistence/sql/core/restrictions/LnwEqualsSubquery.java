package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;


/**
 * Funciona como un equals de sql, contra un Subquery
 * Ej. Columna = (select ...)
 *
 * @author Matias Leone
 */
public class LnwEqualsSubquery extends LnwRightOperandSubquery{

	//Metodo requerido por ser serializable
	public LnwEqualsSubquery() {
	}

	public LnwEqualsSubquery(String column, LnwQuery subquery) {
		super(column, subquery);
	}

	protected String getOperator() {
		return "=";
	}

}
