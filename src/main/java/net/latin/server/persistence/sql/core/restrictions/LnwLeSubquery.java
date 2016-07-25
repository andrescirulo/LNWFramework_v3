package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Funciona como un  menor equals de sql
 * Ej. Columna <= (select)
 *
 */
public class LnwLeSubquery extends LnwRightOperandSubquery{

	//Metodo requerido por ser serializable
	public LnwLeSubquery() {
	}

	/**
	 * Funciona como un  menor equals de sql
	 * Ej. Columna <= Valor
	 *
	 * @author Santiago Aimetta
	 */
	public LnwLeSubquery(String column, LnwQuery subquery) {
		super(column, subquery);
	}

	protected String getOperator() {
	return "<=";
	}
}

