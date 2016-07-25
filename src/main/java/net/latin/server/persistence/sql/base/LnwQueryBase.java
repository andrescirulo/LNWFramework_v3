package net.latin.server.persistence.sql.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.persistence.sql.core.LnwJoin;
import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.core.RestFcty;
import net.latin.server.persistence.sql.core.exceptions.LnwNotImplementedException;
import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;

/**
 * Sql Query
 * Tiene las validaciones de mostrar algo obligatoriamente y
 * tener un from.
 * Para el where y el having se arman restricciones
 * Aún no contempladas las validaciones de funciones de grupo.
 *
 * @author Santiago Aimetta
 */
public class LnwQueryBase  implements LnwQuery {

	protected final static String _select = " SELECT ";
	protected final static String _orderBY = " ORDER BY ";
	protected final static String _from = " FROM ";
	protected final static String _groupBy = " GROUP BY ";
	protected final static String _having = " HAVING ";
	protected final static String _where = " WHERE ";
	protected final static String _limit = " LIMIT ";
	protected final static String _offset = " OFFSET ";
	protected final static String _count = " COUNT";
	protected final static String _max = "MAX";
	protected final static String _min = "MIN";
	private static final Object _sum = " SUM";
	private static final Object _avg = " AVG";
	protected final static String _as = "AS ";
	protected final static String _union = " UNION ";

	protected final static String _select_all = "";
	protected final static String _select_distinct = " DISTINCT ";
	protected final static String _select_unique = " UNIQUE ";
	

	protected int limitRestriction = -1;
	protected int offsetRestriction = 0;
	protected LnwRestriction whereRestriction;
	protected LnwRestriction havingRestriction;
	protected String selectMode = _select_all;

	protected List<String> columns = new ArrayList<String>();
	protected List<String> fromRestrictions = new ArrayList<String>();
	protected List<String> groupByRestrictions = new ArrayList<String>();
	protected List<String> orderByRestrictions = new ArrayList<String>();
	protected List<LnwSqlParameter> parametrosFrom;
	protected List<LnwSqlParameter> parametrosSelect;
	protected List<LnwQuery> unions;



	public LnwQueryBase() {
	}

	/**
	 * Agrega una columna q será visualizada en el
	 * select
	 * @param columna
	 * @return LnwQuery
	 */
	public LnwQuery addColumn( String col ){
		columns.add(col);
		return this;
	}

