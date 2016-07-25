package net.latin.client.widget.dialog;

import java.util.List;

import net.latin.client.widget.button.GwtButton;

public class GwtConfirmDialogSiNoCancelar extends GwtConfirmDialogAbstract  {
	private static final String BOTON_SI = "Si";
	private static final String BOTON_NO = "No";
	private static final String BOTON_CANCELAR = "Cancelar";
	

	public GwtConfirmDialogSiNoCancelar(GwtConfirmAbstractListener listener,String title, String question) {
		super(listener, title, question);
	}

	@Override
	protected void buildButtons(List<GwtButton> buttons) {
		//boton Si
		buttons.add(new GwtButton( BOTON_SI ));
		//boton No
		buttons.add(new GwtButton( BOTON_NO ));
		//boton cancelar
		buttons.add(new GwtButton( BOTON_CANCELAR ));
	}

	public static boolean isBotonSi(String clickedButton) {
		return BOTON_SI.equals(clickedButton);
	}
	public static boolean isBotonNo(String clickedButton) {
		return BOTON_NO.equals(clickedButton);
	}
	public static boolean isBotonCancelar(String clickedButton) {
		return BOTON_CANCELAR.equals(clickedButton);
	}

}
