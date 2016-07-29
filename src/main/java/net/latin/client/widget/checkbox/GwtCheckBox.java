package net.latin.client.widget.checkbox;

import com.vaadin.polymer.paper.widget.PaperCheckbox;

import net.latin.client.utils.ColorUtils;
import net.latin.client.utils.StylesManager;
import net.latin.client.widget.base.LnwWidget;

/**
 * CheckBox de LNW
 *
 * @author Matias Leone
 */
public class GwtCheckBox extends PaperCheckbox implements LnwWidget {

	public void resetWidget() {
		setChecked(false);
	}

	public void setFocus() {
		this.setFocused(true);
	}

	
	public void setColor(String color){
		
		String styleName="checkbox-colored-" + color;
		if (!color.startsWith("#")){
			color="#" + color ;
		}
		
		String darkColor=ColorUtils.cambiarColor(color, 0.7);
		String style="." + styleName + " #checkbox.paper-checkbox{"+
		"	border-color: " + darkColor +";"+
		"}"+
		"." + styleName + " #checkbox.checked.paper-checkbox{"+
		"	background-color:" + color + ";"+
		"	border-color: " + color + ";"+
		"}"+
		"." + styleName + " #ink[checked].paper-checkbox,.checkbox-orange #ink.paper-checkbox{"+
		"	color: " + darkColor + ";"+
		"}";
		
		StylesManager.injectStyle(styleName, style);
		this.addStyleName(styleName);
	}
	
	
}
