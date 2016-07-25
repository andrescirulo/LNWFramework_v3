package net.latin.server.persistence.sql.oracle;

import net.latin.server.persistence.sql.base.LnwFunctionBase;


/**
 *  Sql Function para Oracle
 * @author Fernando Diaz
 *
 */
public class LnwFunctionOracle extends LnwFunctionBase {

	private final static String FROM_DUAL = " FROM dual ";

	public LnwFunctionOracle() {
	}

	protected void armarFrom(StringBuffer bufferStoredProcedure) {
		bufferStoredProcedure.append(FROM_DUAL);
	}


}
