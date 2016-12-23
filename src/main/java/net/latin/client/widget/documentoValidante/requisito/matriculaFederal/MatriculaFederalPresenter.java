package net.latin.client.widget.documentoValidante.requisito.matriculaFederal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.validation.GwtValidator;
import net.latin.client.validation.target.TextBoxBaseValidationTarget;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.documentoValidante.GwtLetradoConstants;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.display.TomoFolioDisplay;
import net.latin.client.widget.msg.GwtMsgFormat;
import net.latin.client.widget.msg.GwtMsgHandler;

public class MatriculaFederalPresenter extends AbstractRequisitoValidantePresenter<TomoFolioDisplay> {
	
	private static final String FOLIO = "folio";
	private static final String TOMO = "tomo";
	
	private static final int TOMO_MINIMO = 0;
	private static final int TOMO_MAXIMO = 9999;
	private static final int FOLIO_MINIMO = 0;
	private static final int FOLIO_MAXIMO = 9999;
	
	public MatriculaFederalPresenter(TomoFolioDisplay display) {
		super(display, TiposDocumentoValidante.TOMO_FOLIO);
	}
	
	public boolean validate(GwtMsgHandler msglistener) {
		GwtValidator validator = new GwtValidator();
		
		TextBoxBaseValidationTarget tgt = validator.addTarget(display.getTomo().getText());
		tgt.required(GwtLetradoConstants.TOMO_REQUERIDO);
		tgt.numeric(GwtLetradoConstants.TOMO_NUMERICO);
		tgt.greater(GwtMsgFormat.getMsg(GwtLetradoConstants.TOMO_MAYOR_A, new Integer(TOMO_MINIMO)), TOMO_MINIMO);
		tgt.smaller(GwtMsgFormat.getMsg(GwtLetradoConstants.TOMO_MENOR_A, new Integer(TOMO_MAXIMO)), TOMO_MAXIMO);
		
		tgt = validator.addTarget(display.getFolio().getText());
		tgt.required(GwtLetradoConstants.FOLIO_REQUERIDO);
		tgt.numeric(GwtLetradoConstants.FOLIO_NUMERICO);
		tgt.greater(GwtMsgFormat.getMsg(GwtLetradoConstants.FOLIO_MAYOR_A, new Integer(FOLIO_MINIMO)), FOLIO_MINIMO);
		tgt.smaller(GwtMsgFormat.getMsg(GwtLetradoConstants.FOLIO_MENOR_A, new Integer(FOLIO_MAXIMO)), FOLIO_MAXIMO);
		
		return validator.validateAll(msglistener);
	}
	
	public void resetWidget() {
		display.getTomo().setText("");
		display.getFolio().setText("");
		
		// this.setAgregar( true );
	}
	
	public void bind() {
		resetWidget();
		
		// TODO Attach listeners
	}
	
	public void unbind() {
	}
	
	/**
	 * Retorna un CustomBean con los valores del tomo y el folio con las claves
	 * "tomo" y "folio"
	 */
	public CustomBean getValue() {
		CustomBean abogado = new CustomBean();
		abogado.put(TOMO, display.getTomo().getText());
		abogado.put(FOLIO, display.getFolio().getText());
		return abogado;
	}
	
	public Map<String, String> getStringMapValue() {
		Map<String, String> abogado = new HashMap<String, String>();
		abogado.put(TOMO, display.getTomo().getText());
		abogado.put(FOLIO, display.getFolio().getText());
		return abogado;
	}
	
	public void setValue(CustomBean abogado) {
		display.getTomo().setText(abogado.getString(TOMO));
		display.getFolio().setText(abogado.getString(FOLIO));
	}
	
//	public String getFooterFormText() {
//		return "Ingrese el tomo y folio del abogado";
//	}
	
	public String getLabelFormText() {
		return "Matrícula Federal";
	}
	
	public void setEnabled(boolean b) {
		display.setTomoEnabled(b);
		display.setFolioEnabled(b);
	}
	
	public List<String> getKeys() {
		List<String> lista = new ArrayList<String>();
		lista.add(TOMO);
		lista.add(FOLIO);
		return lista;
	}
	
	public List<String> getColumnNames() {
		List<String> lista = new ArrayList<String>();
		lista.add("Tomo");
		lista.add("Folio");
		return lista;
	}
	
	public String getBusinessToShow() {
		return display.getTomo().getText() + "/" + display.getFolio().getText();
	}
	
	public String getDocumentoValidanteString() {
		return "Tomo/Folio";
	}
	
}
