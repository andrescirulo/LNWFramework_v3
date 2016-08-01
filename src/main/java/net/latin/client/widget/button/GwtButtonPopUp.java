package net.latin.client.widget.button;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperButton;

import net.latin.client.widget.panels.GwtHorizontalPanel;
import net.latin.client.widget.panels.GwtVerticalPanel;

public class GwtButtonPopUp extends GwtHorizontalPanel {

	private PopupPanel popUpOpciones;
	private GwtVerticalPanel panelOpciones;
	private String PANEL_STYLE="GwtButtonPopUpPanelStyle";
	private String OPTION_STYLE="GwtButtonPopUpOptionStyle";
	private String OPTION_OVER_STYLE="GwtButtonPopUpOptionOverStyle";
	private HorizontalAlignmentConstant popUpAlignment;
	private Map<String,Widget> opciones;
	
	public GwtButtonPopUp(String buttonText) {
//		this.setVerticalAlignment(ALIGN_MIDDLE);
		PaperButton btn=new PaperButton();
		
		Label labelBoton = new Label(buttonText);
		labelBoton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		IronIcon flechaBoton = new IronIcon();
		flechaBoton.setIcon("icons:expand-more");
		flechaBoton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
		btn.add(labelBoton);
		btn.add(flechaBoton);
		btn.setRaised(true);
		btn.getElement().getStyle().setMargin(0, Unit.PX);
		btn.getElement().getStyle().setPaddingTop(0.47, Unit.EM);
		btn.getElement().getStyle().setPaddingBottom(0.47, Unit.EM);
		btn.getElement().getStyle().setPaddingLeft(0.57, Unit.EM);
		btn.getElement().getStyle().setPaddingRight(0.57, Unit.EM);
		this.add(btn);
		
		opciones=new HashMap<String, Widget>();
		panelOpciones=new GwtVerticalPanel();
		popUpOpciones=new PopupPanel(true);
		popUpOpciones.setWidget(panelOpciones);
		popUpOpciones.addStyleName(PANEL_STYLE);
		
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				popUpOpciones.show();
				setPosicion();
				
			}
		});
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				if (popUpOpciones.isVisible()){
					setPosicion();
				}
			}
		});
	}
	
	protected void setPosicion() {
		if (HasHorizontalAlignment.ALIGN_RIGHT.equals(popUpAlignment)){
			int left=getAbsoluteLeft()+getOffsetWidth()-popUpOpciones.getOffsetWidth();
			popUpOpciones.setPopupPosition(left, getAbsoluteTop()+getOffsetHeight());
		}
		else
		{
			popUpOpciones.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop()+getOffsetHeight());
		}
	}

	public void addOption(String text,ClickHandler listener){
		final GwtButton boton=new GwtButton(text);
		boton.setStyleName(OPTION_STYLE);
		boton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boton.removeStyleName(OPTION_OVER_STYLE);
				popUpOpciones.hide();
			}
		});
		boton.addClickHandler(listener);
		
		boton.setWidth("100%");
		panelOpciones.add(boton);
		opciones.put(text,boton);
	}
	
	public <T extends Widget & HasClickHandlers> void addOpcion(String id,final T widget){
		widget.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				popUpOpciones.hide();
			}
		});
		
		widget.setWidth("100%");
		panelOpciones.add(widget);
		opciones.put(id,widget);
	}
	
	public void setOptionsWidth(String width){
		panelOpciones.setWidth(width);
	}
	
	/**
	 * Setea como se alinea el popup respecto al boton
	 */
	 
	public void setPopUpAlignment(HorizontalAlignmentConstant alignment){
		this.popUpAlignment = alignment;
	}

	public void setOptionVisible(String key, boolean visible) {
		Widget widg=opciones.get(key);
		if (widg!=null){
			widg.setVisible(visible);
		}
	}
	
	public void clearOptions(){
		opciones.clear();
		panelOpciones.clear();
	}
	
}
