package net.latin.client.widget.documentoValidante.requisito.cuil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.validation.GwtValidator;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.documentoValidante.GwtLetradoConstants;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.display.CUILDisplay;
import net.latin.client.widget.msg.GwtMsgHandler;

public class CUILPresenter extends AbstractRequisitoValidantePresenter<CUILDisplay> {
	
	
	private static final String CUIL = "cuil";
	
	public CUILPresenter(CUILDisplay display) {
		super(display, TiposDocumentoValidante.CUIL);
	}
	
	public boolean validate(GwtMsgHandler msglistener) {
		GwtValidator validator = new GwtValidator();
		validator.addTarget(display.getCUIL().getText())
		.required(GwtLetradoConstants.CUIL_REQUERIDO)
		.numeric(GwtLetradoConstants.CUIL_NUMERICO).cuil(GwtLetradoConstants.CUIL_VALIDO);
		
		return validator.validateAll(msglistener);
	}
	
	public void resetWidget() {
		display.getCUIL().setText("");
	}
	
	public void bind() {
		resetWidget();
		// TODO Attach listeners
	}
	
	public void unbind() {
	}
	
	public CustomBean getValue() {
		CustomBean abogado = new CustomBean();
		abogado.put(CUIL, display.getCUIL().getText());
		return abogado;
	}
	
	public Map<String, String> getStringMapValue() {
		Map<String, String> abogado = new HashMap<String, String>();
		abogado.put(CUIL, display.getCUIL().getText());
		return abogado;
	}
	
	public void setValue(CustomBean abogado) {
		display.getCUIL().setText(abogado.getString(CUIL));
	}
	
//	public String getFooterFormText() {
//		return "Ingrese el CUIL/CUIT";
//	}
	
	public String getLabelFormText() {
		return "CUIL/CUIT";
	}
	
	public void setEnabled(boolean b) {
		display.setCUILEnabled(b);
	}
	
	public List<String> getKeys() {
		List<String> lista = new ArrayList<String>();
		lista.add(CUIL);
		return lista;
	}
	
	@Override
	public List<String> getColumnNames() {
		List<String> lista = new ArrayList<String>();
		lista.add("CUIL");
		return lista;
	}
	
	public String getBusinessToShow() {
		return display.getCUIL().getText();
	}
	
	public String getDocumentoValidanteString() {
		return "CUIL/CUIT";
	}
	
}
