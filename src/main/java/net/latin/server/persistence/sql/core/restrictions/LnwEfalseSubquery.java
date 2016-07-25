package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;


/**
 * Funciona como un equals false de sql, contra un Subquery
 * Ej. 1 != (select ...)
 *
 * @author Matias Leone
 */
public class LnwEfalseSubquery extends LnwRightOperandSubquery{

	//Metodo requerido por ser serializable
	public LnwEfalseSubquery() {
	}

	public LnwEfalseSubquery(LnwQuery subquery) {
		super(null, subquery);
	}

	protected String getOperator() {
		return "1 !=";
	}

}
