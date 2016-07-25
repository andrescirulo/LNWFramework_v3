package net.latin.server.persistence.sql.core;

import net.latin.server.persistence.sql.core.restrictions.LnwAnd;
import net.latin.server.persistence.sql.core.restrictions.LnwBetween;
import net.latin.server.persistence.sql.core.restrictions.LnwEfalse;
import net.latin.server.persistence.sql.core.restrictions.LnwEfalseSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwEquals;
import net.latin.server.persistence.sql.core.restrictions.LnwEqualsJoin;
import net.latin.server.persistence.sql.core.restrictions.LnwEqualsSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwEtrue;
import net.latin.server.persistence.sql.core.restrictions.LnwEtrueSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwGe;
import net.latin.server.persistence.sql.core.restrictions.LnwGeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwGt;
import net.latin.server.persistence.sql.core.restrictions.LnwGtSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwIJoin;
import net.latin.server.persistence.sql.core.restrictions.LnwIlike;
import net.latin.server.persistence.sql.core.restrictions.LnwIlikeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwInArray;
import net.latin.server.persistence.sql.core.restrictions.LnwInSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwIsNull;
import net.latin.server.persistence.sql.core.restrictions.LnwIsNullSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwLJoin;
import net.latin.server.persistence.sql.core.restrictions.LnwLe;
import net.latin.server.persistence.sql.core.restrictions.LnwLeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwLike;
import net.latin.server.persistence.sql.core.restrictions.LnwLikeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwLt;
import net.latin.server.persistence.sql.core.restrictions.LnwLtSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwMultipleAnd;
import net.latin.server.persistence.sql.core.restrictions.LnwMultipleCustomRestriction;
import net.latin.server.persistence.sql.core.restrictions.LnwMultipleOr;
import net.latin.server.persistence.sql.core.restrictions.LnwNEquals;
import net.latin.server.persistence.sql.core.restrictions.LnwNILike;
import net.latin.server.persistence.sql.core.restrictions.LnwNIlikeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwNLike;
import net.latin.server.persistence.sql.core.restrictions.LnwNLikeSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwNot;
import net.latin.server.persistence.sql.core.restrictions.LnwNotInSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwNotNull;
import net.latin.server.persistence.sql.core.restrictions.LnwNotNullSubquery;
import net.latin.server.persistence.sql.core.restrictions.LnwOr;
import net.latin.server.persistence.sql.core.restrictions.LnwQueryText;
import net.latin.server.persistence.sql.core.restrictions.LnwRJoin;

/**
 * Factory de restricciones
 * Crea las restricciones LnwRestriction de manera
 * static para agilizar se creación y reducir la dependencia
 * léxica.... q comentario..por Dios!
 *
 *
 * @author Santiago Aimetta
 */
public class RestFcty {

	private RestFcty() {
	}

	/**
	 * Equals
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction eq( String column,Object value){
		return new LnwEquals(column,value);
	}

	/**
	 * Funciona como un equals de sql para hacer JOIN de dos
	 * tablas en el where
	 * Ej. columna1 = columna2
	 *
	 * @author Matias Leone
	 */
	public static LnwRestriction eqJoin( String column1,String column2){
		return new LnwEqualsJoin(column1,column2);
	}

	/**
	 * Equals contra una subquery
	 * column = (select)
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction eqSq( String column, LnwQuery query ){
		return new LnwEqualsSubquery( column, query );
	}

	/**
	 * And
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static LnwRestriction and( LnwRestriction r1,LnwRestriction r2){
		return new LnwAnd(r1,r2);
	}

	/**
	 * Like
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction like(String column, Object value){
		return new LnwLike(column,value);
	}

	/**
	 * Like contra una subquery. Para setear el match mode al agregar la columna
	 * a la subquery concatenar con %. Ejemplo ORACLE: sub.addColumn("'%'|| nombre ||'%'" );
	 *
	 * eso resultará en: SELECT '%'|| nombre ||'%'
	 *
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction likeSq(String column, LnwQuery subquery){
		return new LnwLikeSubquery(column,subquery);
	}

	/**
	 * NOT Like
	 * Verifica si dos caracteres son coincidentes Ej. columna NOT Like
	 * (select) <-- Subquery que devuelve una cadena
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction notLikeSq(String column, LnwQuery subquery){
		return new LnwNLikeSubquery(column,subquery);
	}

	/**
	 * >=
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction ge(String column,Object value){
		return new LnwGe(column, value);
	}

	/**
	 * >=
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction geSubquery(String column,LnwQuery subquery){
		return new LnwGeSubquery(column, subquery);
	}

	/**
	 * >
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction gt(String column,Object value){
		return new LnwGt(column, value);
	}

	/**
	 * Resticcion de mayor contra una subquery
	 * column > (select)
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction gtSq(String column,LnwQuery subquery){
		return new LnwGtSubquery(column, subquery);
	}

	/**
	 * InnerJoin
	 * @param column1
	 * @param column2
	 * @return
	 */
	public static LnwRestriction ij(String column1,String column2){
		return new LnwIJoin(column1,column2);
	}


