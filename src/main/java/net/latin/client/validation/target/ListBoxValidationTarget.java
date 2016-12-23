package net.latin.client.validation.target;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Validation target for ListBox
 *
 * @author Matias Leone
 *
 */
public class ListBoxValidationTarget extends ValidationTarget {

	private ListBox listBox;

	public ListBoxValidationTarget( ListBox listBox ) {
		this.listBox = listBox;
	}

	/**
	 * El combo debe tener al menos un elemento
	 * @param errorMsg
	 * @return
	 */
	public ListBoxValidationTarget required( String errorMsg ) {
		if( isOk() ) {
			if( listBox.getItemCount() == 0 ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * El elemento del combo seleccionado no debe ser el primero,
	 * cuya leyenda generalmente es "Seleccione un elemento".
	 * @param errorMsg
	 * @return
	 */
	public ListBoxValidationTarget validElementSelected( String errorMsg ) {
		if( isOk() ) {
			if( listBox.getSelectedIndex() == 0 ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}


}
