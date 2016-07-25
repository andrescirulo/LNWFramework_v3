package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * OR
 *Recibe dos restricciones y las vincula con un
 *nexo lógico OR.
 * @author Santiago Aimetta
 */
public class LnwOr implements LnwRestriction {

	LnwRestriction restrictionA = null;
	LnwRestriction restrictionB = null;

	public LnwOr() {
	}

	public LnwOr(LnwRestriction restrictionA, LnwRestriction restrictionB) {
		super();
		this.restrictionA = restrictionA;
		this.restrictionB = restrictionB;
	}

	public String build(List<Object> parametros) {
		String sentenciaA = restrictionA.build(parametros);
		String sentenciaB = restrictionB.build(parametros);

		StringBuffer buffer = new StringBuffer();
		buffer.append( "(" )
			.append( sentenciaA )
			.append( ") OR (" )
			.append( sentenciaB )
			.append( ") " );
		return buffer.toString();
	}

}
