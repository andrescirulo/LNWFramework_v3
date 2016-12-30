package net.latin.client.widget.form;

public interface GwtDataFormManager<T> {

	/**
	 * @return devuelve un nuevo objeto de datos con los valores por default
	 */
	public T loadDefaultValues( );

	/**
	 * Toma los valores del formulario visual y los carga
	 * en el objeto de datos
	 * @param dataObj
	 */
	public void loadDataObject( GwtForm<T> form, T dataObj );

	/**
	 * Toma los valores del objeto de datos y los carga
	 * en el formulario visual
	 * @param dataObj
	 */
	public void loadForm( GwtForm<T> form, T dataObj );

	/**
	 * Carga solamente el formulario, con valores por default
	 * @param dataObj
	 */
	public void loadVisualDefaultForm( GwtForm<T> form );

}
