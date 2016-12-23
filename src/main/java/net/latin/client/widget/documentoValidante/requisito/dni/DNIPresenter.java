package net.latin.client.widget.documentoValidante.requisito.dni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.validation.GwtValidator;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.documentoValidante.GwtLetradoConstants;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.display.DNIDisplay;
import net.latin.client.widget.msg.GwtMsgFormat;
import net.latin.client.widget.msg.GwtMsgHandler;

public class DNIPresenter extends AbstractRequisitoValidantePresenter<DNIDisplay> {
	
	private static final String DNI = "dni";
	
	private static final int DNI_MINIMO = 0;
	private static final int DNI_MAXIMA = 99999999;
	
	public DNIPresenter(DNIDisplay display) {
		super(display, TiposDocumentoValidante.DNI);
	}
	
	public boolean validate(GwtMsgHandler msglistener) {
		GwtValidator validator = new GwtValidator();
		
		validator.addTarget(display.getDNI().getText()).required(GwtLetradoConstants.DNI_REQUERIDO).numeric(GwtLetradoConstants.DNI_NUMERICO)
				.greater(GwtMsgFormat.getMsg(GwtLetradoConstants.DNI_MAYOR_A, new Integer(DNI_MINIMO)), DNI_MINIMO).smaller(
						GwtMsgFormat.getMsg(GwtLetradoConstants.DNI_MENOR_A, new Integer(DNI_MAXIMA)), DNI_MAXIMA);
		
		return validator.validateAll(msglistener);
	}
	
	public void resetWidget() {
		display.getDNI().setText("");
	}
	
	public void bind() {
		resetWidget();
		// TODO Attach listeners
	}
	
	public void unbind() {
	}
	
	public CustomBean getValue() {
		CustomBean abogado = new CustomBean();
		abogado.put(DNI, display.getDNI().getText());
		return abogado;
	}
	
	public Map<String, String> getStringMapValue() {
		Map<String, String> abogado = new HashMap<String, String>();
		abogado.put(DNI, display.getDNI().getText());
		return abogado;
	}
	
	public void setValue(CustomBean abogado) {
		display.getDNI().setText(abogado.getString(DNI));
	}
	
	public String getLabelFormText() {
		return "DNI";
	}
	
//	public String getFooterFormText() {
//		return "Ingrese el DNI";
//	}
	
	public void setEnabled(boolean b) {
		display.setDNIEnabled(b);
	}
	
	public List<String> getKeys() {
		List<String> lista = new ArrayList<String>();
		lista.add(DNI);
		return lista;
	}
	
	@Override
	public List<String> getColumnNames() {
		List<String> lista = new ArrayList<String>();
		lista.add("DNI");
		return lista;
	}
	
	public String getBusinessToShow() {
		return display.getDNI().getText();
	}
	
	public String getDocumentoValidanteString() {
		return "DNI";
	}
	
}
