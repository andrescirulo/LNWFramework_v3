package net.latin.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Objeto que contiene atributos, los cuales pueden ser utilizados por todos los steps
 * dentro de un unico caso de uso.
 *
 */
public class Context  implements Serializable{

	private static final long serialVersionUID = 7218028655198265952L;
	private final Map<String, Object> map = new HashMap<String, Object>();;

	// ===================//
	/**
	 * Devuelve el atributo especificado si existe, sino devuelve null
	 */
	public final Object getAttribute(String name) {
		return map.get( name );
	}
	// ===================//
	/**
	 * Agrega el atributo especificado. Si ya existe lo sobrescribe
	 */
	public final void setAttribute(String name, Object obj) {
		map.put( name, obj );
	}
	// ===================//
	/**
	 * Quita el atributo seleccionado, solo si existe
	 */
	public final Object removeAttribute( String name ) {
		return map.remove( name );
	}
	// ===================//
	/**
	 * Devuele true si el atributo especificado por name existe, sino devuelve
	 * false
	 */
	public final boolean hasAttribute( String name ) {
		return map.containsKey( name );
	}
	// ==========================//
	/**
	 * Borra todos los atributos existentes
	 */
	public final void clearAtrributes() {
		map.clear();
	}
	// ==========================//
	/**
	 * Devuelve true si el contexto posee algun atributo, o false en caso contrario
	 */
	public final boolean hasSomething() {
		return this.map.size() > 0;
	}
	// ==========================//
	/**
	 * Agrega todos los atributos de un contexto al propio contexto, si es que
	 * existe alguno
	 */
	public final void addAll( Context otherContext ) {
		this.map.putAll( otherContext.map );
	}
	// ==========================//


	@Override
	public String toString() {
		return this.map.toString();
	}


}
