package net.latin.client.widget.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vaadin.polymer.iron.widget.IronCollapse;
import com.vaadin.polymer.paper.widget.PaperItem;

public class GwtMenu extends VerticalPanel {

	private IronCollapse menuItemsContainer;
	private PaperItem menuTitle;
	private List<PaperItem> menuItems;
	private GwtMenuBar menuBar;

	public GwtMenu(String name,GwtMenuBar menuBar) {
		this.menuBar = menuBar;
		menuTitle = new PaperItem(name);
		menuTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				menuItemsContainer.setOpened(!menuItemsContainer.getOpened());
			}
		});

		menuItemsContainer = new IronCollapse();
		menuItemsContainer.setOpened(false);
		
		menuItems=new ArrayList<PaperItem>();
		
		this.add(menuTitle);
		this.add(menuItemsContainer);
	}
	
	public void addMenuItem(GwtMenuItem buildMenuItem) {
		menuItems.add(buildMenuItem);
	}

	public void hide() {
		menuItemsContainer.setOpened(false);
	}

	protected void showExternalPage( String target, String url ) {
		this.menuBar.showExternalPage( target, url );
	}

	protected void showInternalPage(String pageName ) {
		this.menuBar.showInternalPage( pageName );
	}

}
