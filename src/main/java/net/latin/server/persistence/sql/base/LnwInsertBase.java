package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwInsert;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;

/**
 * Sql Insert
 *
 * @author Santiago Aimetta
 */
public class LnwInsertBase  implements LnwInsert {

	private final static String _insert = " INSERT ";
	private final static String _into = " INTO ";
	private final static String _values = " VALUES ";

	private String table = "";
	private List<String> columns = new ArrayList<String>();
	private List<LnwSqlParameter> values = new ArrayList<LnwSqlParameter>();


	public LnwInsertBase() {
	}

	/**
	 * Setea la tabla en la q se hara el insert
	 * @param table
	 * @return
	 */
	public LnwInsert setTable( String table ){
		this.table = table;
		return this;
	}

	/**
	 * Agrega una columna al insert
	 * @param column
	 * @return
	 */
	public LnwInsert addColumn( String column ){
		this.columns.add(column);
		return this;
	}

	/**
	 * Setea  el valor a insertar, si no se
	 * explicitan todas las columnas, debe ir en
	 * el orden que figuran en la tabla
	 * @param value
	 * @return
	 */
	public LnwInsert addValue( Object value ){
		this.values.add(LnwSqlParameter.adaptData(value));
		return this;
	}

	/**
	 * Agrega un par columna,Valor
	 *
	 * @param column
	 * @param value
	 * @return LnwInsert
	 */
	public LnwInsert addValue( String column, Object value ){
		this.columns.add(column);
		this.addValue(value);
		return this;
	}

	/**
	 * Crea el insert, el statement y los values
	 * se retornan en PreparedStatement
	 * @return PreparedStatement
	 */
	public PreparedStatement buildInsert(){
		StringBuffer bufferInsert = new StringBuffer();
		bufferInsert.append( _insert )
			.append( _into )
			.append( this.table );

		//armo las columnas
		StringBuffer bufferColumns = new StringBuffer();
		bufferColumns.append( " (" );
		StringBuffer bufferValues = new StringBuffer();
		bufferValues.append( " (" );

		if( this.columns != null ){
			for (Iterator itColumns = this.columns.iterator(); itColumns.hasNext();) {
				String column = (String) itColumns.next();
				bufferColumns.append( column )
					.append( "," );
				bufferValues.append( "?," );
			}

			//quito la última ,
			bufferColumns
				.deleteCharAt( bufferColumns.length() - 1 )
				.append( ") " );
			//quito la última ,
			bufferValues
				.deleteCharAt( bufferValues.length() - 1 )
				.append( ") " );

			bufferInsert.append( bufferColumns.toString() );
		}
		//armo values
		bufferInsert.append( _values )
			.append( bufferValues.toString() );

		PreparedStatement pStatement = new PreparedStatement(bufferInsert.toString(), LnwSqlParameter.recoverData(this.values));
		this.validate();
		return pStatement;
	}


	/**
	 * Valida la forma en la que esta hecho el Insert
	 */
	public void validate(){

		//si no puso la tabla
		if( this.table.equals("") ) {
			throw new LnwSqlBadStatementException("LnwInsert invalido: falta tabla ");
		}
		//si la cantidad de valores es distinta al nro de columnas
		int cantCols = this.columns.size();
		int cantValues = this.values.size();
		if( cantCols != cantValues ) {
			throw new LnwSqlBadStatementException("LnwInsert invalido: El número de columnas no coincide con los valores ");
		}
		if( this.values.isEmpty() ) {
			throw new LnwSqlBadStatementException("LnwInsert invalido: faltan ingresar los values ");
		}

	}

}

