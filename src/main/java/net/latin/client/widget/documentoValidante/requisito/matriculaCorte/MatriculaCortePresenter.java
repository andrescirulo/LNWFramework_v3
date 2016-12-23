package net.latin.client.widget.documentoValidante.requisito.matriculaCorte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.validation.GwtValidator;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.documentoValidante.GwtLetradoConstants;
import net.latin.client.widget.documentoValidante.TiposDocumentoValidante;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.display.MatriculaCorteDisplay;
import net.latin.client.widget.msg.GwtMsgFormat;
import net.latin.client.widget.msg.GwtMsgHandler;

public class MatriculaCortePresenter extends AbstractRequisitoValidantePresenter<MatriculaCorteDisplay> {
	
	private static final String MATRICULA_CORTE = "matriculaCorte";
	
	private static final int MATRICULA_MINIMA = 0;
	private static final int MATRICULA_MAXIMA = 999999;
	
	public MatriculaCortePresenter(MatriculaCorteDisplay display) {
		super(display, TiposDocumentoValidante.DNI);
	}
	
	public boolean validate(GwtMsgHandler msglistener) {
		GwtValidator validator = new GwtValidator();
		
		validator.addTarget(display.getMatriculaCorte().getText())
			.required(GwtLetradoConstants.MATRICULA_CORTE_REQUERIDA)
			.numeric(GwtLetradoConstants.MATRICULA_CORTE_NUMERICA)
			.greater(GwtMsgFormat.getMsg(GwtLetradoConstants.MATRICULA_CORTE_MAYOR_A, new Integer(MATRICULA_MINIMA)), MATRICULA_MINIMA)
			.smaller(GwtMsgFormat.getMsg(GwtLetradoConstants.MATRICULA_CORTE_MENOR_A, new Integer(MATRICULA_MAXIMA)), MATRICULA_MAXIMA);
		
		return validator.validateAll(msglistener);
	}
	
	public void resetWidget() {
		display.getMatriculaCorte().setText("");
	}
	
	public void bind() {
		resetWidget();
		// TODO Attach listeners
	}
	
	public void unbind() {
	}
	
	public CustomBean getValue() {
		CustomBean abogado = new CustomBean();
		abogado.put(MATRICULA_CORTE, display.getMatriculaCorte().getText());
		return abogado;
	}
	
	public Map<String, String> getStringMapValue() {
		Map<String, String> abogado = new HashMap<String, String>();
		abogado.put(MATRICULA_CORTE, display.getMatriculaCorte().getText());
		return abogado;
	}
	
	public void setValue(CustomBean abogado) {
		display.getMatriculaCorte().setText(abogado.getString(MATRICULA_CORTE));
	}
	
	public String getLabelFormText() {
		return "Matricula otorgada por CSJN";
	}
	
//	public String getFooterFormText() {
//		return "Ingrese el DNI";
//	}
	
	public void setEnabled(boolean b) {
		display.setMatriculaCorteEnabled(b);
	}
	
	public List<String> getKeys() {
		List<String> lista = new ArrayList<String>();
		lista.add(MATRICULA_CORTE);
		return lista;
	}
	
	@Override
	public List<String> getColumnNames() {
		List<String> lista = new ArrayList<String>();
		lista.add("Matricula CSJN");
		return lista;
	}
	
	public String getBusinessToShow() {
		return display.getMatriculaCorte().getText();
	}
	
	public String getDocumentoValidanteString() {
		return "Matricula CSJN";
	}
	
}
