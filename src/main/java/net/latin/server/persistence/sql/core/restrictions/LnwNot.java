package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * Funciona como un  not de sql
 * Ej. NOT( Restriccion )
 *
 * @author Santiago Aimetta
 */
public class LnwNot implements LnwRestriction {

	private LnwRestriction restriction;

	public LnwNot() {
	}

	/**
	 * Funciona como un  not de sql
	 * Ej. NOT( Restriccion )
	 *
	 * @author Santiago Aimetta
	 */
	public LnwNot( LnwRestriction restriction ) {
		this.restriction = restriction;
	}


	public String build(List<Object> parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( " NOT ( " )
			.append( restriction.build( parametros ) )
			.append( ") " );
		return buffer.toString();
	}
}
