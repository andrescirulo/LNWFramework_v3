package net.latin.client.widget.base.helpPage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.dialog.GwtDialogBox;

/**
 * Pagina default para visualizacion de contentidos de ayuda
 *
 */
public class GwtDefaultHelpPage extends GwtDialogBox implements GwtHelpPage {

	private static final String TITLE = "Documentación para esta sección";
	private static final int DIALOG_WIDTH = 550;
	private static final int DIALOG_HEIGHT = 450;

	private HTML docPanel;

	public GwtDefaultHelpPage() {
		setTitle(TITLE);
		setSize(DIALOG_WIDTH + "px", DIALOG_HEIGHT + "px");
//		setPopupPosition(getCenterX(DIALOG_WIDTH), getCenterY(DIALOG_HEIGHT) - DIALOG_HEIGHT / 2);

		//panel con scrollbar para mostrar el html de la documentacion
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "500px");
		docPanel = new HTML();
		scrollPanel.add(docPanel);
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setSize("100%", "500px");
		flowPanel.add(scrollPanel);
		this.add( flowPanel );


		//boton cerrar, abajo a la derecha
		GwtButton cerrar = new GwtButton( "Cerrar" );
		cerrar.addClickHandler( new ClickHandler() {
			public void onClick(ClickEvent event) {
				cerrar_onClick();
			}
		});
//		GwtHorizontalButtonPanel buttonPanel = new GwtHorizontalButtonPanel();
//		buttonPanel.addButton( cerrar );
//		buttonPanel.render();
//		this.add( buttonPanel );

		this.render();
	}

	protected void cerrar_onClick() {
		this.close();
	}

	public void showCaseDocumentation(String useCaseName, String pageName, String documentation) {
		docPanel.setHTML(documentation);
		this.show();
	}

}
