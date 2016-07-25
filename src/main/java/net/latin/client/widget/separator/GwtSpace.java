package net.latin.client.widget.separator;

import com.google.gwt.user.client.ui.HTML;

/**
 * It ´s a simple white space
 *
 * @author Matias Leone
 *
 */
public class GwtSpace extends HTML {

	public GwtSpace() {
		this.setHTML( "<BR>" );
	}
	
	public GwtSpace( int cantidad ) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < cantidad; i++ ) {
			sb.append( "<BR>" );
		}
		this.setHTML( sb.toString() );
	}

}
