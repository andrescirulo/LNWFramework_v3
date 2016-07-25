package net.latin.client.widget.base;

import java.util.Iterator;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * Panel basico a partir del cual crear nuevos panels compuestos.
 * Es recomendable que cada widget implemente el metodo <code>resetWidget()</code>
 *
 * @author Matias Leone
 *
 */
public class GwtVisualComponent extends VerticalPanel implements LnwWidget {

	public GwtVisualComponent() {
		this.setVisible(false);
		this.setWidth("100%");
		this.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		this.setSpacing(0);
	}

	private GwtVisualComponent renderComponent() {
		this.setVisible(true);
		return this;
	}

	/**
	 * Render the visual component and let it ready to be displayed
	 *
	 * @return visual component
	 */
	public GwtVisualComponent render() {
		this.renderComponent();
		return this;
	}

	public void resetWidget() {
		WidgetCollection widgets = this.getChildren();
		for (Iterator iterator = widgets.iterator(); iterator.hasNext();) {
			Widget widget = (Widget) iterator.next();

			//Reseteo el widget si es un LnwWidget
			if(widget instanceof LnwWidget) {
				((LnwWidget) widget).resetWidget();
			}
		}
	}

	public void setFocus() {
		//FIXME delegar foco!
	}

}
