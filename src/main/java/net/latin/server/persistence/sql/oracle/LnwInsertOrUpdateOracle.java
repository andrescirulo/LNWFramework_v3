package net.latin.server.persistence.sql.oracle;

import net.latin.server.persistence.sql.base.LnwInsertOrUpdateBase;
import net.latin.server.persistence.sql.core.LnwInsert;
import net.latin.server.persistence.sql.core.LnwUpdate;


/**
 *  Sql InsertOrUpdate para Oracle
 * @author Fernando Diaz
 *
 */
public class LnwInsertOrUpdateOracle  extends LnwInsertOrUpdateBase {

	private LnwInsert insert = new LnwInsertOracle();
	private LnwUpdate update = new LnwUpdateOracle();

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
