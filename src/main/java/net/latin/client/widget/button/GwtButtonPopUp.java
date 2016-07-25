package net.latin.client.widget.button;

import java.util.HashMap;
import java.util.Map;

import net.latin.client.widget.panels.GwtHorizontalPanel;
import net.latin.client.widget.panels.GwtVerticalPanel;
import net.latin.client.widget.separator.GwtHorizontalSpace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtButtonPopUp extends GwtHorizontalPanel {

	private PopupPanel popUpOpciones;
	private GwtVerticalPanel panelOpciones;
	private String PANEL_STYLE="GwtButtonPopUpPanelStyle";
	private String OPTION_STYLE="GwtButtonPopUpOptionStyle";
	private String OPTION_OVER_STYLE="GwtButtonPopUpOptionOverStyle";
	private HorizontalAlignmentConstant popUpAlignment;
	private Map<String,Widget> opciones;
	
	public GwtButtonPopUp(String buttonText) {
		this.setVerticalAlignment(ALIGN_MIDDLE);
		
		Label labelBoton = new Label(buttonText);
		Image flechaBoton = new Image("imagenes/downArrow.png");
		this.add(labelBoton);
		this.add(new GwtHorizontalSpace(2));
		this.add(flechaBoton);
		
		opciones=new HashMap<String, Widget>();
		panelOpciones=new GwtVerticalPanel();
		popUpOpciones=new PopupPanel(true);
		popUpOpciones.setWidget(panelOpciones);
		popUpOpciones.addStyleName(PANEL_STYLE);
		
		this.addClickHandler(new ClickHandler() {
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
		if (ALIGN_RIGHT.equals(popUpAlignment)){
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
