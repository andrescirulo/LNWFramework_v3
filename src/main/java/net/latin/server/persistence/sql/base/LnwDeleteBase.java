package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwDelete;
import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.core.RestFcty;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;


/**
 *Sql Delete
 *Tiene las validaciones obligatorias,ej tabla obligatoria
 *Si se usa where lleva una LnwRestriction general.
 *
 */
public class LnwDeleteBase  implements LnwDelete {

	protected final static String _delete = " DELETE ";
	protected final static String _from = " FROM ";
	protected final static String _where = " WHERE ";

	protected String table = "";
	protected LnwRestriction whereRestriction;


	public LnwDeleteBase() {
	}

	/**
	 * Setea la tabla del from, de la cual
	 * se borrarán filas
	 * @param table
	 * @return LnwDelete
	 */
	public LnwDelete setTable( String table ){
		this.table = table;
		return this;
	}


	/**
	 * Setea una restriccion para el where
	 * la misma debe ser general, ya que
	 * se pisan.
	 * @param whereRestriction
	 * @return LnwDelete
	 */
	public LnwDelete setWhere( LnwRestriction whereRestriction){
		this.whereRestriction = whereRestriction;
		return this;
	}

	/**
	 * Setea una restriccion eq
	 * @return LnwDelete
	 */
	public LnwDelete setWhereEq( String column, Object value){
		this.whereRestriction = RestFcty.eq(column, value);
		return this;
	}

	public PreparedStatement buildDelete(){
		StringBuffer bufferDelete = new StringBuffer();
		bufferDelete.append( _delete );

		//armo from
		armarFrom(bufferDelete);

		//armo where
		List<Object> paramsWhere = armarWhere(bufferDelete);

		agregarLastSemiColon(bufferDelete);

		List<Object> parametrosGral = armarParametros(paramsWhere);
		PreparedStatement pStatement = new PreparedStatement(bufferDelete.toString(),parametrosGral);
		this.validate();
		return pStatement;
	}

	protected List<Object> armarParametros(List<Object> paramsWhere) {
		List<Object> parametrosGral = new ArrayList<Object>();
		parametrosGral.addAll( paramsWhere );
		return parametrosGral;
	}

	protected List<Object> armarWhere(StringBuffer bufferDelete) {
		bufferDelete.append( _where );
		List<Object> paramsWhere = new ArrayList<Object>();
		String whereStatement = whereRestriction.build(paramsWhere);
		bufferDelete.append( whereStatement );
		return paramsWhere;
	}

	protected void armarFrom(StringBuffer bufferDelete) {
		bufferDelete.append( _from )
			.append( this.table);
	}

	public void validate(){

		//si no puso la tabla
		if( this.table.length() == 0 ){
			throw new LnwSqlBadStatementException("LnwDelete invalido: falta tabla ");
		}
		if( this.whereRestriction == null ){
			throw new LnwSqlBadStatementException("LnwDelete invalido: falta where ");
		}

	}
	protected void agregarLastSemiColon(StringBuffer bufferQuery) {
		//bufferQuery.append( ";" ); //FIXME semicolon eliminado, para que funcione en oracle.
	}
}
