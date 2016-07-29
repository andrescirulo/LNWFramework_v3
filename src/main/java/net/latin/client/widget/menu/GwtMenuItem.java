package net.latin.client.widget.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperRipple;

public abstract class GwtMenuItem extends PaperItem implements GwtMenuElement{

	private String title;
	protected GwtMenuBar menuBar;
	protected GwtMenu menu;
	
	public GwtMenuItem(GwtMenuBar menuBar,GwtMenu menu,String style) {
		this.menuBar = menuBar;
		this.menu = menu;
		this.addStyleName(style);
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showPage();
			}
		});
	}
	
	public abstract void showPage();


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.getElement().setInnerHTML(this.title);
		add(new PaperRipple());
	}


	protected GwtMenu getMenu() {
		return menu;
	}


	protected void setMenu(GwtMenu menu) {
		this.menu = menu;
	}

	public void hide() {
	}

	public Widget getWidget() {
		return this;
	}
}
