package net.latin.client.widget.separator;

import net.latin.client.rpc.GwtBusinessException;
import net.latin.client.widget.base.LnwWidget;

import com.google.gwt.user.client.ui.Label;

/**
 * Linea separadora Horizontal o Vertical
 *
 * @author Matias Leone
 */
public class GwtSeparator extends Label implements LnwWidget {

	private static final String CSS = "GwtSeparator";

	/**
	 * Separador Horizontal
	 */
	public static final int HORIZONTAL = 0;

	/**
	 * Separador Vertical
	 */
	public static final int VERTICAL = 1;

	/**
	 * Separador Horizontal-Vertical
	 */
	public static final int BOTH = 2;

	public static final int OTHER_LENGTH = 2;

	public GwtSeparator( int type, int length ) {
		if( type == HORIZONTAL ) {
			this.create( length, OTHER_LENGTH );
		} else if ( type == VERTICAL ) {
			this.create( OTHER_LENGTH, length );
		} else {
			throw new GwtBusinessException( "Tipo ingresado inválido para una única longitud." );
		}
	}

	public GwtSeparator( int type, int width, int heigth ) {
		this.create(width, heigth);
	}

	private void create(int width, int heigth) {
		setStyleName(CSS);

		String widthPx = width + "px";
		String heigthPx = heigth + "px";

		setSize( widthPx, heigthPx );
	}

	public void resetWidget() {
	}

	public void setFocus() {
	}

}
