package net.latin.client.widget.base;


/**
 * Conjunto de key-value para guardar en la
 * lista repositorio del CustomBean
 *
 * @author Matias Leone
 */
public final class CustomBeanElement extends GwtBusinessObject {

	public String key;
	public CustomBean value;

	/**
	 * Default constructor
	 */
	public CustomBeanElement() {
	}

	public CustomBeanElement(String key, CustomBean value) {
		this.key = key;
		this.value = value;
	}

	public boolean equals(Object obj) {
		if( obj instanceof CustomBeanElement ) {
			final String key2 = ((CustomBeanElement)obj).key;
			return key.equals( key2 );
		} else {
			return false;
		}
	}

	public String toString() {
		return "$" + this.key + "$=" + this.value.toString();
	}
}
