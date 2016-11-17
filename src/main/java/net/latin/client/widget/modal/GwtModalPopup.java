package net.latin.client.widget.modal;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialPreLoader;
import gwt.material.design.client.ui.MaterialSpinner;
import net.latin.client.utils.SupportUtils;
import net.latin.client.widget.base.GwtController;

/**
 * Popup que bloquea la pantalla y muestra un mensaje.
 * Ideal para hacer LOADING.
 *
 */
public class GwtModalPopup extends MaterialModal {

	public final static String CSS_LABEL = "modal-popup-text";
	public final static Integer DEFAULT_ZINDEX=11;

	private FlowPanel panel;
	private HTML msgLabel;
	private MaterialPreLoader spinner;
	
	public GwtModalPopup() {
		super();
		MaterialModalContent mainContainer = new MaterialModalContent();
		panel=new FlowPanel();
		msgLabel = new HTML();
		msgLabel.setStyleName( CSS_LABEL );

		MaterialSpinner subSpinner = new MaterialSpinner();
		spinner = new MaterialPreLoader();
		spinner.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		spinner.add(subSpinner);
		
		msgLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		panel.add( spinner );
		panel.add( msgLabel );
		mainContainer.add(panel);
		getElement().getStyle().setWidth(350, Unit.PX);
		getElement().getStyle().setTextAlign(TextAlign.CENTER);
		this.add(mainContainer);
	}

	public void showPopup( String msg ) {
		panel.clear();
		if (SupportUtils.supportsCssAnimation()){
			panel.add( spinner );
		}
		panel.add( msgLabel );
		msgLabel.setHTML( msg );
		
		RootPanel.get().add(this);
		this.open();
	}
	
	
	public void close() {
		super.close();
		panel.clear();
		this.removeFromParent();
	}
	

	public void showPopup() {
		showPopup( GwtController.defaultI18n.GwtModalPopup_default_msg );
	}

}
