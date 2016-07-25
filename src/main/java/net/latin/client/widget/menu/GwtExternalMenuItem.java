package net.latin.client.widget.menu;

public class GwtExternalMenuItem extends GwtMenuItem {

	private String url;
	private String target;


	public void showPage() {
		this.getMenu().showExternalPage( this.target, this.url );
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}






}
