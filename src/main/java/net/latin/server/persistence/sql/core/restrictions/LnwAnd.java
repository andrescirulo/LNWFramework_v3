package net.latin.server.persistence.sql.core.restrictions;


import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * And.
 * Recibe dos restricciones y las vincula con un
 * nexo lógico AND.
 *
 * @author Santiago Aimetta
 */
public class LnwAnd implements LnwRestriction {

	LnwRestriction restrictionA = null;
	LnwRestriction restrictionB = null;

	public LnwAnd() {
	}

	/**
	 *
	 * @param restriction1
	 * @param restriction2
	 */
	public LnwAnd( LnwRestriction restriction1, LnwRestriction restriction2) {
		restrictionA = restriction1;
		restrictionB = restriction2;
	}

	public String build(List<Object> parametros) {
		String sentenciaA = restrictionA.build(parametros);
		String sentenciaB = restrictionB.build(parametros);

		StringBuffer buffer = new StringBuffer();
		buffer.append( "(" )
			.append( sentenciaA )
			.append(") AND (")
			.append( sentenciaB )
			.append( ")" );
		return buffer.toString();
	}

}
