package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * ILike Verifica si una cadena incluye a la otra resultado de una subquery Ej. columna Like "%djskdj%"
 * <-- (select)
 *
 */
public class LnwIlikeSubquery extends LnwRightOperandSubquery{

	//Metodo requerido por ser serializable
	public LnwIlikeSubquery() {
	}

	/**
	 *
	 * @param columna
	 * @param subquery
	 */
	public LnwIlikeSubquery(String columna, LnwQuery subquery) {
		super(columna, subquery);
	}

	protected String getOperator() {
		return "LIKE lower(";
	}

	protected String getEnd() {
		return ")";
	}
}
