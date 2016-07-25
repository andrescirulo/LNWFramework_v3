package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * NOT Like Verifica si dos caracteres son coincidentes Ej. columna NOT Like
 * (select) <-- Subquery que devuelve una cadena
 *
 */
public class LnwNLikeSubquery extends LnwRightOperandSubquery{

	public LnwNLikeSubquery() {
	}

	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwNLikeSubquery(String columna, LnwQuery subquery) {
		super(columna,subquery);
	}

	protected String getOperator() {
		return "NOT LIKE";
	}

}
