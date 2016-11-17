package net.latin.client.widget.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.ListItem;

public abstract class GwtMenuItem extends ListItem implements GwtMenuElement{

	private String title;
	protected GwtMenuBar menuBar;
	protected GwtMenu menu;
	private MaterialLink linkElem;
	
	public GwtMenuItem(GwtMenuBar menuBar,GwtMenu menu,String style) {
		this.menuBar = menuBar;
		this.menu = menu;
		this.addStyleName(style);
		this.setWidth("100%");
		linkElem=new MaterialLink();
		this.add(linkElem);
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showPage();
			}
		});
		setWaves(WavesType.DEFAULT);
	}
	
	public abstract void showPage();


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
//		this.getElement().setInnerHTML(this.title);
		linkElem.setText(this.title);
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
