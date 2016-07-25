package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Not null contra una subquery
 * Ejemplo: select * from clientes where id IS NOT NULL (select nombre from deudores);
 *
 * @author Matias Leone
 */
public class LnwNotNullSubquery extends LnwRightOperandSubquery {

	public LnwNotNullSubquery() {
	}

	/**
	 * Not null contra una subquery
	 * Ejemplo: select * from clientes where id IS NOT NULL (select nombre from deudores);
	 */
	public LnwNotNullSubquery(LnwQuery subquery ) {
		super(null, subquery);
	}

	protected String getOperator() {
		return "IS NOT NULL";
	}

}
