package net.latin.client.widget.link;

import com.google.gwt.user.client.ui.HTML;

/**
 * HTML link to another site
 * 
 * @author Matias Leone
 */
public class GwtExternalLink extends HTML {

	private String text;
	private String url;
	private String target;
	
	/**
	 * Creates a HTML external link
	 * 
	 * @param text
	 * @param url
	 */
	public GwtExternalLink(String text, String url) {
		this.text=text;
		this.url=url;
		render();
	}
	
	
	/**
	 * Creates a HTML external link
	 * 
	 * @param text
	 * @param url
	 * @param target
	 */
	public GwtExternalLink(String text, String url, String target) {
		this.text=text;
		this.url=url;
		this.target=target;
		render();
	}

	private void render(){
		if (target==null){
			this.setHTML("<a href= '" + url + "' >" + text + "</a>");
		}
		else{
			this.setHTML("<a href= \"" + url + "\" target=\"" + target + "\">" + text + "</a>");
		}
	}
	
	public native static void openLocation(String url) /*-{
	   	$wnd.location.href = url;
	}-*/;


	public void setText(String text) {
		this.text = text;
		render();
	}

	public void setUrl(String url) {
		this.url = url;
		render();
	}
	
	/**
	 * 
	 * @param target El frame donde se va a mostrar el vínculo
	 */
	public void setTarget(String target) {
		this.target = target;
		render();
	}

}
