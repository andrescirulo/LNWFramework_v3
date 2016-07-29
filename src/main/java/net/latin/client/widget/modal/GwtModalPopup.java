package net.latin.client.widget.modal;
import net.latin.client.utils.SupportUtils;
import net.latin.client.widget.base.GwtController;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.polymer.paper.widget.PaperDialog;

/**
 * Popup que bloquea la pantalla y muestra un mensaje.
 * Ideal para hacer LOADING.
 *
 */
public class GwtModalPopup extends PaperDialog {

	public final static String CSS_LABEL = "GwtModalPopup";
	public final static Integer DEFAULT_ZINDEX=11;
	private Integer nextZIndex=DEFAULT_ZINDEX;

	private HTML msgLabel;
	public GwtModalPopup() {
		super();
		msgLabel = new HTML();
		msgLabel.setStyleName( CSS_LABEL );
		
		add( msgLabel );
		this.setModal(true);
//		this.getElement().getStyle().setProperty( "zIndex", "11");
//		this.getElement().getStyle().setProperty( "position", "fixed");
	}

	public void showPopup( String msg ) {
		this.resetFit();
		if (SupportUtils.supportsCssAnimation()){
			msg = "<span class=\"fa fa-spinner fa-lg fa-pulse\"></span>&nbsp;" + msg;
		}
		msgLabel.setHTML( msg );
		this.getElement().getStyle().setProperty( "zIndex", nextZIndex+"");
		nextZIndex=DEFAULT_ZINDEX;
		this.fit();
//		this.open();
		this.center();
	}

	public void showPopup() {
		showPopup( GwtController.defaultI18n.GwtModalPopup_default_msg );
	}

	public void setNextZIndex(int zIndex) {
		nextZIndex=zIndex;
	}



}
