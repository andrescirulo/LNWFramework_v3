package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;
/**
 * Hace un "column IN (sub select)".
 *
 * Ejemplo: select * from clientes where id IN (select nombre from deudores);
 *
 * @author Matias Leone
 */
public class LnwInSubquery extends LnwRightOperandSubquery {


	public LnwInSubquery() {
	}

	/**
	 * Hace un "column IN (sub select)".
	 *
	 * Ejemplo: select * from clientes where id IN (select nombre from deudores);
	 *
	 */
	public LnwInSubquery(String column, LnwQuery subquery ) {
		super(column, subquery);
	}

	protected String getOperator() {
		return "IN";
	}

}