	/**
	 * Ilike
	 * @param columna
	 * @param valor
	 * @return
	 */
	public static LnwRestriction ilike(String columna,Object valor){
		return new LnwIlike(columna,valor);
	}

	/**
	 *ILike contra una subquery. Para setear el match mode al agregar la columna
	 * a la subquery concatenar con %. Ejemplo ORACLE: sub.addColumn("'%'|| nombre ||'%'" );
	 *
	 * eso resultará en: SELECT '%'|| nombre ||'%'
	 *
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction ilikeSq(String columna,LnwQuery subquery){
		return new LnwIlikeSubquery(columna,subquery);
	}

	/**
	 * NOT LIKE LOWER contra una subquery
	 * Ejemplo: select * from clientes where id NOT LIKE LOWER (select nombre from deudores);
	 * @param columna
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction notIlikeSq(String columna,LnwQuery subquery){
		return new LnwNIlikeSubquery(columna,subquery);
	}

	/**
	 * <=
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction le(String column,Object value){
		return new LnwLe(column, value);
	}

	/**
	 * Funciona como un  menor equals de sql
	 * Ej. Columna <= (select)
	 *
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction leSq(String column,LnwQuery subquery){
		return new LnwLeSubquery(column, subquery);
	}

	/**
	 * LeftJoin
	 * @param column1
	 * @param column2
	 * @return
	 */
	public static LnwRestriction lj(String column1,String column2){
		return new LnwLJoin(column1,column2);
	}


	/**
	 * <
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction lt(String column,Object value){
		return new LnwLt(column, value);
	}

	/**
	 * Restriccion de menor contra una subquery
	 * column < (select)
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction ltSq(String column,LnwQuery subquery){
		return new LnwLtSubquery(column, subquery);
	}

	/**
	 * NOT Equals
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction neq( String column,Object value){
		return new LnwNEquals(column,value);
	}

	/**
	 * NOT Equals contra una subquery
	 * column != (select)
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction neqSq( String column,Object value){
		return new LnwNEquals(column,value);
	}

	/**
	 * NOT Like
	 * @param column
	 * @param value
	 * @return
	 */
	public static LnwRestriction nlike(String column, Object value){
		return new LnwNLike(column,value);
	}

	/**
	 *Not Like contra una subquery. Para setear el match mode al agregar la columna
	 * a la subquery concatenar con %. Ejemplo ORACLE: sub.addColumn("'%'|| nombre ||'%'" );
	 *
	 * eso resultará en: SELECT '%'|| nombre ||'%'
	 *
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction nlikeSq(String column, LnwQuery subquery){
		return new LnwNLikeSubquery(column,subquery);
	}



	/**
	 * NOT Ilike
	 * @param columna
	 * @param valor
	 * @return
	 */
	public static LnwRestriction nilike(String columna,Object valor){
		return new LnwNILike(columna,valor);
	}

	/**
	 * Not ILike contra una subquery. Para setear el match mode al agregar la columna
	 * a la subquery concatenar con %. Ejemplo ORACLE: sub.addColumn(" '%' || nombre || '%' " );
	 *
	 * eso resultará en: SELECT '%'|| nombre ||'%'
	 *
	 * @param column
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction nilikeSq(String columna,LnwQuery subquery){
		return new LnwNIlikeSubquery(columna,subquery);
	}

	/**
	 * or
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static LnwRestriction or( LnwRestriction r1,LnwRestriction r2){
		return new LnwOr(r1,r2);
	}

	/**
	 * RigthJoin
	 * @param column1
	 * @param column2
	 * @return
	 */
	public static LnwRestriction rj(String column1,String column2){
		return new LnwRJoin(column1,column2);
	}

	/**
	 * Multiple And
	 * @return
	 */
	public static LnwMultipleAnd multipleAnd(){
		return new LnwMultipleAnd();
	}