	/**
	 * Agrega una columna q será visualizada en el
	 * select, con un alias
	 * @param columna
	 * @return LnwQuery
	 */
	public LnwQuery addColumn( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( col );
		buffer.append( " " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}

	public LnwQuery addColumn(LnwQuery query, String alias) {
		PreparedStatement buildQuery = query.buildQuery();

		StringBuffer buffer = new StringBuffer();
		buffer.append( " ( " );
		buffer.append( buildQuery.getSentencia() );
		buffer.append( " ) " );

		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		buffer.append( " " );

		columns.add( buffer.toString() );

		if( parametrosSelect == null ) parametrosSelect = new ArrayList<LnwSqlParameter>();
		parametrosSelect.addAll( LnwSqlParameter.adaptData( buildQuery.getParams() ) );
		return this;
	}


	/**
	 * Hace COUNT(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addCount( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( _count );
		buffer.append( "(" );
		buffer.append( col );
		buffer.append( ") " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}
	/**
	 * Hace MAX(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addMax( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( _max );
		buffer.append( "(" );
		buffer.append( col );
		buffer.append( ") " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}
	/**
	 * Hace MIN(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addMin( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( _min );
		buffer.append( "(" );
		buffer.append( col );
		buffer.append( ") " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}

	/**
	 * Hace SUM(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addSum( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( _sum );
		buffer.append( "(" );
		buffer.append( col );
		buffer.append( ") " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}

	/**
	 * Hace AVG(col) as alias
	 * @param col
	 * @param alias
	 * @return
	 */
	public LnwQuery addAvg( String col, String alias ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( _avg );
		buffer.append( "(" );
		buffer.append( col );
		buffer.append( ") " );
		if( agregarAsEnAliasSelect() ) buffer.append( _as );
		buffer.append( alias );
		addColumn( buffer.toString() );
		return this;
	}

	/**
	 * Hace sequence.NEXTVAL para obtener el siguiente valor
	 * de una secuencia
	 * @param sequence
	 * @return
	 */
	public LnwQuery addNextValSequence( String sequence ){
		throw new LnwNotImplementedException();
	}

	/**
	 * Setea el número de filas q va a mostrar la consulta
	 * @param filas
	 * @return
	 */
	public LnwQuery setLimit( int rows){
		limitRestriction = rows;
		return this;
	}

	/**
	 * Setea a partir de que número de fila se mostrarán los datos,
	 * comenzando de 0.
	 * @param rowNum
	 * @return
	 */
	public LnwQuery setOffset( int rowNum){
		offsetRestriction = rowNum;
		return this;
	}

	/**
	 * Carga el offset y el limit para hacer un query paginado, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * @param query
	 * @param lowerLimit
	 * @param upperLimit
	 */
	public LnwQuery setOffsetAndLimit(int lowerLimit, int upperLimit ){
		throw new LnwNotImplementedException();
	}

	/**
	 * Carga el offset y el limit para hacer un query paginado ordenado ASC, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * @param lowerLimit
	 * @param upperLimit
	 * @param columnOrder
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder ) {
		throw new LnwNotImplementedException();
	}

	/**
	 * Carga el offset y el limit para hacer un query paginado ordenado, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * @param lowerLimit
	 * @param upperLimit
	 * @param columnOrder
	 * @param orderAsc
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder, boolean orderAsc ) {
		throw new LnwNotImplementedException();
	}

	/**
	 * Agrega una tabla al from
	 * @param tableName
	 * @return LnwQuery
	 */
	public LnwQuery addFrom( String tableName ){
		fromRestrictions.add( tableName );
		return this;
	}

	/**
	 * Permite agregar un subquery en el from
	 * @param query
	 * @return
	 */
	public LnwQuery addFrom( LnwQuery query ){
		return addFrom(query,"");
	}
	/**
	 * Permite agregar un subquery en el from
	 * @param query
	 * @param alias
	 * @return
	 */
	public LnwQuery addFrom( LnwQuery query, String alias){
		PreparedStatement buildQuery = query.buildQuery();

		StringBuffer buffer = new StringBuffer();
		buffer.append( " ( " );
		buffer.append( buildQuery.getSentencia() );
		buffer.append( " ) " );

		if(alias != null && !alias.equals("")){
			if( agregarAsEnAliasFrom() ) buffer.append( _as );
			buffer.append( alias );
			buffer.append( " " );
		}

		fromRestrictions.add( buffer.toString() );

		if( parametrosFrom == null ) parametrosFrom = new ArrayList<LnwSqlParameter>();
		parametrosFrom.addAll( LnwSqlParameter.adaptData( buildQuery.getParams() ) );
		return this;
	}

	/**
	 * Agrega una tabla al from con alias
	 *
	 * @param tableName
	 * @param alias
	 * @return
	 */
	public LnwQuery addFrom( String tableName, String alias ){
		StringBuffer buffer = new StringBuffer();
		buffer.append( tableName );
		buffer.append( " " );
		if( agregarAsEnAliasFrom() ) buffer.append( _as );
		buffer.append( alias );
		fromRestrictions.add( buffer.toString() );
		return this;
	}

	public LnwQuery addJoin(LnwJoin join) {
		fromRestrictions.add( join.build() );
		return this;
	}


	/**
	 * Setea las restricciones del where.
	 * Se debe armar una restriccion general si
	 * hay más de una, ya que se pisan.
	 *
	 * @param whereRestriction
	 * @return LnwQuery
	 */
	public LnwQuery setWhere( LnwRestriction whereRestriction ){
		this.whereRestriction = whereRestriction;
		return this;
	}

	/**
	 * <p>
	 * Setea un WHERE con un solo equals, de la forma:
	 * <p>WHERE column = value
	 * <p>Sirve para agilizar la escritura de querys sencillos
	 * @param column
	 * @param value
	 * @return
	 */
	public LnwQuery setWhereEq(String column, Object value) {
		setWhere( RestFcty.eq(column, value) );
		return this;
	}

	/**
	 * Setea las restricciones del having.
	 * Se debe armar general, ya que si hay
	 * más de una se pisan.
	 *
	 * @param havingRestriction
	 * @return LnwQuery
	 */
	public LnwQuery setHaving( LnwRestriction havingRestriction){
		this.havingRestriction = havingRestriction;
		return this;
	}

	/**
	 * Agrega a la cláusula orderBy una columna con
	 *   orden ascendente.
	 *
	 * @param parametro
	 * @return
	 */
	public LnwQuery addOrderAsc( String parametro ){
		orderByRestrictions.add(parametro+" ASC");
		return this;
	}
	/**
	 *Agrega a la cláusula orderBy una columna con
	 *   orden descendente.
	 * @param parametro
	 * @return
	 */
	public LnwQuery addOrderDsc( String parametro ){
		orderByRestrictions.add(parametro+" DESC");
		return this;
	}

	public LnwQuery selectAll(){
		this.columns.add("*");
		return this;
	}

	/**
	 * Cambia un query que sea del tipo select * from table
	 * por uno del tipo select table.* from table.
	 * Solo puede haber una única tabla en el from y no debe tener alias.
	 */
	public void setSelectAllFromTableInFrom() {
		if( this.fromRestrictions.size() != 1 ) {
			throw new LnwSqlBadStatementException( "Solo se puede hacer tablaFrom.* cuando hay una única tabla en el from" );
		}

		this.columns.clear();
		this.columns.add( fromRestrictions.get( 0 ) +  ".*");
	}

	/**
	 * @return true si el query es del tipo select *
	 */
	public boolean isSelectAll() {
		return columns.size() == 1 && columns.get(0).equals( "*" );
	}

	/**
	 * Agrega a la cláusula orderBy una
	 * columna.
	 * @param parametro
	 * @return
	 */
	public LnwQuery addGroupBy( String parametro ){
		groupByRestrictions.add(parametro);
		return this;
	}

	/**
	 * Construye el query.
	 * Se hacen las validaciones minimas
	 * El PreparedStatement contiene el statement
	 * y los values.
	 * @return PreparedStatement
	 */
	public PreparedStatement buildQuery(){
		//Armo select
		StringBuffer bufferQuery = new StringBuffer();

		armarSelect(bufferQuery);

		//Armo from
		armarFrom(bufferQuery);

		//Armo where
		List<Object> parametrosWhere = armarWhere(bufferQuery);

		//Armo groupBy
		armarGroupBy(bufferQuery);

		//Armo having
		List<Object> parametrosHaving = armarHaving(bufferQuery);

		//Agregar Union
		List<Object> parametrosUnion = armarUnions(bufferQuery);
		
		//Armo orderBy
		armarOrderBy(bufferQuery);

		//Armo limit
		armarLimit(bufferQuery);

		agregarLastSemiColon(bufferQuery);


		//agregar todos los parametros
		List<Object> parametrosGral = armarParametros(parametrosWhere, parametrosHaving,parametrosUnion);

		PreparedStatement pStatement = new PreparedStatement(bufferQuery.toString(),parametrosGral);
		
		this.validate();
		return pStatement;
	}

	private List<Object> armarUnions(StringBuffer bufferQuery) {
		List<Object> parametrosUnions=new ArrayList<Object>();
		
		if( this.unions != null ){
			for (LnwQuery union:this.unions){
				StringBuffer bufferUnion = new StringBuffer();
				PreparedStatement statement = union.buildQuery();
				bufferUnion.append( _union )
					.append( statement.getSentencia() );
				
				bufferQuery.append( bufferUnion );
				
				for (Object param:statement.getParams()){
					parametrosUnions.add(param);
				}
			}
		}
		return parametrosUnions;
	}

	/**
	 * Arma la lista de parámetros que luego son pasados a JDBC.
	 * Se arman los parámetros del FROM, WHERE y HAVING
	 * @param parametrosUnion 
	 */
	private List<Object> armarParametros(List<Object> parametrosWhere, List<Object> parametrosHaving, List<Object> parametrosUnion) {
		List<Object> parametrosGral = new ArrayList<Object>();
		if( parametrosFrom != null ) {
			parametrosGral.addAll( LnwSqlParameter.recoverData( parametrosFrom ) );
		}
		if( parametrosSelect != null ) {
			parametrosGral.addAll( LnwSqlParameter.recoverData( parametrosSelect ) );
		}
		parametrosGral.addAll( parametrosWhere );
		parametrosGral.addAll( parametrosHaving );
		parametrosGral.addAll( parametrosUnion );
		return parametrosGral;
	}

	/**
	 * Genera el ORDER BY del QUERY
	 */
	protected void armarOrderBy(StringBuffer bufferQuery) {
		if( !this.orderByRestrictions.isEmpty()  ){
			StringBuffer bufferOrderBy = new StringBuffer();
			bufferOrderBy.append( _orderBY );

//			for (Iterator itOrderByRestrics = this.orderByRestrictions.iterator(); itOrderByRestrics.hasNext();) {
//				String orden = (String) itOrderByRestrics.next();
//				bufferOrderBy.append( orden )
//					.append( "," );
//			}

			for (int i = 0; i < this.orderByRestrictions.size(); i++) {
				String orden = this.orderByRestrictions.get(i);
				bufferOrderBy.append( orden )
				.append( "," );
			}

			//quito la última ,
			bufferOrderBy.deleteCharAt( bufferOrderBy.length() - 1 );
			bufferQuery.append( bufferOrderBy.toString() );
		}
	}

	/**
	 * Genera el HAVING del QUERY
	 */
	protected List<Object> armarHaving(StringBuffer bufferQuery) {
		List<Object> parametrosHaving = new ArrayList<Object>();
		if( this.havingRestriction != null ){
			StringBuffer bufferHaving = new StringBuffer();
			bufferHaving.append( _having )
				.append( havingRestriction.build(parametrosHaving) );

			bufferQuery.append( bufferHaving );
		}
		return parametrosHaving;
	}

	/**
	 * Genera el GROUP BY del QUERY
	 */
	protected void armarGroupBy(StringBuffer bufferQuery) {
		if( !this.groupByRestrictions.isEmpty()){
			StringBuffer bufferGroupBy = new StringBuffer();
			bufferGroupBy.append( _groupBy );

			for (Iterator itGroupByRestrics = this.groupByRestrictions.iterator(); itGroupByRestrics.hasNext();) {
				String group = (String) itGroupByRestrics.next();
				bufferGroupBy.append( group )
					.append( "," );
			}

			//quito la última ,
			bufferGroupBy.deleteCharAt( bufferGroupBy.length() - 1 );
			bufferQuery.append( bufferGroupBy.toString() );
		}
	}

	/**
	 * Genera el WHERE del QUERY
	 */
	protected List<Object> armarWhere(StringBuffer bufferQuery) {
		List<Object> parametrosWhere = new ArrayList<Object>();
		if( this.whereRestriction != null ){
			StringBuffer bufferWhere = new StringBuffer();
			String whereSentencia = whereRestriction.build(parametrosWhere);
			bufferWhere.append( _where )
				.append( whereSentencia );

			bufferQuery.append( bufferWhere.toString() );
		}
		return parametrosWhere;
	}

	/**
	 * Genera el FROM del QUERY
	 */
	protected void armarFrom(StringBuffer bufferQuery) {
		if( !this.fromRestrictions.isEmpty() ){
			StringBuffer bufferFrom = new StringBuffer();
			bufferFrom.append( _from );

			for (Iterator itFromRestrics = this.fromRestrictions.iterator(); itFromRestrics.hasNext();) {
				String table = (String) itFromRestrics.next();
				bufferFrom.append( table )
					.append( "," );
			}

			//quito la última ,
			bufferFrom.deleteCharAt( bufferFrom.length() - 1 );
			bufferQuery.append( bufferFrom.toString() );
		}
	}

	/**
	 * Genera el SELECT del QUERY con sus columnas
	 * @param bufferQuery
	 */
	protected void armarSelect(StringBuffer bufferQuery) {
		bufferQuery.append( _select );

		agregaTipoSelect(bufferQuery);//all,distinct, unique
		agregaTop(bufferQuery);


		if( this.columns != null ){
			StringBuffer bufferColumns = new StringBuffer();

			for (Iterator itColumns = this.columns.iterator(); itColumns.hasNext();) {
				String column = (String) itColumns.next();
				bufferColumns.append( column )
					.append( "," );
			}

			//quito la última ,
			bufferColumns.deleteCharAt( bufferColumns.length() - 1 );
			bufferQuery.append( bufferColumns.toString() );
		}
	}


	/**
	 * Valida el query
	 */
	public void validate(){
		//Si no puso nada en columns
		if( this.columns.isEmpty() ) {
			throw new LnwSqlBadStatementException("LnwQuery invalido: faltan columnas");
		}
		//si no puso la tabla
		if( this.fromRestrictions.isEmpty() ) {
			throw new LnwSqlBadStatementException("LnwQuery invalido: faltan tablas en el from");
		}
	}

	/**
	 * Metodo interno, no utilizar
	 */
	public void addAllColumns(List<String> columns) {
		this.columns.addAll( columns );
	}

	/**
	 * Da la posibilidad de agregar la palabra TOP luego de la palabra
	 * SELECT, en caso de ser necesario.
	 */
	protected void agregaTop(StringBuffer bufferQuery) {
	}


	/**
	 * Da la posilibilidad de agregar LIMIT en la última parte de la sentencia
	 * del QUERY
	 */
	protected void armarLimit(StringBuffer bufferQuery) {
	}

	/**
	 * Da la posibilidad de agregar punto y coma ";" al final de toda la sentencia
	 * del query
	 */
	protected void agregarLastSemiColon(StringBuffer bufferQuery) {
		//bufferQuery.append( ";" ); //FIXME semicolon eliminado, para que funcione en oracle.
	}

	/**
	 * Indica si para agregar columnas con alias se debe utilizar
	 * la palabra AS o no.
	 * <br>Ejemplo con true: <code>SELECT id AS codigo</code>
	 * <br>Ejemplo con false: <code>SELECT id codigo</code>
	 */
	protected boolean agregarAsEnAliasSelect() {
		return true;
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


	public LnwJoin createJoin() {
		return new LnwJoin();
	}

	private void agregaTipoSelect(StringBuffer bufferQuery) {
		bufferQuery.append(selectMode);
	}

	public LnwQuery selectDistinct() {
		selectMode = _select_distinct;
		return this;
	}

	public LnwQuery selectUnique() {
		selectMode = _select_unique;
		return this;
	}

	public void union(LnwQuery query){
		if( this.unions == null ){
			this.unions=new ArrayList<LnwQuery>();
		}
		this.unions.add(query);
	}

}
