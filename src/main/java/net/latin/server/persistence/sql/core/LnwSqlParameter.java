package net.latin.server.persistence.sql.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Repositorio para un parametro de una sentencia SQL.
 * Antiguamente se utilizaba para serializar a String el valor y poder
 * transmitirlo por un pedido RPC.
 * Actualmente no se utiliza mas la serializacion de sentencias SQL y
 * esta clase se mantiene por compatibilidad hacia, dado que casi todas
 * las restricciones la utilizan.
 * Se almacena el objeto tal cual viene, sin que sufra modificaciones.
 *
 * @author Matias Leone
 */
public class LnwSqlParameter {

	private Object data = null;

	/**
	 * Crea un nuevo parámetro con su valor y tipo cargado.
	 * @param object
	 */
	public LnwSqlParameter(Object object) {
		storeData(object);
	}

	/**
	 * Determina y guarda el tipo del objeto y su valor
	 * @param object
	 */
	private final void storeData(Object object) {
		data = object;
	}

	/**
	 * Carga el valor del parametro
	 */
	public final void setData(Object data) {
		storeData(data);
	}

	/**
	 * Devuelve el valor del parametro, previamente convertido
	 * según su tipo.
	 */
	public final Object getData() {
		return retrieveData();
	}


	/**
	 * Recupera el objeto con su tipo original
	 */
	private final Object retrieveData() {
		return data;
	}

	/**
	 * Adapta un objeto común a un LnwSqlParameter
	 */
	public final static LnwSqlParameter adaptData(Object object) {
		return new LnwSqlParameter(object);
	}

	/**
	 * Recupera un LnwSqlParameter devolviendo un objeto común
	 */
	public final static Object recoverData(LnwSqlParameter parameter) {
		return parameter.getData();
	}

	/**
	 * Recupera un LnwSqlParameter devolviendo un objeto común.
	 * parameter debe ser del tipo LnwSqlParameter
	 */
	public final static Object recoverData(Object parameter) {
		return ((LnwSqlParameter) parameter).getData();
	}

	/**
	 * Adapta una colección común a una colección de LnwSqlParameter
	 */
	public final static Collection<LnwSqlParameter> adaptData(Collection<Object> collection) {
		List<LnwSqlParameter> adapted = new ArrayList<LnwSqlParameter>();;
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			adapted.add( adaptData( iterator.next() ) );
		}
		return adapted;
	}

	/**
	 * Adapta una colección común a una colección de LnwSqlParameter
	 */
	public final static Collection<LnwSqlParameter> adaptData(Object[] array) {
		List<LnwSqlParameter> adapted = new ArrayList<LnwSqlParameter>();
		for (int i = 0; i < array.length; i++) {
			adapted.add( adaptData( array[ i ] ) );
		}
		return adapted;
	}

	/**
	 * Recupera una colleción de LnwSqlParameter, generando una colección de
	 * objetos comunes
	 */
	public final static Collection<Object> recoverData(Collection<LnwSqlParameter> collection) {
		List<Object> recovered = new ArrayList<Object>();
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			recovered.add( ((LnwSqlParameter)iterator.next() ).getData() );
		}
		return recovered;
	}




}
