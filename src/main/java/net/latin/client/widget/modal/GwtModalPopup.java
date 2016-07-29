package net.latin.client.widget.modal;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.polymer.paper.widget.PaperDialog;
import com.vaadin.polymer.paper.widget.PaperSpinner;

import net.latin.client.utils.SupportUtils;
import net.latin.client.widget.base.GwtController;

/**
 * Popup que bloquea la pantalla y muestra un mensaje.
 * Ideal para hacer LOADING.
 *
 */
public class GwtModalPopup extends PaperDialog {

	public final static String CSS_LABEL = "modal-popup-text";
	public final static Integer DEFAULT_ZINDEX=11;

	private FlowPanel panel;
	private HTML msgLabel;
	private PaperSpinner spinner;
	public GwtModalPopup() {
		super();
		panel=new FlowPanel();
		msgLabel = new HTML();
		msgLabel.setStyleName( CSS_LABEL );
		spinner = new PaperSpinner();
		spinner.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		msgLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		panel.add( spinner );
		panel.add( msgLabel );
		this.add(panel);
		this.setModal(true);
	}

	public void showPopup( String msg ) {
		this.resetFit();
		panel.clear();
		if (SupportUtils.supportsCssAnimation()){
			panel.add( spinner );
			spinner.setActive(true);
		}
		panel.add( msgLabel );
		
		msgLabel.setHTML( msg );
		this.removeFromParent();
		RootPanel.get().add(this);
		this.open();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				refit();
			}
		});
	}
	
	@Override
	public void close() {
		super.close();
		if (SupportUtils.supportsCssAnimation()){
			spinner.setActive(false);
		}
	}

	public void showPopup() {
		showPopup( GwtController.defaultI18n.GwtModalPopup_default_msg );
	}

}
