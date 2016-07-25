package net.latin.server.persistence.sql.core;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Sql Insert
 *
 * @author Santiago Aimetta
 */
public interface LnwInsert extends IsSerializable {


	/**
	 * Crea el insert, el statement y los values
	 * se retornan en PreparedStatement
	 * @return PreparedStatement
	 */
	public PreparedStatement buildInsert();

	
	/**
	 * Setea la tabla en la q se hara el insert
	 * @param table
	 * @return
	 */
	public LnwInsert setTable( String table );
	
	/**
	 * Agrega una columna al insert
	 * @param column
	 * @return
	 */
	public LnwInsert addColumn( String column );
	
	/**
	 * Setea  el valor a insertar, si no se
	 * explicitan todas las columnas, debe ir en
	 * el orden que figuran en la tabla
	 * @param value
	 * @return
	 */
	public LnwInsert addValue( Object value );
	
	/**
	 * Agrega un par columna,Valor
	 *
	 * @param column
	 * @param value
	 * @return LnwInsert
	 */
	public LnwInsert addValue( String column, Object value );
	
	/**
	 * Valida la forma en la que esta hecho el Insert
	 */
	public void validate();


}

