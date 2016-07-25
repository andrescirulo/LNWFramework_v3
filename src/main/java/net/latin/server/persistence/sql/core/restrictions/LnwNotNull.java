package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * Not null
 *
 * @author Santiago Aimetta
 *
 */
public class LnwNotNull implements LnwRestriction {

	private String columna;

	public LnwNotNull() {
	}

	public LnwNotNull(String columna) {
		this.columna = columna;
	}

	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " IS NOT NULL" );
		return buffer.toString();
	}

}
