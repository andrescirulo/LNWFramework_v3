package net.latin.client.widget.tabs;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;

public class GwtMaterialTabs extends MaterialRow{

	private List<MaterialTabItem> tabs;
	private MaterialTab tabContainer;
	private int nextId=1;
	
	public GwtMaterialTabs() {
		MaterialColumn tabsColumn = new MaterialColumn();
		tabsColumn.setGrid("12s");
		tabContainer = new MaterialTab();
	}
	
	public void addTab(String text,Widget tabContent){
		addTab(text,tabContent,null);
	}
	public void addTab(String text,Widget tabContent,IconType icon){
		String id=getNextId();
		MaterialTabItem tabItem = crearTabItem(text,icon,id);
		tabs.add(tabItem);
		tabContainer.add(tabItem);
		MaterialColumn contentColumn=new MaterialColumn();
		contentColumn.setId(id);
		contentColumn.setGrid("s12");
		this.add(contentColumn);
	}
	
	private String getNextId() {
		String id="tab_" + nextId;
		nextId++;
		return id;
	}

	private MaterialTabItem crearTabItem(String text,IconType icon,String id){
		MaterialTabItem tabItem=new MaterialTabItem();
		MaterialLink link = new MaterialLink(text);
		link.setHref("#" + id);
		if (icon != null) {
			link.setIconType(icon);
		}
		tabItem.add(link);
		tabItem.setGrid("s1/2");
		return tabItem;
	}
}
