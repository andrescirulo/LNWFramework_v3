package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Funciona como un  mayor equals de sql
 * Ej. Columna >= Valor
 *
 */
public class LnwGeSubquery extends LnwRightOperandSubquery {

	public LnwGeSubquery() {
	}

	/**
	 * Funciona como un  mayor equals de sql
	 * Ej. Columna >= Valor
	 *
	 */
	public LnwGeSubquery(String column, LnwQuery subquery) {
		super(column, subquery);
	}

	protected String getOperator() {
		return ">=";
	}
}
