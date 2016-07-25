package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.LnwUpdate;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.core.RestFcty;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;


/**
 *Sql Update
 *Tiene las validaciones obligatorias,ej tabla obligatoria
 *, isertar por lo menos un valor.
 *Si se usa where lleva una LnwRestriction general.
 *
 * @author Santiago Aimetta
 */
public class LnwUpdateBase  implements LnwUpdate {

	private final static String _update = " UPDATE ";
	private final static String _set = " SET ";
	private final static String _where = " WHERE ";

	private String table = "";
	private List<LnwSqlParameter> values = new ArrayList<LnwSqlParameter>();
	private List<String> columns = new ArrayList<String>();
	private LnwRestriction whereRestriction;

	public LnwUpdateBase() {
	}

	/**
	 * Setea la tabla donde se actualizaran
	 * los datos
	 * @param table
	 * @return LnwUpdate
	 */
	public LnwUpdate setTable( String table ){
		this.table = table;
		return this;
	}

	/**
	 * Agrega un par columna,Valor
	 *
	 * @param column
	 * @param value
	 * @return LnwUpdate
	 */
	public LnwUpdate addValue( String column, Object value ){
		this.columns.add(column);
		values.add( LnwSqlParameter.adaptData(value) );
		return this;
	}


	/**
	 * Setea una restriccion para el where
	 * la misma debe ser general, ya que
	 * se pisan.
	 * @param whereRestriction
	 * @return LnwUpdate
	 */
	public LnwUpdate setWhere( LnwRestriction whereRestriction){
		this.whereRestriction = whereRestriction;
		return this;
	}

	/**
	 * Setea una restriccion eq
	 * @return LnwUpdate
	 */
	public LnwUpdate setWhereEq( String column, Object value){
		this.whereRestriction = RestFcty.eq(column, value);
		return this;
	}

	public PreparedStatement buildUpdate(){
		StringBuffer bufferUpdate = new StringBuffer();
		bufferUpdate.append( _update )
			.append( this.table )
			.append( _set );

		//armo las columnas
		StringBuffer bufferColumns = new StringBuffer();
		if ( this.columns != null ) {
			for (Iterator itColumn = this.columns.iterator(); itColumn.hasNext();) {
				String column = (String) itColumn.next();
				bufferColumns.append( column )
					.append( " = ?," );
			}

			//quito la última ,
			bufferColumns
				.deleteCharAt( bufferColumns.length() - 1 )
				.append( " " );

			bufferUpdate.append( bufferColumns.toString() );
		}

		//armo values
		bufferUpdate.append( _where );
		List<Object> paramsWhere = new ArrayList<Object>();
		if(whereRestriction != null) {
			String whereStatement = whereRestriction.build(paramsWhere);
			bufferUpdate.append( whereStatement );
		}

		List<Object> parametrosGral = new ArrayList<Object>();
		parametrosGral.addAll(LnwSqlParameter.recoverData(values));
		parametrosGral.addAll(paramsWhere);
		PreparedStatement pStatement = new PreparedStatement(bufferUpdate.toString(),parametrosGral);
		this.validate();
		return pStatement;
	}

	public void validate(){

		//si no puso la tabla
		if( this.table.length() == 0 ){
			throw new LnwSqlBadStatementException("LnwUpdate invalido: falta tabla ");
		}
		if( this.whereRestriction == null ){
			throw new LnwSqlBadStatementException("LnwUpdate invalido: falta where ");
		}
		if( this.values.isEmpty()){
			throw new LnwSqlBadStatementException("LnwUpdate invalido: faltan columnas y valores ");
		}

	}

}
