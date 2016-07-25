package net.latin.server.utils.exceptions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.latin.server.utils.collections.CollectionFactory;



public class LnwException extends RuntimeException {


	private String message = "";

	private List info = CollectionFactory.createList();

	public LnwException(String message) {
		super(message);
		this.message = message;
	}

	public LnwException(String message, Throwable cause) {
		super(message, cause);
		this.addMessage(message);
	}
	/**
	 * Agrega un string al mensaje existente de la exception
	 */
	public LnwException addMessage(String message) {
		this.message += ((this.message.isEmpty() ? "" : ". ") + message + "\n");
		return this;
	}

	/**
	 * Agrega informaci�n adicional al mensaje de la exception para poder
	 * observar el contexto en el que fue lanzada la exception.
	 *
	 * @param name
	 *            etiqueta que identifica el valor que se desea mostrar
	 * @param value
	 *            estado perteneciente al contexto en el que se lanz� la
	 *            exception
	 */
	public LnwException addInfo(String name, Object value) {
		String string = "\t" + name + ": " + value;
		this.info.add(string);
		return this;
	}

	/**
	 * Copia una lista de infos a la lista de info de esta excepcion.
	 * Permite que al bajar el nivel de abstracion de la excepcion, pueda
	 * obtener todos los mensajes que habian sido agregados.
	 * Se tuvo que bajar el nivel porque en cierto lugar del framework se
	 * tiraba este tipo de exception y deberia de haberse creado una excepcion
	 * de abstraccion media.
	 *
	 * @param infoList Lista con entradas de informacion de contexto de excepcion.
	 */
	public LnwException addInfo(List infoList) {
		this.info.addAll(infoList);
		return this;
	}

	public List getInfo() {
		return this.info;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Iterator iterator = this.info.iterator();
		String stringInfo = "";
		while (iterator.hasNext()) {
			String element = (String) iterator.next();
			stringInfo += element + "\n";
		}
		return this.message + "\n" + stringInfo
				+ ((this.getCause() != null) ? this.getCause().toString() : "");
	}

	/**
	 * Dado un mensaje y una excepcion, si esta es una LnwException se
	 * le agregara el mensaje recbido, de lo contrario creara una
	 * LnwException que wrapea a la exception recibida.
	 */
	public static LnwException wrap(String message, Throwable exception) {
		if (exception instanceof LnwException) {
			return ((LnwException) exception).addMessage(message);
		}
		return new LnwException(message, exception);
	}

	// ************************************************************************************************************
	// Asserts
	// ************************************************************************************************************

	public static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new LnwException(message);
		}
	}

	public static void assertFalse(boolean condition, String message) {
		assertTrue(!condition, message);
	}

	public static void assertNull(Object obj, String message) {
		assertTrue(obj == null, message);
	}

	public static void assertNotNull(Object obj, String message) {
		assertTrue(!(obj == null), message);
	}

	public static void assertEquals(Object obj1, Object obj2, String message) {
		assertTrue(((obj1 == null) && (obj2 == null)) || (obj1 != null)
				&& (obj1.equals(obj2)), message);
	}

	public static void assertContains(Collection col, Object obj, String message) {
		assertTrue((col != null) && col.contains(obj), message);
	}

	/**
	 * @param exception
	 */
	public static LnwException wrap(Exception exception) {
		return LnwException.wrap("", exception);
	}

}
