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
import net.latin.client.widget.button.GwtButtonPopUp;
import net.latin.client.widget.checkbox.GwtCheckBox;
import net.latin.client.widget.dialog.GwtConfirmAbstractListener;
import net.latin.client.widget.dialog.GwtConfirmDialogAceptar;
import net.latin.client.widget.dialog.GwtConfirmDialogSiNo;
import net.latin.client.widget.dialog.GwtConfirmDialogSiNoCancelar;
import net.latin.client.widget.form.GwtForm;
import net.latin.client.widget.modal.GwtModal;
import net.latin.client.widget.panels.GwtHorizontalPanel;
import net.latin.client.widget.panels.GwtVerticalPanel;
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
		
		agregarDialogs(form);
		
		
		GwtButtonPopUp btnPop=new GwtButtonPopUp("Abrir Opciones");
		btnPop.addOption("Opcion 1", new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		btnPop.addOption("Opcion 2", new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		btnPop.addOption("Opcion 3", new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		
		GwtVerticalPanel panelVarios=new GwtVerticalPanel("Varios (Panel vertical)",true);
		panelVarios.add(btnPop);
		
		panelVarios.add(new GwtButton("Bloquear pantalla", new ClickHandler() {
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
		form.addElement(panelVarios);
		
		form.addButton(new GwtButton("Aceptar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		}));
		
		this.add(form.render());
	}
	
	private void agregarDialogs(GwtForm form) {
		GwtHorizontalPanel panel=new GwtHorizontalPanel("Dialogs (Panel horizontal)",true);
		GwtConfirmDialogAceptar aceptarDialog=new GwtConfirmDialogAceptar(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Aceptar de prueba", "Te estoy mostrando un mensaje");
		panel.add(new GwtButton("Dialog Aceptar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				aceptarDialog.showCentered();
			}
		}));
		GwtConfirmDialogSiNo siNoDialog=new GwtConfirmDialogSiNo(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Si/No de prueba", "¿Te estoy mostrando un mensaje?");
		panel.add(new GwtButton("Dialog Si/No", new ClickHandler() {
			public void onClick(ClickEvent event) {
				siNoDialog.showCentered();
			}
		}));
		
		GwtConfirmDialogSiNoCancelar siNoCancelarDialog=new GwtConfirmDialogSiNoCancelar(new GwtConfirmAbstractListener() {
			public void accion(String clickedButton, Object dataObj) {
			}
		}, "Dialog Si/No/Cancelar de prueba", "¿Te estoy mostrando un mensaje?");
		panel.add(new GwtButton("Dialog Si/No/Cancelar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				siNoCancelarDialog.showCentered();
			}
		}));
		
		form.addElement(panel);
	}

	protected void onVisible() {
		server.getTextoInicial(new GwtAsyncCallback<SimpleRespuestRPC>() {
			public void onSuccess(SimpleRespuestRPC result) {
				lbl.setText(result.getMensaje());
			}
		});
	}
}
