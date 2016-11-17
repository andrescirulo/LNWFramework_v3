package net.latin.client.widget.gwtswitch;

import gwt.material.design.client.ui.MaterialSwitch;
import net.latin.client.utils.ColorUtils;
import net.latin.client.utils.StylesManager;
import net.latin.client.widget.base.LnwWidget;

public class GwtSwitch extends MaterialSwitch implements LnwWidget {

	@Override
	public void resetWidget() {
		setValue(false);
	}

	@Override
	public void setFocus() {
		this.setFocus(true);
	}

	public void setColor(String color){
		
		String styleName="switch-colored-" + color;
		if (!color.startsWith("#")){
			color="#" + color ;
		}
		
		String lightColor=ColorUtils.cambiarColor(color, 60);
		
		String styleBar="." + styleName + " label input[type=checkbox]:checked + .lever{"+
		"	background-color:" + lightColor + " !important;"+
		"}";
		String styleButton="." + styleName + " label input[type=checkbox]:checked + .lever:after{"+
		"	background-color:" + color + " !important;"+
		"}";
		
		String style=styleBar+styleButton;
		StylesManager.injectStyle(styleName, style);
		this.addStyleName(styleName);
	}
}
