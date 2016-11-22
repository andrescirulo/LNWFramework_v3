package net.latin.client.test.inicio.pages.tabs;

import net.latin.client.utils.ColorUtils;
import net.latin.client.widget.checkbox.GwtCheckBox;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.panels.GwtVerticalPanel;
import net.latin.client.widget.tabs.GwtMaterialTab;

public class CheckBoxTab extends GwtMaterialTab {

	public CheckBoxTab(GwtMensajesHandler handler) {
		super(handler);
		
		GwtCheckBox check=new GwtCheckBox();
		check.setText("Texto del checkbox Azul");
		check.setColor(ColorUtils.BLUE);
		GwtCheckBox check1=new GwtCheckBox();
		check1.setText("Texto del checkbox Rojo");
		check1.setColor(ColorUtils.RED);
		GwtCheckBox check2=new GwtCheckBox();
		check2.setText("Texto del checkbox Amarillo");
		check2.setColor(ColorUtils.YELLOW);
		GwtCheckBox check3=new GwtCheckBox();
		check3.setText("Texto del checkbox Naranja");
		check3.setColor(ColorUtils.ORANGE);
		
		GwtVerticalPanel panelChecks=new GwtVerticalPanel("Checkbox", true);
		panelChecks.add(check);
		panelChecks.add(check1);
		panelChecks.add(check2);
		panelChecks.add(check3);
		this.add(panelChecks);
	}
	
}
