package net.latin.client.widget.menu.data;

import java.util.List;

import net.latin.client.widget.base.GwtBusinessObject;

public abstract class MenuItem extends GwtBusinessObject {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract List<MenuItem> getChilds();

	public boolean isLeaf() {
		return false;
	}

}
