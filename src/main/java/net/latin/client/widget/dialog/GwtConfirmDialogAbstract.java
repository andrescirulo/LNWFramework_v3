package net.latin.client.widget.dialog;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.separator.GwtHorizontalSpace;
import net.latin.client.widget.separator.GwtSpace;

public abstract class GwtConfirmDialogAbstract extends GwtDialogBox implements ClickHandler {

	private static final int SPACING_BUTTONS = 3;
	private static final int SPACING_BETWEEN_BUTTONS = 20;

	protected GwtConfirmAbstractListener listener;
	private Object dataObj;
	private String question;
	private HTML labelPregunta;
	private MaterialIcon imagen;
	private int space = 10;
	private List<GwtButton> buttons;

	public GwtConfirmDialogAbstract(GwtConfirmAbstractListener listener, String title, String question) {
		initVisualComponents(listener, title, question);
	}

	public GwtConfirmDialogAbstract(GwtConfirmAbstractListener listener, String title) {
		initVisualComponents(listener, title, "");
	}

	private void initVisualComponents(GwtConfirmAbstractListener listener, String title, String question) {
		this.listener = listener;

		// titulo del dialog
		this.setTitle(title);

		HorizontalPanel hpPrincipal = new HorizontalPanel();
		hpPrincipal.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hpPrincipal.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

		// pregunta
		this.question = question;
		labelPregunta = new HTML(question);
		labelPregunta.addStyleName("popUpQuestion");
		imagen = new MaterialIcon();
		imagen.setIconType(IconType.WARNING);
		imagen.setIconSize(IconSize.LARGE);
		imagen.setIconColor(Color.YELLOW_DARKEN_2);

		hpPrincipal.add(new GwtHorizontalSpace(this.space));
		hpPrincipal.add(imagen);
		hpPrincipal.add(new GwtHorizontalSpace(this.space));
		hpPrincipal.add(labelPregunta);
		hpPrincipal.add(new GwtHorizontalSpace(this.space));
		hpPrincipal.setCellVerticalAlignment(labelPregunta, HasVerticalAlignment.ALIGN_MIDDLE);
		this.add(hpPrincipal);
		hpPrincipal.add(new GwtHorizontalSpace(this.space));

		// panel para botones
		HorizontalPanel panelButtons = new HorizontalPanel();
		panelButtons.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		panelButtons.setSpacing(SPACING_BUTTONS);
		this.add(panelButtons);

		buttons = new ArrayList<GwtButton>();
		buildButtons(buttons);
		GwtSpace space;
		for (GwtButton button : buttons) {
			button.addClickHandler(this);
			panelButtons.add(button);
			if (buttons.size() > 1 && !buttons.get(buttons.size() - 1).equals(button)) {
				space = new GwtSpace();
				space.setWidth(SPACING_BETWEEN_BUTTONS + "px");
				panelButtons.add(space);
			}
			button.getElement().getParentElement().getStyle().setTextAlign(TextAlign.CENTER);
		}
		
		// auto-render
		this.render();
	}

	protected abstract void buildButtons(List<GwtButton> buttons);

	public void onClick(ClickEvent event) {
		Object button=event.getSource();
		this.close();
		if (button instanceof Button) {
			this.listener.accion(((Button) button).getText(), this.dataObj);
		}
	}

	public void showCentered() {
		showCentered(1);
	}

	private void showCentered(int index) {
		final int indice = index - 1;
		if (indice >= buttons.size() || indice < 0) {
			throw new RuntimeException("No existe el indice " + (index) + " el maximo es " + buttons.size());
		}

		close();
		setVisible(true);
		show();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				GwtWidgetUtils.setFocus((Focusable)buttons.get(indice));
			}
		});
	}

	public void setData(Object dataObj) {
		this.dataObj = dataObj;
	}

	public Object getData() {
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
		if (labelPregunta != null)
			labelPregunta.getElement().getStyle().setProperty( attribute, value);
	}


	/**
	 * Setea el estilo de la imagen del dialog
	 * @param style
	 */
	public void setImageStyle(String style){
		imagen.addStyleName(style);
	}
	
	public void setImageIcon(IconType icon){
		imagen.setIconType(icon);
	}
	
	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}


	public void setFocusBotonSi() {
		GwtWidgetUtils.setFocus((Focusable)this.buttons.get(0));
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				GwtWidgetUtils.setFocus((Focusable)buttons.get(0));
			}
		});

	}
	public void setFocusButton(String button) {
		for(final GwtButton b:buttons) {
			if(button.equalsIgnoreCase(b.getText())){
				GwtWidgetUtils.setFocus((Focusable)b);
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						GwtWidgetUtils.setFocus((Focusable)b);
					}
				});
			}
		}
	}

}
