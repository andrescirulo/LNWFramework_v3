package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * Funciona como un equals de sql para hacer JOIN de dos
 * tablas en el where
 * Ej. columna1 = columna2
 *
 * @author Matias Leone
 */
public class LnwEqualsJoin implements LnwRestriction {

	private String columna1;
	private String columna2;

	public LnwEqualsJoin() {
	}

	public LnwEqualsJoin(String column1, String column2 ) {
		this.columna1 = column1;
		this.columna2 = column2;
	}

	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna1 );
		buffer.append( " = " );
		buffer.append( columna2 );
		return buffer.toString();
	}

}
