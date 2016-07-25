package net.latin.server.persistence.sql.base;

import net.latin.server.persistence.sql.core.LnwInsert;
import net.latin.server.persistence.sql.core.LnwUpdate;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Carga un insert y un update para retornarlos
 * @author Santiago Aimetta
 *
 */
public class LnwInsertOrUpdateBase  implements IsSerializable {

	private LnwInsert insert = new LnwInsertBase();
	private LnwUpdate update = new LnwUpdateBase();

	public LnwInsertOrUpdateBase() {
	}

	public LnwInsertOrUpdateBase setTable( String table ){
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
	public LnwInsertOrUpdateBase addValue( String column, Object value ){
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
