package net.latin.client.widget.checkbox;

import gwt.material.design.client.ui.MaterialCheckBox;
import net.latin.client.utils.StylesManager;
import net.latin.client.widget.base.LnwWidget;

/**
 * CheckBox de LNW
 *
 */
public class GwtCheckBox extends MaterialCheckBox implements LnwWidget {

	
	public void resetWidget() {
		setValue(false);
	}

	public void setFocus() {
		this.setFocus(true);
	}

	
	public void setColor(String color){
		String styleName="checkbox-colored-" + color;
		if (!color.startsWith("#")){
			color="#" + color ;
		}
		
		String style="." + styleName + " > [type=\"checkbox\"]:checked + label:before {"+
		"	border-right: 2px solid " + color +" !important;"+
		"	border-bottom: 2px solid " + color +" !important;"+
		"}";
		
		StylesManager.injectStyle(styleName, style);
		this.addStyleName(styleName);
	}
	
	
}
