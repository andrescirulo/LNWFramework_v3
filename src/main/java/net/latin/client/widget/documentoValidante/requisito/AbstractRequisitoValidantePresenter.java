package net.latin.client.widget.documentoValidante.requisito;

import java.util.List;
import java.util.Map;

import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.mvp.WidgetPresenter;
import net.latin.client.widget.msg.GwtMsgHandler;

public abstract class AbstractRequisitoValidantePresenter <T extends RequisitoValidanteDisplay> 
																		extends WidgetPresenter<T>
																			  implements LnwWidget{
	
	private TiposDocumentoValidante tipoDocumentoValidante;
	private String footer="";

	public AbstractRequisitoValidantePresenter(T display, TiposDocumentoValidante tipoDocumentoValidante) {
		super(display);
		this.tipoDocumentoValidante = tipoDocumentoValidante;
		bind();
	}
	
	public TiposDocumentoValidante getTipoDocumentoValidante() {
		return tipoDocumentoValidante;
	}

	public abstract boolean validate(GwtMsgHandler msglistener);
	
	public abstract CustomBean getValue();
	public abstract Map<String, String> getStringMapValue();
	public abstract void setValue(CustomBean values);

	public abstract String getLabelFormText();
	
	public abstract String getDocumentoValidanteString();

	public abstract void setEnabled(boolean b);
	public abstract void resetWidget();

	public abstract List<String> getKeys();
	public abstract List<String> getColumnNames(); 
	public abstract String getBusinessToShow();

	
	public final void setFocus()
	{
		GwtWidgetUtils.setFocus(display.getFirtsFocusWidget());
	}
	
	public final void setFooter(String footer) {
		this.footer=footer;
	}
	
	public final String getFooterFormText() {
		return footer;
	} 
	
	

	
}
