package net.latin.client.widget.menu;

import java.util.ArrayList;
import java.util.List;

import gwt.material.design.client.constants.SideNavType;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialSideNav;
import net.latin.client.rpc.commonUseCase.InitialInfo;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.menu.data.LeafMenuItem;
import net.latin.client.widget.menu.data.MenuItem;

public class GwtMenuBar extends MaterialSideNav {

	private List<GwtMenuElement> menues;
	private GwtController controller;
	private List<GwtMenuBarListener> listeners;
	
	public GwtMenuBar(GwtController controller) {
		this.listeners=new ArrayList<GwtMenuBarListener>();
		this.menues=new ArrayList<GwtMenuElement>();
		this.controller = controller;
		this.setType(SideNavType.PUSH);
		this.setTop(64);
		this.setId("sidenav");
		this.setAlwaysShowActivator(true);
		this.setAllowBodyScroll(true); 
	}

	public void resetWidget() {
		for(GwtMenuElement menu:menues){
			this.remove(menu.getWidget());
		}
	}

	public void hideAll() {
		for(GwtMenuElement menu:menues){
			menu.hide();
		}
	}

	public void unselectAllMenuItems() {
		
	}

	public void setMenuBarVisible(boolean visible) {
		if (visible){
			for (GwtMenuBarListener listener:listeners){
				listener.onMenuVisible();
			}
		}
		else{
			for (GwtMenuBarListener listener:listeners){
				listener.onMenuHide();
			}
		}
	}

	public void addListener(GwtMenuBarListener listener) {
		listeners.add(listener);
	}

	public void build(InitialInfo initialInfo) {
		if (initialInfo.getMenu()!=null){
			buildMenuEntries(initialInfo.getMenu().getChilds());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void buildMenuEntries(List childs) {
		MaterialCollapsible entriesContainer=new MaterialCollapsible();
		MenuItem menu;
		for (int i = 0; i < childs.size(); i++) {
			menu = (MenuItem) childs.get( i );
			if( menu.isLeaf() ) {
				LeafMenuItem leaf = (LeafMenuItem)menu;
//				menues.add( buildMenuItem(null,leaf,"paper-item-menu") );
				menues.add( buildMenuItem(null,leaf,"mock-style") );
			}
			else {
				GwtMenu gwtMenu = new GwtMenu( menu.getName(),this);
				for (int j = 0; j < menu.getChilds().size(); j++) {
					LeafMenuItem leaf = (LeafMenuItem)menu.getChilds().get( j );
//					gwtMenu.addMenuItem( buildMenuItem(gwtMenu, leaf ,"paper-item-submenu") );
					gwtMenu.addMenuItem( buildMenuItem(gwtMenu, leaf ,"mock-style") );
				}
				menues.add( gwtMenu );
			}
		}
		
		for (GwtMenuElement gwtMenu:menues){
			entriesContainer.add(gwtMenu.getWidget());
//			this.add(gwtMenu.getWidget());
		}
		this.add(entriesContainer);
	}
	
	/**
	 * Creates the correct GwtMenuItem
	 */
	private GwtMenuItem buildMenuItem(GwtMenu menu, LeafMenuItem leaf ,String style) {
		//it's an external link
		if( leaf.isExternal() ) {
			GwtExternalMenuItem item = new GwtExternalMenuItem(this,menu,style);
			item.setTitle( leaf.getName() );
			item.setUrl( leaf.getUrl() );
			return item;

		//it's an internal link
		} else {
			GwtInternalMenuItem item = new GwtInternalMenuItem(this,menu, leaf.getName(), leaf.getUrl() ,style);
			return item;
		}
	}
	
	/**
	 * Show an internal GWT page
	 */
	public void showInternalPage(String pageGroup) {
		this.controller.tryShowPageGroup( pageGroup );
	}

	/**
	 * Goes to an external url
	 */
	public void showExternalPage(String target, String url) {
		this.controller.showExternalLink( url );
	}

	public void showOrHide() {
		setMenuBarVisible(!this.isVisible());
	}
}
