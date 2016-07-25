package net.latin.server.persistence.sql.core.restrictions;

import java.util.ArrayList;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
/**
 * Permite crear una restricción con nexos
 * and y or.
 * Cada restriccion se adjunta a la anterior mediante un AND o un OR.
 * Ejemplo: WHERE rest1 AND rest2 OR rest3
 * El nexo de la primera restriccion agregada no es utilizado.
 *
 * @author Santiago Aimetta
 */
public class LnwMultipleCustomRestriction implements LnwMultipleRestriction {

	private List<LnwRestriction> restricciones = new ArrayList<LnwRestriction>();
	private List<String> connectors = new ArrayList<String>();

	public LnwMultipleCustomRestriction() {
	}

	/**
	 * @deprecated
	 * Agrega una restriccion generica
	 * Se considera un AND.
	 * Mantenido por compatibilidad hacia atras.
	 */
	public LnwMultipleCustomRestriction addRestriction(LnwRestriction restriction){
		addAndRestriction(restriction);
		return this;
	}

	/**
	 * Agrega una restriccion al and
	 * @param restriction
	 */
	public LnwMultipleCustomRestriction addAndRestriction(LnwRestriction restriction) {
		this.restricciones.add( restriction );
		this.connectors.add(" AND ");
		return this;
	}

	/**
	 * Agrega una restriccion al or
	 * @param restriction
	 */
	public LnwMultipleCustomRestriction addOrRestriction(LnwRestriction restriction){
		this.restricciones.add( restriction );
		this.connectors.add(" OR ");
		return this;
	}


	public String build(List<Object> parametros) {
		StringBuffer statement = new StringBuffer();
		int size = restricciones.size();
		for (int i = 0; i < size; i++) {
			statement.append(" (");
			statement.append( restricciones.get(i).build(parametros) );
			statement.append(") ");

			if(i < size -1) {
				statement.append( connectors.get(i + 1) );
			}
		}

		return statement.toString();
	}

	public boolean hasRestrictions() {
		return this.restricciones.size() > 0;
	}


}
