package net.latin.client.widget.label;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import net.latin.client.widget.base.GwtVisualComponent;

/**
 * Panel vertical que muestra una lista de labels, uno debajo del otro
 *
 */
public class GwtLabelPanel extends GwtVisualComponent {

	private static final String MAIN_CSS = "GwtLabelPanelMainPanel";
	private static final String DEFAULT_LABEL_CSS = "GwtLabelPanelLabel";

	private final List<Label> labels;
	private String verticalSpacing;

	public GwtLabelPanel( int verticalSpacing ) {
		labels = new ArrayList<Label>();
		this.verticalSpacing = verticalSpacing > 0 ? verticalSpacing + "px" : null;

		setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		setStyleName(MAIN_CSS);
	}

	public GwtLabelPanel() {
		this(0);
	}

	/**
	 * Agrega un label con estilo customizado por el usuario
	 * @param label
	 * @param labelStyle
	 */
	public Label addLabel( Label label, String labelStyle ) {
		label.setStyleName( labelStyle );
		label.setHorizontalAlignment( HorizontalPanel.ALIGN_CENTER );
		labels.add(label);
		return label;
	}

	/**
	 * Agrega un label con el estilo default.
	 * Si el label tiene otro estilo, el mismo sera reemplazado por el estilo
	 * default. Utilizar <code>public void addLabel( Label label, String labelStyle )</code> para
	 * evitar esto.
	 * @param label
	 */
	public Label addLabel( Label label ) {
		return this.addLabel(label, DEFAULT_LABEL_CSS);
	}

	/**
	 * Crea un label con el texto especificado y con un estilo customizado por el usuario
	 * @param label
	 * @param labelStyle
	 */
	public Label addLabel( String text, String labelStyle ) {
		return this.addLabel( new Label(text), DEFAULT_LABEL_CSS);
	}

	/**
	 * Crea un label con el texto especificado y con un estilo default
	 * @param text
	 */
	public Label addLabel( String text ) {
		return this.addLabel( new Label(text) );
	}

	/**
	 * Agrega muchos strings de label
	 * @param labels
	 * @param labelStyle
	 */
	public void addLabels( List<String> labels ) {
		for (int i = 0; i < labels.size(); i++) {
			addLabel(labels.get(i));
		}
	}

	public GwtVisualComponent render() {
		Label label;
		for (int i = 0; i < labels.size(); i++) {
			//agregar spacing
			if( verticalSpacing != null && i != 0 ) {
				label = new Label();
				label.setHeight( verticalSpacing );
				this.add( label );
			}

			//agregar label
			label = (Label) labels.get(i);
			this.add( label );
		}

		return super.render();
	}



}
