package net.latin.client.widget.dialog;

import java.util.List;

import net.latin.client.widget.button.GwtButton;

public class GwtConfirmDialogAceptar extends GwtConfirmDialogAbstract  {
	private static final String BUTTON_ACEPTAR = "Aceptar";

	public GwtConfirmDialogAceptar(GwtConfirmAbstractListener listener,String title, String question) {
		super(listener, title, question);
	}
	@Override
	protected void buildButtons(List<GwtButton> buttons) {
		//boton aceptar
		buttons.add(new GwtButton( BUTTON_ACEPTAR ));
		
	}

}
