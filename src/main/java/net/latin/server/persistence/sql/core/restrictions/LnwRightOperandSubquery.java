package net.latin.server.persistence.sql.core.restrictions;

import java.util.List;

import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.LnwRestriction;
import net.latin.server.persistence.sql.core.PreparedStatement;

/**
 * Abstracción de restricciones con una subquery como operando
 *
 * Ejemplo: select * from tabla where OPERATOR (Subquery)
 *
 * @author Santiago Aimetta
 */
public abstract class LnwRightOperandSubquery implements LnwRestriction {

	private String columna = "";
	private LnwQuery subquery;

	//Constructor requerido por ser serializable
	public LnwRightOperandSubquery() {

	}

	public LnwRightOperandSubquery(String column, LnwQuery subquery ) {
		columna = column;
		this.subquery = subquery;
	}

	public String build(List<Object> parametros) {
		StringBuffer buffer = new StringBuffer();

		//cargar igualación
		if(columna != null) {
			buffer.append( columna );
		}
		buffer.append(" " + getOperator().trim() + " ");

		//construir subQuery
		PreparedStatement buildQuery = subquery.buildQuery();

		//cargar sentencia
		buffer.append( " ( " );
		buffer.append( buildQuery.getSentencia() );
		buffer.append( " ) " );

		//agrega al final lo que se defina en la implementacion
		buffer.append(getEnd());

		//cargar parámetros
		Object[] params = buildQuery.getParams();
		for (int i = 0; i < params.length; i++) {
			parametros.add( params[i] );
		}

		return buffer.toString();
	}

	//Por defecto no agrega nada al final
	protected String getEnd() {
		return "";
	}

	//Hook para la definicion de las restricciones
	protected abstract String getOperator();

}
