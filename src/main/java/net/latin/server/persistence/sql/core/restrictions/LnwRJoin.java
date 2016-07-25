package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;

/**
 * RigthJoin de tablas común
 *
 * @author Santiago Aimetta
 */
public class LnwRJoin implements LnwRestriction {

	private String columna1 = "";
	private String columna2 = "";

	public LnwRJoin() {
	}

	public LnwRJoin(String columna1, String columna2) {
		this.columna1 = columna1;
		this.columna2 = columna2;
	}

	/**
	 * RightJoin nuestra todo lo de la segunda tabla Ej. id_mascota(+) =
	 * id_dueño
	 */
	public String build(List parametros) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( columna1 )
			.append( "(+) = " )
			.append( columna2 );
		return buffer.toString();
	}

}
