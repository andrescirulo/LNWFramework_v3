package net.latin.client.widget.menu;

import com.vaadin.polymer.paper.widget.PaperItem;

public abstract class GwtMenuItem extends PaperItem{

	private String title;
	private GwtMenu menu;

	public abstract void showPage();


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	protected GwtMenu getMenu() {
		return menu;
	}


	protected void setMenu(GwtMenu menu) {
		this.menu = menu;
	}

}
