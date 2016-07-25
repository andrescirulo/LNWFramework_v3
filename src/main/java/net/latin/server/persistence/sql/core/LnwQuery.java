package net.latin.server.persistence.sql.core;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Sql Query
 * <br>Tiene las validaciones de mostrar algo obligatoriamente y
 * <br>tener un from.
 * <br>Para el where y el having se arman restricciones
 * <br>Aún no contempladas las validaciones de funciones de grupo.
 *
 * <br>Si se anidan subqueries, hay que ir agregándolos en el orden de la
 * <br>escritura normal del query.
 * <br>Por ejemplo, los subqueries del SELECT deben ser agregados antes
 * <br>que los subqueries del WHERE. Mas allá de que los métodos de la
 * <br>clase permitan alterar el orden.
 *
 * @author Fernando Diaz
 *
 */
public interface LnwQuery extends IsSerializable {


	/**
	 * Construye el query.
	 * Se hacen las validaciones minimas
	 * El PreparedStatement contiene el statement
	 * y los values.
	 * @return PreparedStatement
	 */
	public PreparedStatement buildQuery();

	/**
	 * Agrega una columna q será visualizada en el
	 * select
	 * @param columna
	 * @return LnwQuery
	 */
	public LnwQuery addColumn( String col );

	/**
	 * Agrega una columna q será visualizada en el
	 * select, con un alias
	 * @param columna
	 * @return LnwQuery
	 */
	public LnwQuery addColumn( String col, String alias );

	/**
	 * Agrega un subselect al SELECT
	 * @param query subselect
	 * @param alias alias del subselect
	 */
	public LnwQuery addColumn( LnwQuery query, String alias );

	/**
	 * Hace COUNT(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addCount( String col, String alias ) ;

	/**
	 * Hace SUM(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addSum( String col, String alias ) ;

	/**
	 * Hace AVG(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addAvg( String col, String alias ) ;

	/**
	 * Hace sequence.NEXTVAL para obtener el siguiente valor
	 * de una secuencia
	 * @param sequence
	 * @return
	 */
	public LnwQuery addNextValSequence( String sequence );

	/**
	 * Setea el número de filas q va a mostrar la consulta.
	 * Ver la documentacion propia de la implementacion para cada Base de Datos.
	 * @param filas
	 * @return
	 */
	public LnwQuery setLimit( int rows);

	/**
	 * Setea a partir de que número de fila se mostrarán los datos,
	 * comenzando de 1.
	 * Ver la documentacion propia de la implementacion para cada Base de Datos.
	 * @param rowNum
	 * @return
	 */
	public LnwQuery setOffset( int rowNum);

	/**
	 * Carga el offset y el limit para hacer un query paginado.
	 * Ver la documentacion propia de la implementacion para cada Base de Datos.
	 * @param query
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 */
	public LnwQuery setOffsetAndLimit(int lowerLimit, int upperLimit );

	/**
	 * Carga el offset y el limit para hacer un query paginado con orden ASC
	 * Ver la documentacion propia de la implementacion para cada Base de Datos.
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 * @param columnOrder
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder );

	/**
	 * Carga el offset y el limit para hacer un query paginado con orden
	 * Ver la documentacion propia de la implementacion para cada Base de Datos.
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 * @param columnOrder
	 * @param orderAsc
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder, boolean orderAsc );


	/**
	 * Agrega una tabla al from
	 * @param tableName
	 * @return LnwQuery
	 */
	public LnwQuery addFrom( String tableName );

	/**
	 * Permite agregar un subquery en el from
	 * @param query
	 * @return
	 */
	public LnwQuery addFrom( LnwQuery query );

	/**
	 * Permite agregar un subquery en el from
	 * @param query
	 * @param alias
	 * @return
	 */
	public LnwQuery addFrom( LnwQuery query,String alias);

	/**
	 * Agrega una tabla al from con alias
	 *
	 * @param tableName
	 * @param alias
	 * @return
	 */
	public LnwQuery addFrom( String tableName, String alias );


	/**
	 * Agrega JOIN en el FROM.
	 * <br>Crear el join mediante el metodo <code>createJoin()</code>
	 * <br>Ejemplo:
	 * <br>
	 * <br><code>FROM table1 INNER JOIN table2 AS alias2 ON
	 * <br>(tabla1.column1 = table2.column2)
	 * </code>
	 *
	 */
	public LnwQuery addJoin( LnwJoin join );

	/**
	 * Se crea una instancia de LnwJoin del tipo específico para
	 * cada base de datos
	 */
	public LnwJoin createJoin();


	/**
	 * Setea las restricciones del where.
	 * Se debe armar una restriccion general si
	 * hay más de una, ya que se pisan.
	 *
	 * @param whereRestriction
	 * @return LnwQuery
	 */
	public LnwQuery setWhere( LnwRestriction whereRestriction);

	/**
	 * <p>
	 * Setea un WHERE con un solo equals, de la forma:
	 * <p>
	 * WHERE column = value
	 * <p>
	 * Sirve para agilizar la escritura de querys sencillos
	 * @param column
	 * @param value
	 * @return
	 */
	public LnwQuery setWhereEq( String column, Object value );

	/**
	 * Setea las restricciones del having.
	 * Se debe armar general, ya que si hay
	 * más de una se pisan.
	 *
	 * @param havingRestriction
	 * @return LnwQuery
	 */
	public LnwQuery setHaving( LnwRestriction havingRestriction);

	/**
	 * Agrega a la cláusula orderBy una columna con
	 *   orden ascendente.
	 *
	 * @param parametro
	 * @return
	 */
	public LnwQuery addOrderAsc( String parametro );

	/**
	 *Agrega a la cláusula orderBy una columna con
	 *   orden descendente.
	 * @param parametro
	 * @return
	 */
	public LnwQuery addOrderDsc( String parametro );

	/**
	 * selecciona todas las columnas del from
	 * @return
	 */
	public LnwQuery selectAll();

	/**
	 * Cambia un query que sea del tipo select * from table
	 * por uno del tipo select table.* from table.
	 * Solo puede haber una única tabla en el from y no debe tener alias.
	 */
	public void setSelectAllFromTableInFrom();

	/**
	 * @return true si el query es del tipo select *
	 */
	public boolean isSelectAll();

	/**
	 * Agrega a la cláusula orderBy una
	 * columna.
	 * @param parametro
	 * @return
	 */
	public LnwQuery addGroupBy( String parametro );

	/**
	 * valida que el query tenga los datos necesarios para ser
	 * construido
	 */
	public void validate();

	/**
	 * agrega todas las columnas
	 * @param columns
	 */
	public void addAllColumns(List<String> columns);

	/**
	 * selecciona las filas distintas
	 */
	public LnwQuery selectDistinct();

	/**
	 * selecciona las filas unicas
	 */
	public LnwQuery selectUnique();

	public void union(LnwQuery query);
}
