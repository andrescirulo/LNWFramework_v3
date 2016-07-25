package net.latin.client.widget.separator;

import com.google.gwt.user.client.ui.Label;

/**
 * Horizontal separator
 *
 * @author Martin D'Aloia
 */
public class GwtHorizontalSpace extends Label {

	/**
	 * Creates an horizontal separator
	 * @param width: width of the separation
	 */
	public GwtHorizontalSpace( int width ) {
		this.setWidth( width + "px" );
	}

}
