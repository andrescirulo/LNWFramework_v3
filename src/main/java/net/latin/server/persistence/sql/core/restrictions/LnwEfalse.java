package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * Verifica si una columna es false
 * @author Santiago Aimetta
 *
 */
public class LnwEfalse implements LnwRestriction{

	private String columna;

	public LnwEfalse() {
	}

	public LnwEfalse(String columna) {
		this.columna = columna;
	}

	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna )
			.append( "= FALSE" );
		return buffer.toString();
	}

}
