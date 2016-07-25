package net.latin.client.widget.menu.data;

import java.util.ArrayList;
import java.util.List;

public class CompositeMenuItem extends MenuItem {

	private List<MenuItem> childs = new ArrayList<MenuItem>();

	public List<MenuItem> getChilds() {
		return childs;
	}

	public boolean addChild(MenuItem menuItem) {
		return this.childs.add(menuItem);
	}
}
