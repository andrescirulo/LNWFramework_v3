package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * Equals true
 *
 * @author Matias Leone
 */
public class LnwEtrue implements LnwRestriction{

	private String columna;

	public LnwEtrue() {
	}

	public LnwEtrue(String columna) {
		this.columna = columna;
	}

	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( "= 1" ); //FIXME cambiamos TRUE por 1, para que funcione en oracle.
		return buffer.toString();
	}

}
