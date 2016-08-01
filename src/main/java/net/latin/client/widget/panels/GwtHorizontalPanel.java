package net.latin.client.widget.panels;

import java.util.Iterator;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import net.latin.client.widget.base.LnwWidget;

public class GwtHorizontalPanel extends FlowPanel implements LnwWidget {
	
	private FlowPanel mainPanel=new FlowPanel();
	private Label titlePanel;
	
	public GwtHorizontalPanel() {
		mainPanel=new FlowPanel();
		this.getElement().getStyle().setPosition(Position.RELATIVE);
		this.getElement().getStyle().setPadding(5, Unit.PX);
		super.add(mainPanel);
	}
	public GwtHorizontalPanel(String titulo,Boolean borde) {
		this();
		setTitleText(titulo);
		showBorder(borde);
	}
	
	public void resetWidget() {
		WidgetCollection widgets = this.getChildren();
		for (Iterator<Widget> iterator = widgets.iterator(); iterator.hasNext();) {
			Widget widget = iterator.next();
			// Intento realizar el casteo.
			try {
				((LnwWidget) widget).resetWidget();
			} catch (ClassCastException e) {
				// No debo realizar nada.
			}
		}
	}
	
	@Override
	public void add(Widget w) {
		w.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		mainPanel.add(w);
	}
	
	/**
	 * Coloca un titulo en la parte superior del panel
	 * @param titulo
	 */
	public void setTitleText(String titulo){
		titlePanel=new Label(titulo);
		titlePanel.setStyleName("gwt-panel-titulo");
		this.getElement().getStyle().setPaddingTop(15, Unit.PX);
		this.getElement().getStyle().setMarginTop(15, Unit.PX);
		super.add(titlePanel);
	}

	public void setFocus() {
		//FIXME delegar foco!
	}
	
	public HandlerRegistration addClickHandler(ClickHandler listener) {
		return this.addDomHandler(listener, ClickEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler){
		return this.addDomHandler(handler, MouseOverEvent.getType());
	}
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler){
		return this.addDomHandler(handler, MouseOutEvent.getType());
	}

	public void showBorder(boolean mostrar) {
		
		if (mostrar){
			this.getElement().getStyle().setBorderColor("#DDDDDD");
			this.getElement().getStyle().setBorderWidth(1, Unit.PX);
			this.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		}
		else{
			this.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		}
	}
}
