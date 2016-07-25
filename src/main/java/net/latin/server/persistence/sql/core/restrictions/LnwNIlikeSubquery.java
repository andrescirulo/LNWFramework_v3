package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * NOT LIKE LOWER contra una subquery
 * Ejemplo: select * from clientes where id NOT LIKE LOWER (select nombre from deudores);
 *
 * @author Matias Leone
 */
public class LnwNIlikeSubquery extends LnwRightOperandSubquery{

	public LnwNIlikeSubquery() {
	}

	/**
	 * NOT LIKE contra una subquery
	 * Ejemplo: select * from clientes where id NOT LIKE LOWER (select nombre from deudores);
	 */
	public LnwNIlikeSubquery(String columna, LnwQuery subquery) {
		super(columna, subquery);
	}

	protected String getOperator() {
		return "NOT LIKE lower(";
	}

	protected String getEnd() {
		return ")";
	}
}
