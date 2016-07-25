package net.latin.server.persistence.sql.core.restrictions;

import net.latin.server.persistence.sql.core.LnwQuery;


/**
 * Hace un "EXISTS (sub select)".
 *
 * Ejemplo: select *
 * 			from clientes cli
 * 			where EXISTS (	select nombre
 * 						  	from deudores deud
 * 							where deud.nombre = cli.nombre
 * 						  );
 *
 * @author Fede Temoli
 */
public class LnwExistsSubquery extends LnwRightOperandSubquery {

	public LnwExistsSubquery() {
	}

	/**
	 * Hace un "EXISTS (sub select)".
	 */
	public LnwExistsSubquery(LnwQuery subquery ) {
		super(null, subquery);
	}

	protected String getOperator() {
		return "EXISTS";
	}

}
