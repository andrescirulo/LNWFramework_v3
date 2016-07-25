package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Funciona como un  mayor restricto de sql
 * Ej. Columna > Valor
 *
 */
public class LnwGtSubquery extends LnwRightOperandSubquery {

	//Metodo requerido por ser serializable
	public LnwGtSubquery() {
	}

	/**
	 * Funciona como un  mayor restricto de sql
	 * Ej. Columna > subquery
	 *
	 */
	public LnwGtSubquery(String column, LnwQuery subquery) {
		super(column, subquery);
	}

	protected String getOperator() {
		return ">";
	}
}


