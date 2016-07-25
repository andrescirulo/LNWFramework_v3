package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;

/**
 * Like Verifica si dos caracteres son coincidentes Ej. columna Like (select)
 * <--Valor
 *
 */
public class LnwLikeSubquery extends LnwRightOperandSubquery{

	//Metodo requerido por ser serializable
	public LnwLikeSubquery() {
	}


	/**
	 *
	 * @param columna
	 * @param valor
	 */
	public LnwLikeSubquery(String columna, LnwQuery subquery) {
		super(columna, subquery);
	}

	protected String getOperator() {
		return "LIKE";
	}

}
