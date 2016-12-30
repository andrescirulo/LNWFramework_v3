package net.latin.client.widget.collapsible;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;

public class GwtCollapsiblePanel extends MaterialCollapsible {
	
	public void addItem(MaterialCollapsibleHeader header,MaterialCollapsibleBody body){
		MaterialCollapsibleItem item=new MaterialCollapsibleItem();
		item.add(header);
		item.add(body);
		this.add(item);
	}
	public void addItem(String headerText,Widget widget){
		MaterialCollapsibleHeader header=new MaterialCollapsibleHeader();
		header.add(new MaterialLabel(headerText));
		
		MaterialCollapsibleBody body=new MaterialCollapsibleBody();
		body.add(widget);
		
		addItem(header, body);
	}
}
