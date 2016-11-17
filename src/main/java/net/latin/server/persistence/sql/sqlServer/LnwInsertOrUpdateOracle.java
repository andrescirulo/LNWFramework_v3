package net.latin.server.persistence.sql.sqlServer;

import com.google.gwt.user.client.rpc.IsSerializable;

import net.latin.server.persistence.sql.core.LnwInsert;
import net.latin.server.persistence.sql.core.LnwUpdate;


/**
 * Carga un insert y un update para retornarlos
 * @author Santiago Aimetta
 *
 */
public class LnwInsertOrUpdateOracle  implements IsSerializable {

	private LnwInsert insert = new LnwInsertSqlServer();
	private LnwUpdate update = new LnwUpdateSqlServer();

	public LnwInsertOrUpdateOracle() {
	}

	public LnwInsertOrUpdateOracle setTable( String table ){
		insert.setTable(table);
		update.setTable(table);
		return this;
	}

	/**
	 * Agrega un par columna,Valor
	 *
	 * @param column
	 * @param value
	 * @return LnwInsertOrUpdate
	 */
	public LnwInsertOrUpdateOracle addValue( String column, Object value ){
		insert.addValue(column,value);
		update.addValue(column, value);
		return this;
	}

	/**
	 * Obtiene una sentencia insert con los datos proporcionados
	 * @return insert
	 */
	public LnwInsert getInsert(){
		return insert;
	}
	/**
	 * Obtiene una sentencia update con los datos proporcionados
	 * @return update
	 */
	public LnwUpdate getUpdate(){
		return update;
	}


}
