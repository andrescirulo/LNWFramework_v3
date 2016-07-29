package net.latin.client.widget.menu;

public class GwtInternalMenuItem extends GwtMenuItem {

	private String pageName;

	public GwtInternalMenuItem(GwtMenuBar menuBar,GwtMenu menu, String title, String pageName ,String style) {
		super(menuBar,menu,style);
		this.setTitle(title);
		this.setPageName(pageName);
	}

	public void showPage() {
		this.menuBar.showInternalPage( this.pageName );
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}
