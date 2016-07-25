package net.latin.server.persistence.sql.oracle;

import net.latin.server.persistence.sql.base.LnwQueryBase;
import net.latin.server.persistence.sql.core.LnwJoin;
import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.RestFcty;

/**
 * Sql Query para Oracle
 * @author Fernando Diaz
 *
 */
public class LnwQueryOracle  extends LnwQueryBase {

	private static final String ROWNUM_FOR_OFFSET_AND_LIMIT = "rownum_for_offset_and_limit";
	public final static String FROM_DUAL = "DUAL";
	protected static final String _rownum = " ROWNUM";
	protected static final String _nextval = " .NEXTVAL";


	/**
	 * Hace sequence.NEXTVAL para obtener el siguiente valor
	 * de una secuencia
	 * @param sequence
	 * @return
	 */
	public LnwQuery addNextValSequence( String sequence ) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( sequence );
		buffer.append( _nextval );
		addColumn( buffer.toString() );
		return this;
	}


	/**
	 * Carga el offset y el limit para hacer un query paginado, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * <p><h3>IMPORTANTE:</h3> El query solo puede tener una única tabla en el FROM
	 * y no debe tener alias. Usar una vista si se necesitan varias tablas.
	 * <p>Se devuelve una nueva instancia de LnwQuery y es esta la que debe ser utilizada
	 * para ejecutar el query, no la original.
	 *
	 * @param query
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit ) {
		return _setOffsetAndLimit(lowerLimit,upperLimit,_rownum );
	}

	/**
	 * Carga el offset y el limit para hacer un query paginado ordenado
	 * por un campo pasado por parametro, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * <p><h3>IMPORTANTE:</h3> El query solo puede tener una única tabla en el FROM
	 * y no debe tener alias. Usar una vista si se necesitan varias tablas.
	 * <p>Se devuelve una nueva instancia de LnwQuery y es esta la que debe ser utilizada
	 * para ejecutar el query, no la original.
	 *
	 * @param query
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder ) {
		return setOffsetAndLimit(lowerLimit,upperLimit,columnOrder,true);
	}

	/**
	 * Carga el offset y el limit para hacer un query paginado ordenado
	 * por un campo pasado por parametro, en base a la informacion
	 * recibida del GwtPaginationTable.
	 * <p><h3>IMPORTANTE:</h3> El query solo puede tener una única tabla en el FROM
	 * y no debe tener alias. Usar una vista si se necesitan varias tablas.
	 * <p>Se devuelve una nueva instancia de LnwQuery y es esta la que debe ser utilizada
	 * para ejecutar el query, no la original.
	 *
	 * @param query
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 */
	public LnwQuery setOffsetAndLimit( int lowerLimit, int upperLimit, String columnOrder, boolean orderAsc ) {
		StringBuffer columnRowNumber = new StringBuffer();
		columnRowNumber.append("row_number() over(order by ");
		columnRowNumber.append(columnOrder);
		if (orderAsc) {
			columnRowNumber.append(" ASC");
		} else {
			columnRowNumber.append(" DESC");
		}
		columnRowNumber.append(")");

		return _setOffsetAndLimit(lowerLimit,upperLimit,columnRowNumber.toString());
	}

	/**
	 * el tercer parametro es la columna/funcion para obtener el numero de columna
	 * @param lowerLimit: arranca en 1, inclusive
	 * @param upperLimit: hasta N, inclusive
	 * @param columnRowNumber
	 * @return
	 */
	private LnwQuery _setOffsetAndLimit( int lowerLimit, int upperLimit,String columnRowNumber ) {
		//ver si es un query del tipo select *
		if( this.isSelectAll() ) {
			this.setSelectAllFromTableInFrom();
		}

		this.addColumn(columnRowNumber,ROWNUM_FOR_OFFSET_AND_LIMIT );

		//creamos un query padre que incorpore al query original como un subselect del from
		LnwQueryOracle father = new LnwQueryOracle();
		father.selectAll();
		father.addFrom(this);
		//en el where del query padre cargamos los limites
		father.setWhere( RestFcty.between( ROWNUM_FOR_OFFSET_AND_LIMIT, new Integer(lowerLimit), new Integer(upperLimit) ) );

		//intercambiamos el hijo por el padre
		return father;
	}

	protected boolean agregarAsEnAliasFrom() {
		return false;
	}

	public LnwJoin createJoin() {
		return new LnwJoinOracle();
	}

}
