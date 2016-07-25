package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Not null contra una subquery
 * Ejemplo: select * from clientes where id IS NULL (select nombre from deudores);
 *
 * @author Matias Leone
 */
public class LnwIsNullSubquery extends LnwRightOperandSubquery {

	public LnwIsNullSubquery() {
	}

	/**
	 * Not null contra una subquery
	 * Ejemplo: select * from clientes where id IS NULL (select nombre from deudores);
	 */
	public LnwIsNullSubquery(LnwQuery subquery ) {
		super(null, subquery);
	}

	protected String getOperator() {
		return "IS NULL";
	}

}
