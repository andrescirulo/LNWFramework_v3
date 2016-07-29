package net.latin.client.widget.radioButton;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.polymer.paper.widget.PaperRadioButton;
import com.vaadin.polymer.paper.widget.PaperRadioGroup;

import net.latin.client.utils.ColorUtils;
import net.latin.client.utils.StylesManager;
import net.latin.client.widget.base.LnwWidget;

public class GwtRadioButton extends PaperRadioGroup implements LnwWidget {

	private List<PaperRadioButton> childs = new ArrayList<PaperRadioButton>();

	/**
	 * Constructor del GwtRadioButton. 
	 * @param name
	 */
	public GwtRadioButton() {
	}

	/**
	 * Adhiere una entrada al gwtradiobutton (adhiere una opcion al grupo creado). El childtext recibido es el texto que se mostrara por pantalla
	 * @param childText
	 */
	public void addChild(String childText){
		addChild(childText,null);
	}
	
	public void addChild(String childText,String color){
		PaperRadioButton child = new PaperRadioButton();
		child.add(new Label(childText));
		child.setName(childText);
		child.setValue(childText);
		if (color!=null){
			setColor(child,color);
		}
		childs.add(child);
		this.add(child);
	}
	
	private void setColor(PaperRadioButton radio,String color){
		String styleName="radio-colored-" + color;
		if (!color.startsWith("#")){
			color="#" + color ;
		}
		
		String darkColor=ColorUtils.cambiarColor(color, 0.7);
		String style="." + styleName + "[checked] #offRadio.paper-radio-button{" +
				"border-color:" + color+ ";" +
				"}";
		style+="." + styleName + " #offRadio.paper-radio-button{" +
				"border-color:" + darkColor+ ";" +
				"}";
		style+="." + styleName + " #onRadio.paper-radio-button{" +
				"background-color:" + color+ ";" +
				"}";
		
		style+="." + styleName + " #ink[checked].paper-radio-button{"+
				"color:" + darkColor+ ";" +
				"}";
		style+="." + styleName + " #ink.paper-radio-button{"+
				"color:" + darkColor+ ";" +
				"}";
		
		StylesManager.injectStyle(styleName, style);
		radio.addStyleName(styleName);
	}

	/**
	 * Agrega una lista de entradas al radio button. Recibe una lista de Strings (cada entrada)
	 * @param childsToAdd
	 */
	public void addAllChilds(List<String> childsToAdd){
		for (String childToAdd:childsToAdd){
			addChild(childToAdd,childToAdd);
		}
	}

	/**
	 * Devuelve el PaperRadioButton que esté seleccionado (devuelve un PaperRadioButton, no un GwtRadioButton)
	 * @return
	 */
	public PaperRadioButton getSelectedChild(){
		for (PaperRadioButton child:childs){
			if(child.getChecked()){
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Resetea el widget. Ninguna opcion queda seleccionada
	 */
	public void resetWidget() {
		for (PaperRadioButton child:childs){
			child.setChecked(false);
		}
	}


	/**
	 * Devuelve el child (PaperRadioButton) de acuerdo al indice recibido (se respeta el orden en el que fueron agregados)
	 * @param index
	 * @return
	 */
	public PaperRadioButton getChild(int index){
		return childs.get(index);
	}

	/**
	 * Devuelve true si alguna opcion esta seleccionada. Devuelve false en caso contrario
	 * @return
	 */
	public boolean hasChildSelected(){
		for (PaperRadioButton child:childs){
			if(child.getChecked()){
				return true;
			}
		}
		return false;
	}

	public void setFocus() {
		if(childs.size()>0){
			this.getChild(0).setFocused(true);
		}
	}

	public void addKeyDownHandler(KeyDownHandler listener) {
		for (PaperRadioButton child:childs){
			child.addDomHandler(listener, KeyDownEvent.getType());
		}
	}
	public void addKeyUpHandler(KeyUpHandler listener) {
		for (PaperRadioButton child:childs){
			child.addDomHandler(listener, KeyUpEvent.getType());
		}
	}
	public void addKeyPressHandler(KeyPressHandler listener) {
		for (PaperRadioButton child:childs){
			child.addDomHandler(listener, KeyPressEvent.getType());
		}
	}

	/**
	 * Devuelve el texto del child que está seleccionado
	 * @return selectedChildText
	 */
	public String getSelectedChildText(){
		PaperRadioButton selected = getSelectedChild();

		if(selected == null){
			return null;
		}

		return selected.getValue();
	}


}
