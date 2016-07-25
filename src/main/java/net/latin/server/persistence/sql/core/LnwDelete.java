package net.latin.server.persistence.sql.core;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Interfaz para crear una sentencia delete
 * @author Fernando Diaz
 *
 */
public interface LnwDelete extends IsSerializable {

	/**
	 * Setea la tabla del from, de la cual
	 * se borrarán filas
	 * @param table
	 * @return LnwDelete
	 */
	public LnwDelete setTable( String table );
	
	/**
	 * Setea una restriccion para el where
	 * la misma debe ser general, ya que
	 * se pisan.
	 * @param whereRestriction
	 * @return LnwDelete
	 */
	public LnwDelete setWhere( LnwRestriction whereRestriction);
	
	/**
	 * Setea una restriccion eq
	 * @return LnwDelete
	 */
	public LnwDelete setWhereEq( String column, Object value);
	
	/**
	 * Construye la sentencia delete
	 * @return
	 */
	public PreparedStatement buildDelete();
	
	/**
	 * Valida que tenga seteado una tabla y las restricciones
	 */
	public void validate();

}
