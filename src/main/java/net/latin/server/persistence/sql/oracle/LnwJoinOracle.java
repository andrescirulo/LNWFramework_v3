package net.latin.server.persistence.sql.oracle;

import net.latin.server.persistence.sql.core.LnwJoin;

/**
 * JOIN de Oracle
 *
 * @author Matias Leone
 */
public class LnwJoinOracle extends LnwJoin {

	protected boolean agregarAsEnAliasFrom() {
		return false;
	}

}
