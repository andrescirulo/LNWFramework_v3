package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * is null
 *
 * @author Santiago Aimetta
 *
 */
public class LnwIsNull implements LnwRestriction {

	private String columna;

	public LnwIsNull() {
	}

	public LnwIsNull(String columna) {
		this.columna = columna;
	}

	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( " is Null" );
		return buffer.toString();
	}

}
