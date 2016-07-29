package net.latin.client.test.inicio.pages;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
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
import net.latin.client.widget.dialog.GwtConfirmAbstractListener;
import net.latin.client.widget.dialog.GwtConfirmDialogAceptar;
import net.latin.client.widget.form.GwtForm;
import net.latin.client.widget.modal.GwtModal;
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
		text.setLabel("Texto numerico (sin Label)");
		text.setNumericNatural();
		
		GwtValidateTextBox text2=new GwtValidateTextBox();
		text2.setNoLabelFloat(true);
		text2.setCharCounter(true);
		text2.setTrim();
		
		GwtValidateTextBox text3=new GwtValidateTextBox();
		text3.setLabel("Textbox con footer");
		text3.setTrim();
		
		
		GwtCheckBox check=new GwtCheckBox();
		check.add(new Label("Texto del checkbox"));
		check.setColor(ColorUtils.BLUE);
		
		GwtRadioButton radio=new GwtRadioButton();
		radio.addChild("Negro",ColorUtils.BLACK);
		radio.addChild("Rojo",ColorUtils.RED);
		radio.addChild("Azul",ColorUtils.BLUE);
		radio.addChild("Amarillo",ColorUtils.YELLOW);
		radio.addChild("Naranja",ColorUtils.ORANGE);
		radio.setModoVertical();
		
		GwtForm form=new GwtForm("Titulo");
		form.addElement(text);
		form.addElementWithLabel("Elemento con label",text2);
		form.addElementWithFooter(text3, "Este elemento tiene footer");
		form.addElement(check);
		form.addSubtitle("Subtitulo para Radios");
		form.addElement(radio);
		
		GwtConfirmDialogAceptar aceptarDialog=new GwtConfirmDialogAceptar(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog de prueba", "Te estoy mostrando un mensaje");
		
		form.addLeftButton(new GwtButton("Dialog Aceptar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				aceptarDialog.showCentered();
			}
		}));
		form.addLeftButton(new GwtButton("Bloquear pantalla", new ClickHandler() {
			public void onClick(ClickEvent event) {
				GwtModal.blockScreen("Bloqueando por 5 segundos");
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
					public boolean execute() {
						GwtModal.unblockScreen();
						return false;
					}
				}, 5000);
			}
		}));
		
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
