package net.latin.client.widget.menu;


public class GwtInternalMenuItem extends GwtMenuItem {

	private String pageName;

	public GwtInternalMenuItem( String title, String pageName ) {
		this.setTitle(title);
		this.setPageName(pageName);
	}

	public void showPage() {
		this.getMenu().showInternalPage( this.pageName );
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}






}
