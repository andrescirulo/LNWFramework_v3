package net.latin.client.widget.radioButton;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import gwt.material.design.client.ui.MaterialRadioButton;
import net.latin.client.utils.StylesManager;
import net.latin.client.widget.base.LnwWidget;

public class GwtRadioButton extends VerticalPanel implements LnwWidget {

	private List<MaterialRadioButton> childs = new ArrayList<MaterialRadioButton>();

	private static final int HORIZONTAL=0; 
	private static final int VERTICAL=1;

	private static final String MAIN_STYLE = "radio-container";
	
	private int modo=HORIZONTAL;

	private String name;
	
	/**
	 * Constructor del GwtRadioButton. 
	 * @param name
	 */
	public GwtRadioButton(String name) {
		this.name = name;
		this.setStyleName(MAIN_STYLE);
	}

	/**
	 * Adhiere una entrada al gwtradiobutton (adhiere una opcion al grupo creado). El childtext recibido es el texto que se mostrara por pantalla
	 * @param childText
	 */
	public void addChild(String childText){
		addChild(childText,null);
	}
	
	public void addChild(String childText,String color){
		MaterialRadioButton child = new MaterialRadioButton();
		child.setText(childText);
		child.setName(name);
		child.setFormValue(childText);
		if (color!=null){
			setColor(child,color);
		}
		childs.add(child);
		
		if (modo==VERTICAL){
			setEstiloVertical(child);
		}
		this.add(child);
	}
	
	private void setColor(MaterialRadioButton radio,String color){
		String styleName="radio-colored-" + color;
		if (!color.startsWith("#")){
			color="#" + color ;
		}
		
		String style="." + styleName + " [type=\"radio\"]:checked + label:after{" +
				"border-color:" + color+ " !important;" +
				"background-color:" + color+ " !important;" +
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
	public MaterialRadioButton getSelectedChild(){
		for (MaterialRadioButton child:childs){
			if(child.getValue()){
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Resetea el widget. Ninguna opcion queda seleccionada
	 */
	public void resetWidget() {
		for (MaterialRadioButton child:childs){
			child.setValue(false);
		}
	}


	/**
	 * Devuelve el child (MaterialRadioButton) de acuerdo al indice recibido (se respeta el orden en el que fueron agregados)
	 * @param index
	 * @return
	 */
	public MaterialRadioButton getChild(int index){
		return childs.get(index);
	}

	/**
	 * Devuelve true si alguna opcion esta seleccionada. Devuelve false en caso contrario
	 * @return
	 */
	public boolean hasChildSelected(){
		for (MaterialRadioButton child:childs){
			if(child.getValue()){
				return true;
			}
		}
		return false;
	}

	public void setFocus() {
		if(childs.size()>0){
			this.getChild(0).setFocus(true);
		}
	}

	public void addKeyDownHandler(KeyDownHandler listener) {
		for (MaterialRadioButton child:childs){
			child.addDomHandler(listener, KeyDownEvent.getType());
		}
	}
	public void addKeyUpHandler(KeyUpHandler listener) {
		for (MaterialRadioButton child:childs){
			child.addDomHandler(listener, KeyUpEvent.getType());
		}
	}
	public void addKeyPressHandler(KeyPressHandler listener) {
		for (MaterialRadioButton child:childs){
			child.addDomHandler(listener, KeyPressEvent.getType());
		}
	}

	/**
	 * Devuelve el texto del child que está seleccionado
	 * @return selectedChildText
	 */
	public String getSelectedChildText(){
		MaterialRadioButton selected = getSelectedChild();

		if(selected == null){
			return null;
		}

		return selected.getFormValue();
	}

	public void setModoVertical(){
		modo=VERTICAL;
		for (MaterialRadioButton child:childs){
			setEstiloVertical(child);
		}
	}

	private void setEstiloVertical(MaterialRadioButton child) {
		Style childStyle = child.getElement().getStyle();
		childStyle.setDisplay(Display.BLOCK);
		childStyle.setPaddingTop(5, Unit.PX);
		childStyle.setPaddingBottom(5, Unit.PX);
	}
}
