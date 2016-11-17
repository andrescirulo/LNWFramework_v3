package net.latin.client.widget.confirm;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.dialog.GwtDialogBox;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.separator.GwtSpace;

public class GwtConfirm extends GwtDialogBox implements ClickHandler {

	private static final int SPACING_BUTTONS = 3;
	private static final String URL_DEFAULT = "imagenes/icons/alert.gif";

	private GwtConfirmListener listener;
	protected GwtButton buttonSi;
	protected GwtButton buttonNo;
	private Object dataObj;
	private String question;
	private HTML labelPregunta;
	private Label imagen;
	private String url = URL_DEFAULT;
	private int space = 10;
	private int topPadding = 1;
	private int bottomPadding = 1;
	public static final int ORDENBOTONES_SI_NO = 0;
	public static final int ORDENBOTONES_NO_SI = 1;

	public GwtConfirm(GwtConfirmListener listener, String title, String question) {
		initVisualComponents(listener, title, question, ORDENBOTONES_SI_NO);
	}

	public GwtConfirm(GwtConfirmListener listener, String title, String question, int ordenBotones) {
		initVisualComponents(listener, title, question, ordenBotones);
	}

	private void initVisualComponents(GwtConfirmListener listener, String title, String question, int ordenBotones) {
		this.listener = listener;

		//titulo del dialog
		this.setTitle(title);

		HorizontalPanel hpPrincipal = new HorizontalPanel();
		hpPrincipal.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hpPrincipal.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

		//pregunta
		this.question = question;
		labelPregunta = new HTML( question );

		imagen = new Label();
		imagen.setWidth("100px");
		imagen.setHeight("80px");
		 imagen.getElement().getStyle().setProperty( "background", "center center no-repeat "+ "url("+ url +")");

		hpPrincipal.add(imagen);
		hpPrincipal.add(new GwtHorizontalSpace(this.space));
		hpPrincipal.add(labelPregunta);
		hpPrincipal.add(new GwtHorizontalSpace(this.space));

		this.add(new GwtSpace(this.topPadding));
		this.add(hpPrincipal);

		//panel para botones
		HorizontalPanel panelButtons = new HorizontalPanel();
		panelButtons.setHorizontalAlignment( HorizontalPanel.ALIGN_CENTER );
		panelButtons.setSpacing( SPACING_BUTTONS );
		panelButtons.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		this.add( panelButtons );
		this.add(new GwtSpace(this.bottomPadding));

		//boton No
		buttonNo = new GwtButton( "No",this );

		//boton Si
		buttonSi = new GwtButton( "Si",this );

		//Orden de los botones Si / No
		switch (ordenBotones) {
		case ORDENBOTONES_SI_NO:
			panelButtons.add( buttonSi );
			panelButtons.add( buttonNo );
			break;
		case ORDENBOTONES_NO_SI:
			panelButtons.add( buttonNo );
			panelButtons.add( buttonSi );
			break;

		default:
			break;
		}

		//auto-render
		this.render();
	}

	public void onClick(ClickEvent event) {
		Object button=event.getSource();
		this.close();

		if((button.equals(buttonSi)))
			this.listener.accionSi(this.dataObj);
		else
			this.listener.accionNo(this.dataObj);
	}

	public void setData(Object dataObj) {
		this.dataObj = dataObj;
	}

	public Object getData(){
		return this.dataObj;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
		this.labelPregunta.setHTML(question);
	}

	public void setTextStyleAttribute(String attribute, String value) {
		if(labelPregunta != null)
			labelPregunta.getElement().getStyle().setProperty( attribute, value);
	}

	public String getUrlImage() {
		return url;
	}

	public void setUrlImage(String url) {
		this.url = url;
		 imagen.getElement().getStyle().setProperty( "background", "center center no-repeat "+ "url("+ url +")");
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public int getTopPadding() {
		return topPadding;
	}

	public void setTopPadding(int topPadding) {
		this.topPadding = topPadding;
	}

	public int getBottomPadding() {
		return bottomPadding;
	}

	public void setBottomPadding(int bottomPadding) {
		this.bottomPadding = bottomPadding;
	}

}
