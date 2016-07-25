package net.latin.server.persistence.sql.core;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 *Resultado de ejecutar cualquier
 *instrucción sql,contiene el String con el statement
 *y un array con los parametros.
 *
 * @author Santiago Aimetta
 */
public class PreparedStatement  implements IsSerializable {

	private String sentencia;
	private Object[] params;

	public PreparedStatement() {
	}

	public PreparedStatement(String statement, Collection parametrosGral) {
		sentencia = statement;
		params = parametrosGral.toArray();
	}

	/**
	 * Es la instruccion como String
	 * @return sentencia
	 */
	public String getSentencia() {
		return sentencia;
	}

	/**
	 * Retorna los párametros de la sentencia
	 * @return Object[] params
	 */
	public Object[] getParams() {
		return params;
	}

}
