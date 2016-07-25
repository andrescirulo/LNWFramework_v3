package net.latin.server.persistence.sql.core;

import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Builder para agregar JOINs a un Query
 * <br>Ejemplo:
 * <br>SELECT *
 * <br>FROM table1 JOIN table2 ON (table1.col1A = table2.col2)
 * <br>AND LEFT JOIN table3 ON (table1.col1B = table3.col3)
 *
 * <br>
 * <br>Extender si es necesario customizar para cada base de datos.
 *
 * @author Matias Leone
 */
public class LnwJoin implements IsSerializable {

	protected final static String _inner_join = " JOIN ";
	protected final static String _left_join = " LEFT JOIN ";
	protected final static String _right_join = " RIGHT JOIN ";
	protected final static String _on = " ON ";
	protected final static String _as = "AS ";


	protected boolean baseTableAdded = false;
	protected StringBuffer buffer;


	/**
	 * Constructor interno. No construir un join a mano.
	 * <br>Utilizar el método <code>createJoin()</code> de la implementacion
	 * de <code>LnwQuery</code> correspondiente.
	 */
	public LnwJoin() {
		buffer = new StringBuffer();
	}

	/**
	 * Carga el nombre de la tabla base, sin JOIN
	 */
	public LnwJoin baseTable(String tableName, String alias) {
		//agregar tabla base con alias
		buffer.append( tableName );
		buffer.append( " " );
		if( agregarAsEnAliasFrom() ) buffer.append( _as );
		buffer.append( alias );

		baseTableAdded = true;
		return this;
	}

	/**
	 * Método de creación interna del String de un JOIN
	 */
	private void createJoin(String alias1, String[] columns1, String table2, String alias2, String[] columns2, String joinExp) {
		//validaciones
		if(!baseTableAdded) throw new LnwSqlBadStatementException( "Debe agregarse primero la table base del JOIN" );
		if(columns1.length != columns2.length) throw new LnwSqlBadStatementException( "Las columnas para el JOIN tienen distinta cantidad de parámetros" );

		//crear join
		buffer.append( joinExp );
		buffer.append( table2 );
		buffer.append( " " );
		if( agregarAsEnAliasFrom() ) buffer.append( _as );
		buffer.append( alias2 );
		buffer.append( _on );
		buffer.append( "( " );

		//crear relacion de PK con FK
		for (int i = 0; i < columns1.length; i++) {
			//column1
			buffer.append( alias1 );
			buffer.append( "." );
			buffer.append( columns1[ i ] );

			buffer.append( " = " );

			//column2
			buffer.append( alias2 );
			buffer.append( "." );
			buffer.append( columns2[ i ] );

			buffer.append( " AND " );
		}

		//agregar paréntesis final
		buffer.delete( buffer.length() - 5, buffer.length() - 1 );
		buffer.append( " ) " );
	}


	/**
	 * Agrega una tabla con INNER JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 INNER JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param columns1: lista de columnas de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param columns2: lista de columnas de la tabe2 para realizar el JOIN
	 */
	public LnwJoin join( String alias1, String[] columns1, String table2, String alias2, String[] columns2 ) {
		createJoin(alias1, columns1, table2, alias2, columns2, _inner_join);
		return this;
	}

	/**
	 * Agrega una tabla con JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param column1: columna de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param column2: columna de la tabe2 para realizar el JOIN
	 */
	public LnwJoin join( String alias1, String column1, String table2, String alias2, String column2 ) {
		return join(alias1, new String[]{column1}, table2, alias2, new String[]{column2});
	}

	/**
	 * Agrega una tabla con LEFT JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 LEFT JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param columns1: lista de columnas de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param columns2: lista de columnas de la tabe2 para realizar el JOIN
	 */
	public LnwJoin leftJoin( String alias1, String[] columns1, String table2, String alias2, String[] columns2 ) {
		createJoin(alias1, columns1, table2, alias2, columns2, _left_join);
		return this;
	}

	/**
	 * Agrega una tabla con LEFT JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 LEFT JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param column1: columna de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param column2: columna de la tabe2 para realizar el JOIN
	 */
	public LnwJoin leftJoin( String alias1, String column1,String table2, String alias2, String column2 ) {
		return leftJoin(alias1, new String[]{column1}, table2, alias2, new String[]{column2});
	}

	/**
	 * Agrega una tabla con RIGHT JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 RIGHT JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param columns1: lista de columnas de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param columns2: lista de columnas de la tabe2 para realizar el JOIN
	 */
	public LnwJoin rightJoin( String alias1, String[] columns1, String table2, String alias2, String[] columns2 ) {
		createJoin(alias1, columns1, table2, alias2, columns2, _right_join);
		return this;
	}

	/**
	 * Agrega una tabla con RIGHT JOIN en el FROM.
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 RIGHT JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1A = table2.column2A AND
	 * <br>table1.column1B = table2.column2B AND ...)
	 * </code>
	 *
	 * <br>
	 * <br>Siguiendo el ejemplo, la table1 ya tiene que haber sido agregada al JOIN
	 * <br> mediante el constructor o mediante otro método de JOIN.
	 *
	 * @param alias1: alias de la tabla que ya fue agregada al FROM
	 * @param column1: columna de la table1 para realizar el JOIN
	 * @param table2: nombre de la tabla que se quiere agregar con JOIN al FROM
	 * @param alias2: alias de la table2
	 * @param column2: columna de la tabe2 para realizar el JOIN
	 */
	public LnwJoin rightJoin( String alias1, String column1, String table2, String alias2, String column2 ) {
		return rightJoin(alias1, new String[]{column1}, table2, alias2, new String[]{column2});
	}

	/**
	 * Indica si para agregar tablas en el FROM con alias se debe utilizar
	 * la palabra AS o no.
	 * <br>Ejemplo con true: <code>FROM tabla AS alias</code>
	 * <br>Ejemplo con false: <code>FROM tabla alias</code>
	 */
	protected boolean agregarAsEnAliasFrom() {
		return true;
	}

	/**
	 * Método interno. No utilizar.
	 * <br>Genera el String del join
	 */
	public String build() {
		return buffer.toString();
	}



}
