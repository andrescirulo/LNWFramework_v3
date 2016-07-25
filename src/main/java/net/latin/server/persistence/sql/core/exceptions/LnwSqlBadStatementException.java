package net.latin.server.persistence.sql.core.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *Excepcion que surge de armar mal un statement,
 *No chequeada.
 *Ejemplo de casos: faltan tablas en el from, insert con
 *más columns q values, etc.
 *
 * @author Santiago Aimetta
 */
public class LnwSqlBadStatementException extends RuntimeException implements IsSerializable {

	public LnwSqlBadStatementException() {
	}

	public LnwSqlBadStatementException(String message) {
		super(message);
	}

	public LnwSqlBadStatementException(Throwable cause) {
		super(cause);
	}

	public LnwSqlBadStatementException(String message, Throwable cause) {
		super(message, cause);
	}

}
