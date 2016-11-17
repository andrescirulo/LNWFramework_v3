package net.latin.client.widget.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.ListItem;

public class GwtMenu extends MaterialCollapsibleItem implements GwtMenuElement {

	private MaterialCollapsibleBody menuItemsContainer;
	private MaterialLink menuTitle;
	private List<ListItem> menuItems;
	private GwtMenuBar menuBar;
	private GwtMaterialIconMorph iconMorph;

	public GwtMenu(String name,GwtMenuBar menuBar) {
		this.menuBar = menuBar;
		this.setWidth("100%");
		menuItemsContainer = new MaterialCollapsibleBody();
		menuItemsContainer.setWidth("100%");
		menuItemsContainer.getElement().getStyle().setProperty("paddingLeft", "20px");
		
		MaterialCollapsibleHeader menuTitleContainer = new MaterialCollapsibleHeader();
		menuTitle = new MaterialLink(name);
		menuTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				iconMorph.morph();
			}
		});
		
		MaterialIcon iconMore = new MaterialIcon(IconType.EXPAND_MORE);
		MaterialIcon iconLess = new MaterialIcon(IconType.EXPAND_LESS);
		iconMorph = new GwtMaterialIconMorph();
		iconMorph.setIconSize(IconSize.SMALL);
		iconMorph.removeClickMorph();
		iconMorph.add(iconLess);
		iconMorph.add(iconMore);
		iconMorph.setDisplay(Display.INLINE_BLOCK);
		iconMorph.getElement().getStyle().setProperty("lineHeight", "3.5rem");
		menuTitleContainer.add(menuTitle);
		menuTitle.add(iconMorph);
		
		this.collapse();
		
		menuItems=new ArrayList<ListItem>();
		
		this.add(menuTitleContainer);
		this.add(menuItemsContainer);
	}
	
	protected void setIcon(boolean opened) {
		if (opened){
			menuTitle.setIconType(IconType.EXPAND_LESS);
		}
		else{
			menuTitle.setIconType(IconType.EXPAND_MORE);
		}
	}

	public void addMenuItem(GwtMenuItem buildMenuItem) {
		menuItems.add(buildMenuItem);
		menuItemsContainer.add(buildMenuItem);
	}

	public void hide() {
		collapse();
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
