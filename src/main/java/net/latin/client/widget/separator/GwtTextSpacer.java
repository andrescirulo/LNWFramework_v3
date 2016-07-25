package net.latin.client.widget.separator;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Horizontal separator with a text in the middle
 *
 * @author Matias Leone
 */
public class GwtTextSpacer extends HorizontalPanel {

	public GwtTextSpacer( String text, int spacing ) {
		Label label1 = new Label();
		label1.setWidth( spacing + "px" );

		Label labelText = new Label( text );

		Label label2 = new Label();
		label2.setWidth( spacing + "px" );

		this.add( label1 );
		this.add( labelText );
		this.add( label2 );
	}
	public GwtTextSpacer( String text, int spacing , String cssStyle) {
		Label label1 = new Label();
		label1.setWidth( spacing + "px" );

		Label labelText = new Label( text );
		labelText.setHeight("100%");
		labelText.addStyleName(cssStyle);

		Label label2 = new Label();
		label2.setWidth( spacing + "px" );

		this.add( label1 );
		this.add( labelText );
		this.add( label2 );
	}
	

}
