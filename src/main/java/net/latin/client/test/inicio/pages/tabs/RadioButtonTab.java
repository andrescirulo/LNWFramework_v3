package net.latin.client.test.inicio.pages.tabs;

import net.latin.client.utils.ColorUtils;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.panels.GwtVerticalPanel;
import net.latin.client.widget.radioButton.GwtRadioButton;
import net.latin.client.widget.tabs.GwtMaterialTab;

public class RadioButtonTab extends GwtMaterialTab {

	public RadioButtonTab(GwtMensajesHandler handler) {
		super(handler);
		
		GwtRadioButton radio=new GwtRadioButton("colores");
		radio.addChild("Negro",ColorUtils.BLACK);
		radio.addChild("Rojo",ColorUtils.RED);
		radio.addChild("Azul",ColorUtils.BLUE);
		radio.addChild("Amarillo",ColorUtils.YELLOW);
		radio.addChild("Naranja",ColorUtils.ORANGE);
		radio.setModoVertical();
		
		GwtVerticalPanel panelRadio=new GwtVerticalPanel("Radio Buttons", true);
		panelRadio.add(radio);
		
		this.add(panelRadio);
	}
}
