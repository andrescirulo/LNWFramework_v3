package net.latin.server.persistence.sql.oracle;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Utilidades generales para oracle
 *
 * @author Matias Leone
 */
public class LnwOracleUtils implements IsSerializable {

	public LnwOracleUtils() {
	}

	/**
	 * Convierte un boolean en un int 1 o 0
	 * @param bool
	 * @return
	 */
	public static int adaptBoolean(Boolean bool) {
		if( bool == null ) return 0;
		return bool.booleanValue() ? 1 : 0;
	}

}
