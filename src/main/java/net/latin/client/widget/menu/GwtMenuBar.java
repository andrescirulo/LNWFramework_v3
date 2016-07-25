package net.latin.client.widget.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;

import net.latin.client.rpc.commonUseCase.InitialInfo;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.menu.data.LeafMenuItem;
import net.latin.client.widget.menu.data.MenuItem;

public class GwtMenuBar extends FlowPanel {

	private List<GwtMenu> menues;
	private GwtController controller;
	private List<GwtMenuBarListener> listeners;
	
	public GwtMenuBar(GwtController controller) {
		this.listeners=new ArrayList<GwtMenuBarListener>();
		this.menues=new ArrayList<GwtMenu>();
		this.controller = controller;
		this.getElement().setAttribute("drawer", "");
		this.getElement().setAttribute("mode", "seamed");
	}

	public void resetWidget() {
		for(GwtMenu menu:menues){
			this.remove(menu);
		}
	}

	public void hideAll() {
		for(GwtMenu menu:menues){
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
		MenuItem menu;
		for (int i = 0; i < childs.size(); i++) {
			menu = (MenuItem) childs.get( i );
			GwtMenu gwtMenu = new GwtMenu( menu.getName(),this);
			if( !menu.isLeaf() ) {
				for (int j = 0; j < menu.getChilds().size(); j++) {
					LeafMenuItem leaf = (LeafMenuItem)menu.getChilds().get( j );
					gwtMenu.addMenuItem( buildMenuItem( leaf ) );
				}
			}
			menues.add( gwtMenu );
		}
	}
	
	/**
	 * Creates the correct GwtMenuItem
	 */
	private GwtMenuItem buildMenuItem( LeafMenuItem leaf  ) {
		//it's an external link
		if( leaf.isExternal() ) {
			GwtExternalMenuItem item = new GwtExternalMenuItem();
			item.setTitle( leaf.getName() );
			item.setUrl( leaf.getUrl() );
			return item;

		//it's an internal link
		} else {
			GwtInternalMenuItem item = new GwtInternalMenuItem( leaf.getName(), leaf.getUrl() );
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
}
