package net.latin.client.test.inicio.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

import net.latin.client.rpc.GwtAsyncCallback;
import net.latin.client.rpc.GwtRpc;
import net.latin.client.test.inicio.rpc.InicioTestClientAsync;
import net.latin.client.utils.ColorUtils;
import net.latin.client.widget.base.GwtPage;
import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.checkbox.GwtCheckBox;
import net.latin.client.widget.form.GwtForm;
import net.latin.client.widget.radioButton.GwtRadioButton;
import net.latin.client.widget.textBox.GwtValidateTextBox;

public class InicioTestPage extends GwtPage {

	private InicioTestClientAsync server;
	private Label lbl;

	public InicioTestPage() {
		server = (InicioTestClientAsync)GwtRpc.getInstance().getServer( "InicioTestCase" );
		lbl = new Label();
		this.add(lbl);
		GwtValidateTextBox text=new GwtValidateTextBox();
		text.setLabel("Ingresa el texto numerico");
		text.setNumericNatural();
		
		GwtValidateTextBox text2=new GwtValidateTextBox();
//		text2.setLabel("Ingresa el texto");
		text2.setNoLabelFloat(true);
		text2.setTrim();
		
		
		GwtCheckBox check=new GwtCheckBox();
		check.add(new Label("Texto del checkbox"));
		check.setColor(ColorUtils.BLUE);
		
		GwtRadioButton radio=new GwtRadioButton();
		radio.addChild("Negro",ColorUtils.BLACK);
		radio.addChild("Rojo",ColorUtils.RED);
		radio.addChild("Azul",ColorUtils.BLUE);
		radio.addChild("Amarillo",ColorUtils.YELLOW);
		radio.addChild("Naranja",ColorUtils.ORANGE);
		
		GwtForm form=new GwtForm("Titulo");
		form.addSimpleElement(text);
		form.addElement("Texto Prueba",text2);
		form.addSimpleElement(check);
		form.addSimpleElement(radio);
		
		form.addButton(new GwtButton("Aceptar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		}));
		
		this.add(form.render());
	}
	
	protected void onVisible() {
		server.getTextoInicial(new GwtAsyncCallback<SimpleRespuestRPC>() {
			public void onSuccess(SimpleRespuestRPC result) {
				lbl.setText(result.getMensaje());
			}
		});
	}
}
