package net.latin.client.widget.documentoValidante.requisito.cuitProveedor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.validation.GwtValidator;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.documentoValidante.GwtLetradoConstants;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.display.CUITProveedorDisplay;
import net.latin.client.widget.msg.GwtMsgHandler;

public class CUITProveedorPresenter extends AbstractRequisitoValidantePresenter<CUITProveedorDisplay> {
	
	
	private static final String CUIT = "cuit";
	
	public CUITProveedorPresenter(CUITProveedorDisplay display) {
		super(display, TiposDocumentoValidante.CUIT_PROVEEDOR);
	}
	
	public boolean validate(GwtMsgHandler msglistener) {
		GwtValidator validator = new GwtValidator();
		validator.addTarget(display.getCUIT().getText())
		.required(GwtLetradoConstants.CUIL_REQUERIDO)
		.numeric(GwtLetradoConstants.CUIL_NUMERICO).cuil(GwtLetradoConstants.CUIL_VALIDO);
		
		return validator.validateAll(msglistener);
	}
	
	public void resetWidget() {
		display.getCUIT().setText("");
	}
	
	public void bind() {
		resetWidget();
		// TODO Attach listeners
	}
	
	public void unbind() {
	}
	
	public CustomBean getValue() {
		CustomBean abogado = new CustomBean();
		abogado.put(CUIT, display.getCUIT().getText());
		return abogado;
	}
	
	public Map<String, String> getStringMapValue() {
		Map<String, String> abogado = new HashMap<String, String>();
		abogado.put(CUIT, display.getCUIT().getText());
		return abogado;
	}
	
	public void setValue(CustomBean abogado) {
		display.getCUIT().setText(abogado.getString(CUIT));
	}
	
//	public String getFooterFormText() {
//		return "Ingrese el CUIL/CUIT";
//	}
	
	public String getLabelFormText() {
		return "CUIT";
	}
	
	public void setEnabled(boolean b) {
		display.setCUITEnabled(b);
	}
	
	public List<String> getKeys() {
		List<String> lista = new ArrayList<String>();
		lista.add(CUIT);
		return lista;
	}
	
	@Override
	public List<String> getColumnNames() {
		List<String> lista = new ArrayList<String>();
		lista.add("CUIL");
		return lista;
	}
	
	public String getBusinessToShow() {
		return display.getCUIT().getText();
	}
	
	public String getDocumentoValidanteString() {
		return "CUIL/CUIT";
	}
	
}
