package net.latin.server.security.config;

import org.jdom2.Element;

import net.latin.client.widget.menu.data.LeafMenuItem;

public class LnwMenuUseCase implements LnwMenuItem{

	private String id;
	private String title;

	public LnwMenuUseCase(Element element) {
		buildFromElement( element );
	}

	private void buildFromElement(Element element) {
		//id
		id = element.getAttributeValue( LnwGeneralConfig.ID_ATTR );

		//title
		title = element.getAttributeValue( LnwGeneralConfig.TITLE_ATTR );
	}
	@Override
	public String getId() {
		return id;
	}



	public String getTitle() {
		return title;
	}

	@Override
	public LeafMenuItem buildGwtMenu() {
		LeafMenuItem widget = new LeafMenuItem();
		widget.setName(this.title);
		widget.setUrl(this.id);
		return widget;
	}



}
