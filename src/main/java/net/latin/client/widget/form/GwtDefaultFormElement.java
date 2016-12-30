package net.latin.client.widget.form;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.LnwWidget;

/**
 * Elemento default del GwtForm que tiene la forma [texto, widget]
 * 
 * Ejemplo: ["usuario", TextBox ] ["pais", ComboBox ]
 * 
 * @author Matias Leone
 */
public class GwtDefaultFormElement implements GwtFormElement {

	public static final String TEXT_CSS = "form-element-label-text";
	public static final String REQUIRED_TEXT_CSS = "form-required-element-label";

	/**
	 * ID del Elemento Es el texto de la descripcion Debe ser único
	 */
	protected String id;

	/**
	 * Label con la descripcion
	 */
	protected MaterialLabel descriptionLabel;

	/**
	 * Label que indica que el campo es requerido
	 */
	protected MaterialLabel requiredLabel;

	/**
	 * Widget del elemento
	 */
	protected Widget widget;

	/**
	 * VerticalPanel que envuelve al widget
	 */
	protected VerticalPanel vpWidget;

	/**
	 * HorizontalPanel que envuelve a todo el elemento
	 */
	protected HorizontalPanel elementPanel;

	/**
	 * Indica si se usa el requiredLabel
	 */
	protected boolean required = false;
	
	/**
	 * Indica si tiene footer
	 */
	private boolean footer = false;
	
	/**
	 * Label que contiene el texto del footer.
	 */
	protected GwtFooterLabel footerLabel;
	
	/**
	 * Texto del footerLabel.
	 */
	private String footerText;
	

	public void setElementId(String id) {
		this.id = id;
	}

	public String getElementId() {
		return id;
	}
	
	public void setLabelText(String text) {
		descriptionLabel.setText(text);
	}

	public HorizontalPanel getElementPanel() {
		return elementPanel;
	}

	
	public GwtFooterLabel getFooterLabel() {
		return footerLabel;
	}


	public void setFooterText(String footerString) {
		this.footer = true;
		this.footerText = footerString;
		if (this.footerLabel!=null){
			this.footerLabel.setText(this.footerText);
		}
	}

	
	public void buildElement(GwtForm form) {
		// int horSeparation =
		// GwtController.defaultI18n.GwtForm_horizontal_separation;

		// creamos label de required
		int requiredWidth = 0;
		if (required) {
			requiredWidth = GwtController.defaultI18n.GwtForm_requiredField_width_separation;
			requiredLabel = new MaterialLabel();
			requiredLabel.setStyleName(REQUIRED_TEXT_CSS);
			requiredLabel.setTextColor(Color.BLUE_ACCENT_2);
			requiredLabel.setText(GwtController.defaultI18n.GwtForm_requiredField_string);
			requiredLabel.setWidth(GwtController.defaultI18n.GwtForm_requiredField_width + "px");
		}

		
		// creamos un label con el texto
		descriptionLabel = new MaterialLabel(id);
		descriptionLabel.setStyleName(TEXT_CSS);
		descriptionLabel.setTextColor(Color.BLUE_ACCENT_2);
//		descriptionLabel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		// descriptionLabel.setWidth(descriptionLabel.getOffsetWidth() +
		// horSeparation+ "px");
		descriptionLabel.setHeight("auto");
		descriptionLabel.setWidth(GwtController.defaultI18n.GwtForm_max_label_width - requiredWidth + "px");
		
		
		// creamos el panel que engloba al widget
		vpWidget = new VerticalPanel();
		vpWidget.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		vpWidget.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		// agregamos el widget
		vpWidget.add(widget);
		
		//agregamos el footer
		if (footer){
			footerLabel = new GwtFooterLabel(footerText);
			vpWidget.add(footerLabel);
		}

		// creamos el panel para englobar todo
		elementPanel = new HorizontalPanel();
		elementPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		elementPanel.setSpacing(GwtController.defaultI18n.GwtForm_title_element_spacing_vertical);

		// Agregamos el label y el vpWidget
		elementPanel.add(descriptionLabel);
		if (required){
			elementPanel.add(requiredLabel);
			elementPanel.setCellWidth(requiredLabel, "20px");
		}
		elementPanel.add(vpWidget);
		
		elementPanel.setCellWidth(descriptionLabel,GwtController.defaultI18n.GwtForm_max_label_width - requiredWidth + "px");
	}

	/**
	 * @return the widget
	 */
	public Widget getWidget() {
		return widget;
	}

	/**
	 * @param widget
	 *            the widget to set
	 */
	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	/**
	 * @return the descriptionLabel
	 */
	public MaterialLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	/**
	 * @return the vpWiget
	 */
	public VerticalPanel getVpWidget() {
		return vpWidget;
	}

	/**
	 * @return the requiredLabel
	 */
	public MaterialLabel getRequiredLabel() {
		return requiredLabel;
	}

	/**
	 * Limpiamos el Widget de este elemento. Muchos elementos agregados no
	 * extienden de LnwWidget, por lo que se intenta averiguar que son.
	 */
	public void resetWidget() {
		if (widget instanceof LnwWidget) {
			((LnwWidget) widget).resetWidget();
		}
	}

	private void resetCellPanel(CellPanel cellPanel) {
		for (int i = 0; i < cellPanel.getWidgetCount(); i++) {
			Widget wid = cellPanel.getWidget(i);
			if (wid instanceof LnwWidget) {
				((LnwWidget) wid).resetWidget();
			}
			if (wid instanceof CellPanel) {
				resetCellPanel((CellPanel) wid);
			}
		}
	}

	/**
	 * Hacemos focus sobre el widget de este elemento. Muchos elementos
	 * agregados no extienden de LnwWidget, por lo que se intenta averiguar que
	 * son.
	 */
	public void setFocus() {
		if (widget instanceof LnwWidget) {
			((LnwWidget) widget).setFocus();
		}
	}

	public boolean isVisible() {
		return elementPanel.isVisible();
	}

	public void setVisible(boolean flag) {
		elementPanel.setVisible(flag);
	}

	/**
	 * Indica que el widget debe marcarse como un campo requerido.
	 * 
	 * @param flag
	 */
	public void setRequired(boolean flag) {
		required = flag;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isFooter() {
		return footer;
	}

	/**
	 * Indica si el widget tendrá o no un footer.
	 * @param footer
	 */
	public void setFooter(boolean footer) {
		this.footer = footer;
	}
	
	

}
