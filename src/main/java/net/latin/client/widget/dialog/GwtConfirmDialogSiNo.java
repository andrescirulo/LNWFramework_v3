package net.latin.client.widget.dialog;

import java.util.List;

import net.latin.client.widget.button.GwtButton;

public class GwtConfirmDialogSiNo extends GwtConfirmDialogAbstract  {
	private static final String BOTON_SI = "Si";
	private static final String BOTON_NO = "No";
	

	public GwtConfirmDialogSiNo(GwtConfirmAbstractListener listener,String title, String question) {
		super(listener, title, question);
	}
	public GwtConfirmDialogSiNo(GwtConfirmAbstractListener listener,String title) {
		super(listener, title,"");
	}

	@Override
	protected void buildButtons(List<GwtButton> buttons) {
		//boton No
		buttons.add(new GwtButton( BOTON_NO ));
		//boton Si
		buttons.add(new GwtButton( BOTON_SI ));
	}
	public static boolean isBotonSi(String clickedButton) {
		return BOTON_SI.equals(clickedButton);
	}
	public static boolean isBotonNo(String clickedButton) {
		return BOTON_NO.equals(clickedButton);
	}

}
