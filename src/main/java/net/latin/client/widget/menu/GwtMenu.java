package net.latin.client.widget.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.iron.widget.IronCollapse;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperRipple;

public class GwtMenu extends VerticalPanel implements GwtMenuElement {

	private IronCollapse menuItemsContainer;
	private PaperItem menuTitle;
	private List<PaperItem> menuItems;
	private GwtMenuBar menuBar;
	private IronIcon ironIcon;

	public GwtMenu(String name,GwtMenuBar menuBar) {
		this.menuBar = menuBar;
		this.setWidth("100%");
		menuTitle = new PaperItem();
		menuTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				menuItemsContainer.setOpened(!menuItemsContainer.getOpened());
				setIcon(menuItemsContainer.getOpened());
				menuTitle.setActive(menuItemsContainer.getOpened());
			}
		});
		menuTitle.add(new Label(name));
		ironIcon = new IronIcon();
		ironIcon.setIcon("icons:expand-more");
		menuTitle.add(ironIcon);
		menuTitle.add(new PaperRipple());
//		menuTitle.setWidth("100%");

		menuItemsContainer = new IronCollapse();
		menuItemsContainer.setWidth("100%");
		menuItemsContainer.setOpened(false);
		menuItemsContainer.getPolymerElement().getStyle().setProperty("padding-left", "20px");
		
		menuItems=new ArrayList<PaperItem>();
		
		this.add(menuTitle);
		this.add(menuItemsContainer);
		menuTitle.addStyleName("paper-item-menu");
	}
	
	protected void setIcon(boolean opened) {
		if (opened){
			ironIcon.setIcon("icons:expand-less");
		}
		else{
			ironIcon.setIcon("icons:expand-more");
		}
	}

	public void addMenuItem(GwtMenuItem buildMenuItem) {
		menuItems.add(buildMenuItem);
		menuItemsContainer.add(buildMenuItem);
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

	public Widget getWidget() {
		return this;
	}

}
