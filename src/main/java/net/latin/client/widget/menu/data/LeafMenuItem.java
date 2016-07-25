package net.latin.client.widget.menu.data;

import java.util.List;

public class LeafMenuItem extends MenuItem {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuItem> getChilds() {
		return null;
	}

	public boolean isLeaf() {
		return true;
	}
	
	public boolean isExternal(){
		return false;
	}
}
