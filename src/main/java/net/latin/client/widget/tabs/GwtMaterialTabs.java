package net.latin.client.widget.tabs;

import java.util.ArrayList;
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
	private Integer tabSize=null;
	
	public GwtMaterialTabs() {
		MaterialColumn tabsColumn = new MaterialColumn();
		tabs=new ArrayList<MaterialTabItem>();
		tabsColumn.setGrid("s12");
		tabContainer = new MaterialTab();
		tabsColumn.add(tabContainer);
		this.add(tabsColumn);
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
		contentColumn.add(tabContent);
		
		this.add(contentColumn);
		refreshTabSizes();
	}
	
	private void refreshTabSizes() {
		int tabSz=0;
		if (tabSize==null){
			tabSz=(int) Math.floor(12.0/tabs.size());
		}
		else{
			tabSz=tabSize;
		}
		for (MaterialTabItem tab:tabs){
			tab.setGrid("s" + tabSz);
		}
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
		return tabItem;
	}
}
