package net.latin.server.persistence.sql.core.restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * Permite agregar restricciones unidas por or
 *
 * @author Santiago Aimetta
 */
public class LnwMultipleOr implements LnwMultipleRestriction {

	private List<LnwRestriction> restricciones = new ArrayList<LnwRestriction>();

	public LnwMultipleOr() {
	}

	/**
	 * Agrega una restriccion al and
	 * @param restriction
	 */
	public LnwMultipleOr addRestriction(LnwRestriction restriction){
		this.restricciones.add(restriction);
		return this;
	}

	public String build(List<Object> parametros) {
		List<String> sentencias = new ArrayList<String>();
		for (Iterator itRestrics = this.restricciones.iterator(); itRestrics.hasNext();) {
			LnwRestriction restric = (LnwRestriction) itRestrics.next();
			sentencias.add( restric.build( parametros ) );
		}

		StringBuffer statement = new StringBuffer();
		for (int j = 0; j < sentencias.size(); j++) {
			statement.append( " (" )
				.append( sentencias.get( j ) )
				.append( ") OR" );
		}
		statement.delete(statement.length() - 2, statement.length());
		return statement.toString();
	}

	public boolean hasRestrictions() {
		return this.restricciones.size() > 0;
	}

}
