package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * InnerJoin de tablas com�n
 *
 * @author Santiago Aimetta
 */
public class LnwIJoin implements LnwRestriction {

	private String columna1 = "";
	private String columna2 = "";

	public LnwIJoin() {
	}

	public LnwIJoin(String columna1, String columna2) {
		this.columna1 = columna1;
		this.columna2 = columna2;
	}

	/**
	 * Join Ej. id_mascota = id_due�o
	 */
	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna1 )
			.append( " = " )
			.append( columna2 );
		return buffer.toString();
	}

}
