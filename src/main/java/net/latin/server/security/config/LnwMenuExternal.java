package net.latin.server.security.config;

import net.latin.client.widget.menu.data.ExternalLeaf;
import net.latin.client.widget.menu.data.LeafMenuItem;

import org.jdom2.Element;

public class LnwMenuExternal implements LnwMenuItem{

	private String id;
	private String title;
	private String url;

	public LnwMenuExternal(Element element) {
		buildFromElement(element);
	}

	private void buildFromElement(Element element) {
		//id
		id = element.getAttributeValue( LnwGeneralConfig.ID_ATTR );

		//title
		title = element.getAttributeValue( LnwGeneralConfig.TITLE_ATTR );

		//url
		url = element.getAttributeValue( LnwGeneralConfig.URL_ATTR );
	}


	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public LeafMenuItem buildGwtMenu() {
		LeafMenuItem widget = new ExternalLeaf();
		widget.setUrl(this.id);
		widget.setName(this.title);
		return widget;
	}

	@Override
	public String getId() {
		return this.id;
	}




}
