package net.latin.client.widget.tabs;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import net.latin.client.widget.msg.GwtMensajesHandler;
import net.latin.client.widget.msg.GwtMsgNotification;

public class GwtMaterialTabs extends MaterialRow implements GwtMensajesHandler{

	private List<MaterialTabItem> tabs;
	private MaterialTab tabContainer;
	private int nextId=1;
	private Integer tabSize=null;
	private GwtMensajesHandler msgHandler;
	private GwtMsgNotification msg;
	
	public GwtMaterialTabs() {
		this(false,null);
	}
	/**
	 * Especifica si el tab se comporta como el componente principal
	 * reduciendo los márgenes para estar en la pagina completa
	 * @param mainContainer
	 */
	public GwtMaterialTabs(Boolean mainContainer) {
		this(mainContainer,null);
	}
	
	/**
	 * Agrega un handler para los mensajes si no se quiere que los coloque la tab dentro suyo
	 * @param msgHandler
	 */
	public GwtMaterialTabs(GwtMensajesHandler msgHandler) {
		this(false,msgHandler);
	}
	
	
	/**
	 * Especifica si el tab se comporta como el componente principal
	 * reduciendo los márgenes para estar en la pagina completa
	 * Agrega un handler para los mensajes si no se quiere que los coloque la tab dentro suyo
	 * @param mainContainer
	 * @param msgHandler
	 */
	public GwtMaterialTabs(Boolean mainContainer,GwtMensajesHandler msgHandler) {
		tabs=new ArrayList<MaterialTabItem>();
		tabContainer = new MaterialTab();
		
		MaterialColumn tabsColumn = new MaterialColumn();
		tabsColumn.setGrid("s12");
		tabsColumn.addStyleName("tabs-col");
		tabsColumn.add(tabContainer);
		this.add(tabsColumn);
		this.addStyleName("material-tab");
		this.msgHandler = msgHandler;
		if (mainContainer){
			this.addStyleName("material-tab-main");
		}
		if (msgHandler==null){
			msg = new GwtMsgNotification();
			msg.addStyleName("col");
			msg.addStyleName("s12");
			this.add(msg);
			msg.render();
		}
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
		if (tabContent instanceof GwtMaterialTab ){
			GwtMaterialTab tc=(GwtMaterialTab) tabContent;
			if (msgHandler!=null){
				tc.setMsgHandler(msgHandler);
			}
			else{
				tc.setMsgHandler(this);
			}
		}
		
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
		tabItem.setWaves(WavesType.DEFAULT);
		return tabItem;
	}
	
	public void addAllErrorMessages(List<String> messagesList) {
		msg.addAllErrorMessages(messagesList);
	}
	
	public void clearMessages() {
		msg.clear();
	}
	
	public void addErrorMessage(String text) {
		msg.addErrorMessage(text);
	}
	
	public void addAlertMessage(String text) {
		msg.addAlertMessage(text);
	}
	
	public void addOkMessage(String text) {
		msg.addOkMessage(text);
	}
	
	public void addLoadingMessage(String text) {
		msg.addLoadingMessage(text);
	}
}
