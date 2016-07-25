package net.latin.server.persistence.sql.core.restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.RestFcty;
/**
 * Permite agregar restricciones unidas por and
 *
 * @author Santiago Aimetta
 */
public class LnwMultipleAnd implements LnwMultipleRestriction {

	private List<LnwRestriction> restricciones = new ArrayList<LnwRestriction>();

	public LnwMultipleAnd() {
	}

	/**
	 * Agrega una restriccion al and
	 * @param restriction
	 */
	public LnwMultipleAnd addRestriction(LnwRestriction restriction){
		this.restricciones.add(restriction);
		return this;
	}

	/**
	 * Agrega una restriccion eq al and
	 */
	public LnwMultipleAnd addEqRestriction(String column, Object value){
		this.restricciones.add(RestFcty.eq(column, value));
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
				.append( ") AND" );
		}
		statement.delete(statement.length() - 3, statement.length());
		return statement.toString();
	}

	public boolean hasRestrictions() {
		return this.restricciones.size() > 0;
	}

}