	/**
	 * Multiple Or
	 * @return
	 */
	public static LnwMultipleOr multipleOr() {
		return new LnwMultipleOr();
	}


	/**
	 * Permite crear una restricción con nexos
	 * and y or
	 * @return
	 */
	public static LnwMultipleCustomRestriction customAndOr() {
		return new LnwMultipleCustomRestriction();
	}

	/**
	 * Permite crear una restricción not null
	 * @param columna
	 * @return
	 */
	public static LnwRestriction notNull(String columna){
		return new LnwNotNull(columna);
	}

	/**
	 * Permite crear una restricción not null contra el resultado de una subquery
	 * (select) is not null
	 * @param columna
	 * @return
	 */
	public static LnwRestriction notNullSq(LnwQuery subquery){
		return new LnwNotNullSubquery(subquery);
	}

	/**
	 * Permite crear una restricción is null
	 * @param columna
	 * @return
	 */
	public static LnwRestriction isNull(String columna){
		return new LnwIsNull(columna);
	}

	/**
	 * Permite crear una restricción is null contra el resultado de una subquery
	 * @param columna
	 * @return
	 */
	public static LnwRestriction isNullSq(LnwQuery subquery){
		return new LnwIsNullSubquery(subquery);
	}

	/**
	 * Premite crear una restricción para chequear si
	 * un booleano es true
	 * @param columna
	 * @return
	 */
	public static LnwRestriction isTrue(String columna){
		return new LnwEtrue(columna);
	}


	/**
	 * Funciona como un equals true de sql, contra un Subquery
	 * Ej. 1 = (select ...)
	 *
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction isTrueSq(LnwQuery subquery){
		return new LnwEtrueSubquery(subquery);
	}

	/**
	 * Premite crear una restricción para chequear si
	 * un booleano es false
	 *
	 * @param columna
	 * @return
	 */
	public static LnwRestriction isFalse(String columna){
		return new LnwEfalse(columna);
	}

	/**
	 * Funciona como un equals false de sql, contra un Subquery
	 * Ej. 1 != (select ...)
	 *
	 * @param subquery
	 * @return
	 */
	public static LnwRestriction isFalseSq(LnwQuery subquery){
		return new LnwEfalseSubquery(subquery);
	}

	/**
	 * Premite crear una llamada a una funcion y enviarle parametros
	 * @param functionName nombre de la funcion
	 * @return
	 */
	public static LnwRestrictionFunction function( String functionName ){
		return new LnwRestrictionFunction( functionName );
	}

	/**
	 * NOT (restriction)
	 */
	public static LnwRestriction not( LnwRestriction restriction ) {
		return new LnwNot( restriction );
	}

	/**
	 * column IN (array_values)
	 */
	public static LnwInArray inArray( String column ) {
		return new LnwInArray( column );
	}

	/**
	 * column IN (subselect)
	 */
	public static LnwInSubquery inSq(String column, LnwQuery subquery) {
		return new LnwInSubquery( column, subquery );
	}

	/**
	 * column NOT IN (subselect)
	 */
	public static LnwNotInSubquery notInSq(String column, LnwQuery subquery) {
		return new LnwNotInSubquery( column, subquery );
	}

	/**
	 * column BETWEEN value1 AND value2
	 */
	public static LnwRestriction between( String column, Object value1, Object value2 ) {
		return new LnwBetween(column, value1, value2);
	}
	/**
	 * column BETWEEN value1 AND (select)
	 */
	public static LnwRestriction between( String column, Object value, LnwQuery subquery) {
		return new LnwBetween(column, value, subquery);
	}
	/**
	 * column BETWEEN subquery AND value1
	 */
	public static LnwRestriction between( String column, LnwQuery subquery, Object value ) {
		return new LnwBetween(column, subquery, value);
	}
	/**
	 * column BETWEEN subquery1 AND subquery2
	 */
	public static LnwRestriction between( String column, LnwQuery subquery1, LnwQuery subquery2 ) {
		return new LnwBetween(column, subquery1, subquery2);
	}

	/**
	 * Restriccion de texto libre para crear una condicion con el SQL que
	 * sea necesario.
	 * Solo utilizar en caso de que el resto de las restricciones no alcancen
	 * para cumplir la consulta.
	 * El texto debe ser debe pasar los parametros del SQL con signos de pregunta.
	 * Ej: funcionX(?,?) > 5
	 */
	public static LnwQueryText queryText(String sqlText) {
		return new LnwQueryText(sqlText);
	}

}
