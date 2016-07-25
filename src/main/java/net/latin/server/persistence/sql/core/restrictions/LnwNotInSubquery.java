package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;
/**
 * Hace un "column NOT IN (sub select)".
 *
 * Ejemplo: select * from clientes where id NOT IN (select nombre from deudores);
 *
 * @author Matias Leone
 */
public class LnwNotInSubquery extends LnwRightOperandSubquery {

	public LnwNotInSubquery() {
	}

	/**
	 * Hace un "column IN (sub select)".
	 *
	 * Ejemplo: select * from clientes where id NOT IN (select nombre from deudores);
	 *
	 */
	public LnwNotInSubquery(String column, LnwQuery subquery ) {
		super(column, subquery);
	}

	protected String getOperator() {
		return "NOT IN";
	}

}
