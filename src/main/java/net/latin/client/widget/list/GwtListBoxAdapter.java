package net.latin.client.widget.list;

/**
 * Adapt an object to be shown in a ListBox
 * @author Matias Leone
 *
 */
public interface GwtListBoxAdapter<T> {

	/**
	 * @param itemModel: object to adapt
	 * @return description to be shown in the ListBox
	 */
	public String getListBoxDescription( T itemModel );

}
