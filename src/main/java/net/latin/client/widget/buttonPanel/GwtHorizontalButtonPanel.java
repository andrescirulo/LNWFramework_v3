package net.latin.client.widget.buttonPanel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;

import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.separator.GwtHorizontalSpace;

/**
 * Panel Horizontal para agregar botones a izquierda y a derecha.
 *
 */
public class GwtHorizontalButtonPanel extends GwtVisualComponent {
	protected static final String NO_WIDTH = "0%"; //Equivale al ancho exacto del widget que tiene dentro (en IE)
	protected static final int SPACING_BETWEEN_ELEMENTS = 10;
	protected List<GwtButton> leftButtons;
	protected List<GwtButton> rightButtons;

	/**
	 * CSS, tiene el mismo que el formulario
	 */
	private static final String BUTTONS_CSS = "GwtFormButtonsPanel";
	private HorizontalPanel auxPanel;

	/**
	 * Crea un nuevo GwtHorizontalButtonPanel
	 */
	public GwtHorizontalButtonPanel() {
		this.leftButtons = new ArrayList<GwtButton>();
		this.rightButtons = new ArrayList<GwtButton>();
		this.addStyleName(BUTTONS_CSS);
	}

	/**
	 * Agrega el button a la derecha
	 * @param button
	 */
	public void addButton( GwtButton button ) {
		rightButtons.add( button );
	}

	/**
	 * Agrega el button a la izquierda
	 * @param button
	 */
	public void addLeftButton( GwtButton button ) {
		leftButtons.add( button );
	}

	/**
	 * Genera los botones a izquierda o a derecha segun
	 * como se hayan agregado
	 */
	public GwtVisualComponent render(){
		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment( HorizontalPanel.ALIGN_CENTER );
		panel.setWidth( "100%" );
		auxPanel = new HorizontalPanel();
		auxPanel.setWidth( "90%" );

		HorizontalPanel leftPanel = new HorizontalPanel();
		leftPanel.setHorizontalAlignment( HorizontalPanel.ALIGN_LEFT );
		leftPanel.setWidth( "100%" );
		HorizontalPanel auxLeftPanel = new HorizontalPanel();
		auxLeftPanel.setWidth( NO_WIDTH );
		for (int i = 0; i < leftButtons.size(); i++) {
			auxLeftPanel.add( (GwtButton)leftButtons.get(i) );

			if ( i + 1 != leftButtons.size() )
				auxLeftPanel.add( new GwtHorizontalSpace( SPACING_BETWEEN_ELEMENTS ) );
		}

		HorizontalPanel rightPanel = new HorizontalPanel();
		rightPanel.setHorizontalAlignment( HorizontalPanel.ALIGN_RIGHT );
		rightPanel.setWidth( "100%" );
		HorizontalPanel auxRightPanel = new HorizontalPanel();
		auxRightPanel.setWidth( NO_WIDTH );
		for (int i = 0; i < rightButtons.size(); i++) {
			auxRightPanel.add( (GwtButton)rightButtons.get(i) );

			if ( i + 1 != rightButtons.size() )
				auxRightPanel.add( new GwtHorizontalSpace( SPACING_BETWEEN_ELEMENTS ) );
		}

		rightPanel.add( auxRightPanel );
		leftPanel.add( auxLeftPanel );

		auxPanel.add( leftPanel );
		auxPanel.add( rightPanel );

		panel.add( auxPanel );
		this.add( panel );
		return super.render();
	}

	public void resetWidget() {
		//No debo realizar ninguna accion en particular
	}

	public void setFocus() {
		if(rightButtons.size() > 0) {
			GwtButton b = (GwtButton)rightButtons.get(rightButtons.size()-1);
			b.setFocus(true);
			//b.setText("focused");
		}

	}

	public void setAuxPanelWidth(String width){
		auxPanel.setWidth(width);
	}
}
