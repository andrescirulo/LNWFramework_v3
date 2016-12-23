package net.latin.client.widget.textArea;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import gwt.material.design.client.ui.MaterialTextArea;
import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.base.LnwWidget;

/**
 * Este textArea es un textArea con un label que cuenta la cantidad de
 * caracteres y lineas restantes. Por defecto esta limitado a 240 caracteres, 60
 * caracteres por linea, y 10 lineas, y 30 caracteres por palabra
 * 
 * @author Andres Cirulo
 * 
 */

public class GwtValidateTextArea extends GwtVisualComponent implements LnwWidget {
	private String TEXTAREA_WIDTH = "97%";
	private String PANEL_WIDTH = "400px";
	private int TEXTAREA_VISIBLE_LINES = 4;
	private int TEXTAREA_MAX_WORD_LENGTH = 30;
	private int TEXTAREA_MAX_LENGTH = 240;
	private int TEXTAREA_MAX_CHARS_X_LINES = 61; // 92 visibles + \r
	private int TEXTAREA_MAX_LINES = 10;
	private static final String TOKEN_LINE_SEPARATOR = "\n";
	private static final String CSS_CANTIDAD_EXCEDIDA = "TextAreaCantidadExcedida";
	
	private MaterialTextArea texto = new MaterialTextArea();
	private HTML caracteresDisponibles;
	private VerticalPanel vPanel = new VerticalPanel();
	private boolean required = false;
	private boolean maxwordlenght = false;
	private HorizontalAlignmentConstant horAlign;
	
	/**
	 * 
	 * @param width
	 *            (en pixeles o porcentaje)
	 * @param height
	 *            (en cantidad de lineas visibles)
	 */
	public GwtValidateTextArea(String width, int height) {
		this(width,height,HasHorizontalAlignment.ALIGN_LEFT);
	}
	/**
	 * 
	 * @param width
	 *            (en pixeles o porcentaje)
	 * @param height
	 *            (en cantidad de lineas visibles)
	 */
	public GwtValidateTextArea(String width, int height,HorizontalAlignmentConstant horizontalAlignment) {
		TEXTAREA_VISIBLE_LINES = height;
		TEXTAREA_WIDTH = width;
		horAlign=horizontalAlignment;
		
		this.visualInit();
		// automatic render
		this.add(vPanel);
		this.render();
	}
	
	private void visualInit() {
		texto.addBlurHandler(new TrimFocusHandler());
		texto.setWidth(TEXTAREA_WIDTH);
		setVisibleLines(TEXTAREA_VISIBLE_LINES);
		texto.getElement().getStyle().setProperty( "fontSize", "12px");
		texto.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				updateCaracteresDisponibles();
			}
		});
		texto.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				updateCaracteresDisponibles();
			}
		});
		
		vPanel.setHorizontalAlignment(horAlign);
		vPanel.setWidth(PANEL_WIDTH);
		vPanel.add(texto);
		caracteresDisponibles = new HTML();
		vPanel.add(caracteresDisponibles);
		updateCaracteresDisponibles();
		vPanel.setVisible(true);
	}
	
	public void resetWidget() {
		texto.setText("");
		updateCaracteresDisponibles();
	}
	
	private void updateCaracteresDisponibles() {
		StringBuffer sb = new StringBuffer();
		int cantCarDisp = TEXTAREA_MAX_LENGTH - getLength();
		int catnLinDisp = TEXTAREA_MAX_LINES - getCantidadDeLineas();
		
		// actualizo el string
		sb.append("Caracteres restantes:&nbsp;");
		agregarNumero(sb, cantCarDisp);
		sb.append("&nbsp;&nbsp;Líneas restantes:&nbsp;");
		agregarNumero(sb, catnLinDisp);
		caracteresDisponibles.setHTML(sb.toString());
	}
	
	public int getLength() {
		String text = getText();
		return text.trim().length();
		
	}
	
	public int getCantidadDeLineas() {
		String text = getText();
		String[] split = text.trim().split(TOKEN_LINE_SEPARATOR);
		int contLineas = 0;
		for (int i = 0; i < split.length; i++) {
			contLineas += (split[i].length() / TEXTAREA_MAX_CHARS_X_LINES) + 1;
		}
		return contLineas;
	}
	
	private void agregarNumero(StringBuffer sb, int cantDisp) {
		if (cantDisp < 0) {
			sb.append("<span class='" + CSS_CANTIDAD_EXCEDIDA + "'>");
			sb.append(cantDisp);
			sb.append("&nbsp;(Excedido)");
			sb.append("</span>");
		} else {
			sb.append(cantDisp);
		}
	}
	
	public void setEnabled(boolean val) {
		this.texto.setEnabled(val);
	}
	
	public void setFocus() {
		texto.setFocus(true);
	}
	
	public String getText() {
		return texto.getText().replace("\r", "").replace(TOKEN_LINE_SEPARATOR, "\r" + TOKEN_LINE_SEPARATOR).trim();
	}
	
	public void setText(String text) {
		if (text != null) {
			texto.setText(text.trim());
			updateCaracteresDisponibles();
		}
	}
	
	public void setWidth(String width) {
		TEXTAREA_WIDTH = width;
		if (texto != null) {
			texto.setWidth(TEXTAREA_WIDTH);
		}
	}
	
	
	public void setVisibleLines(int visibleLines) {
		this.TEXTAREA_VISIBLE_LINES = visibleLines;
		texto.setHeight(visibleLines + "em");
	}
	
	public void setMaxLength(int maxLength) {
		TEXTAREA_MAX_LENGTH = maxLength;
	}
	
	public void setMaxCharsXLinea(int max_chars_x_lines) {
		TEXTAREA_MAX_CHARS_X_LINES = max_chars_x_lines;
	}
	
	public void setMaxLines(int max_lines) {
		TEXTAREA_MAX_LINES = max_lines;
	}
	
	public void setPanelWidth(Integer panel_width) {
		PANEL_WIDTH = panel_width + "px";
		vPanel.setWidth(PANEL_WIDTH);
	}
	public void setPanelWidth(String panel_width) {
		PANEL_WIDTH = panel_width;
		vPanel.setWidth(PANEL_WIDTH);
		super.setWidth(PANEL_WIDTH);
	}
	
	private class TrimFocusHandler implements BlurHandler {
		
		public void onBlur(BlurEvent event) {
			Object sender = event.getSource();
			((MaterialTextArea) sender).setText(((TextArea) sender).getText().trim());
		}
		
		
	}
	
	public void setMaxWordLength(int size) {
		maxwordlenght = true;
		TEXTAREA_MAX_WORD_LENGTH = size;
	}
	
	public void trim() {
		this.setText(getText().trim());
	}
	
	public void setTabIndex(int nextIndex) {
		this.texto.setTabIndex(nextIndex);
	}
	
	public void clear(){
		resetWidget();
	}
	
	public void setMostrarFooter(boolean mostrar){
		caracteresDisponibles.setVisible(mostrar);
	}

}
