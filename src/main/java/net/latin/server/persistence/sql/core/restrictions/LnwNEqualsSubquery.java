package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Funciona como un  not equals de sql contra un subquery
 * Ej. Columna != subquery
 *
 * @author Santiago Aimetta
 */
public class LnwNEqualsSubquery extends LnwRightOperandSubquery {

	public LnwNEqualsSubquery(){
	}

	public LnwNEqualsSubquery(String column, LnwQuery subquery) {
		super(column, subquery);
	}

	protected String getOperator() {
		return "!=";
	}
}
