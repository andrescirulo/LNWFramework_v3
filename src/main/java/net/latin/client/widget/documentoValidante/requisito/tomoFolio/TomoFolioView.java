package net.latin.client.widget.documentoValidante.requisito.tomoFolio;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.TextAlign;
import net.latin.client.widget.documentoValidante.requisito.display.TomoFolioDisplay;
import net.latin.client.widget.listener.EnterKeyUpHandler;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.separator.GwtTextSpacer;
import net.latin.client.widget.textBox.GwtTextBox;

public class TomoFolioView extends Composite implements TomoFolioDisplay, BlurHandler {
	
	private static final String TEXTBOX_WIDTH = "50px";
	private static final int TOMO_MAX_LENGTH = 4;
	private static final int FOLIO_MAX_LENGTH = 4;
	
	private GwtTextBox tomo = new GwtTextBox();
	private GwtTextBox folio = new GwtTextBox();
	
	// private MagistraturaButton boton = new MagistraturaButton();
	
	public TomoFolioView() {
		tomo.setWidth(TEXTBOX_WIDTH);
		tomo.setMaxLength(TOMO_MAX_LENGTH);
		tomo.addStyleName("tomo-field");
		tomo.addKeyUpHandler(new EnterKeyUpHandler() {
			protected void accionEnter(KeyUpEvent event) {
				folio.setFocus();
			}
		});
		
		tomo.addBlurHandler(this);
		folio.addBlurHandler(this);
		
		folio.setWidth(TEXTBOX_WIDTH);
		folio.setMaxLength(FOLIO_MAX_LENGTH);
		
		HorizontalPanel panel = new HorizontalPanel();
		
		panel.setWidth("120px");
		panel.add(tomo);
		GwtTextSpacer spacer = new GwtTextSpacer("/", 10);
		spacer.getLabelText().getElement().getStyle().setFontSize(1.4, Unit.EM);
		panel.add(spacer);
		panel.add(folio);
		panel.setCellWidth(tomo, TEXTBOX_WIDTH);
		panel.setCellWidth(folio, TEXTBOX_WIDTH);
		panel.setCellVerticalAlignment(spacer, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.add(new GwtHorizontalSpace(20));
		// panel.add(boton);
		initWidget(panel);
		this.setWidth("130px");
	}
	
	public Widget asWidget() {
		return this;
	}
	
	public HasText getFolio() {
		return this.folio;
	}
	
	public HasText getTomo() {
		return this.tomo;
	}
	
	public void setFolioEnabled(boolean b) {
		this.tomo.setEnabled(b);
	}
	
	public void setTomoEnabled(boolean b) {
		this.folio.setEnabled(b);
	}

	public HasKeyUpHandlers getWidgetForNextFocus() {
		return folio;
	}

	public Focusable getFirtsFocusWidget() {
		return tomo;
	}

	public void onBlur(BlurEvent event) {
		if (event.getSource().equals(tomo)){
			formatNumber(tomo);
		}
		if (event.getSource().equals(folio)){
			formatNumber(folio);
		}		
	}
	
	public void formatNumber(GwtTextBox textbox){
		try{
			textbox.setText(Integer.parseInt(textbox.getText())+"");
		}
		catch (Exception e){
		}
	}

	@Override
	public String getWidth() {
		return "130px";
	}
}
