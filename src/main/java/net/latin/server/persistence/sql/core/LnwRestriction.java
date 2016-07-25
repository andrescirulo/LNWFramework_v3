package net.latin.server.persistence.sql.core;

import java.util.List;

/**
 * Tipo para las restricciones de una instruccion
 * sql
 * Ej.Equals,Exist,Like,Ilike,AND,OR,etc.
 *
 * @author Santiago Aimetta
 */
public interface LnwRestriction {
	/**
	 * Recibe los parámetros, los modifica y
	 * devuelve el string de la sentencia
	 * @param parametros
	 * @return
	 */
	public String build(List<Object> parametros);

}
