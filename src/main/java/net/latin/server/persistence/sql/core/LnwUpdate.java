package net.latin.server.persistence.sql.core;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 *Sql Update
 *Tiene las validaciones obligatorias,ej tabla obligatoria
 *, isertar por lo menos un valor.
 *Si se usa where lleva una LnwRestriction general.
 *
 * @author Santiago Aimetta
 */
public interface LnwUpdate extends IsSerializable {


	public PreparedStatement buildUpdate();

	/**
	 * Setea la tabla donde se actualizaran
	 * los datos
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwUpdate setTable( String table );

	/**
	 * Agrega un par columna,Valor
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwUpdate addValue( String column, Object value );

	/**
	 * Setea una restriccion para el where
	 * la misma debe ser general, ya que
	 * se pisan.
	 * @param whereRestriction
	 * @return LnwUpdate
	 */
	public LnwUpdate setWhere( LnwRestriction whereRestriction);

	/**
	 * Setea una restriccion eq
	 * @return LnwUpdate
	 */
	public LnwUpdate setWhereEq( String column, Object value);


	/**
	 * valida que los datos esten cargados
	 */
	public void validate();


}
