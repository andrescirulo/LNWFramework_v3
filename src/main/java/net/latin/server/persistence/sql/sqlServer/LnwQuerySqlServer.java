package net.latin.server.persistence.sql.sqlServer;

import net.latin.server.persistence.sql.base.LnwQueryBase;

/**
 * Sql Query
 * Tiene las validaciones de mostrar algo obligatoriamente y
 * tener un from.
 * Para el where y el having se arman restricciones
 * Aún no contempladas las validaciones de funciones de grupo.
 *
 * @author Santiago Aimetta
 */
public class LnwQuerySqlServer  extends LnwQueryBase {

	private static final String TOP = " TOP ";

	protected void agregaTop(StringBuffer bufferQuery) {
		if(limitRestriction != -1) {
			bufferQuery.append(TOP);
			//TODO testear que sea este el valor real
			bufferQuery.append(this.limitRestriction);
		}
	}


}
