package net.latin.client.widget.button;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

public class GwtButtonPopUp extends FlowPanel {

	private MaterialDropDown popUpOpciones;
	private Map<String,Widget> opciones;
	
	private String dropDownId;
	
	public GwtButtonPopUp(String buttonText) {
		dropDownId = "opt_btn_" + Random.nextInt();
		MaterialButton btn=new MaterialButton();
		
		btn.setText(buttonText);
		btn.setIconType(IconType.ARROW_DROP_DOWN);
		btn.setIconPosition(IconPosition.RIGHT);
		btn.setType(ButtonType.RAISED);
		
		opciones=new HashMap<String, Widget>();
		popUpOpciones=new MaterialDropDown();
		
		popUpOpciones.setBelowOrigin(true);
		popUpOpciones.setActivator(dropDownId);
		btn.setActivates(dropDownId);
		this.add(btn);
		this.add(popUpOpciones);
		
		popUpOpciones.reinitialize();
	}
	

	public void addOption(String text,ClickHandler listener){
		final MaterialLink boton=new MaterialLink(text);
		boton.addClickHandler(listener);
		
		boton.setWidth("100%");
		popUpOpciones.add(boton);
		opciones.put(text,boton);
		popUpOpciones.reinitialize();
	}
	
	public <T extends Widget & HasClickHandlers> void addOpcion(String id,final T widget){
		widget.setWidth("100%");
		popUpOpciones.add(widget);
		opciones.put(id,widget);
		popUpOpciones.reinitialize();
	}
	
	public void setOptionsWidth(String width){
		popUpOpciones.setWidth(width);
	}
	
	public void setOptionVisible(String key, boolean visible) {
		Widget widg=opciones.get(key);
		if (widg!=null){
			widg.setVisible(visible);
		}
	}
	
	public void clearOptions(){
		opciones.clear();
		popUpOpciones.clear();
	}
	
}
