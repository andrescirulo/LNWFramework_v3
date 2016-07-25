package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Funciona como un  menor restricto de sql
 * Ej. Columna < Valor
 *
 * @author Santiago Aimetta
 */
public class LnwLtSubquery extends LnwRightOperandSubquery {

	public LnwLtSubquery() {
	}

	/**
	 * Funciona como un  menor restricto de sql
	 * Ej. Columna < (select)
	 *
	 */
	public LnwLtSubquery(String column, LnwQuery subquery ) {
		super(column, subquery);
	}

	protected String getOperator() {
		return "<";
	}
}

