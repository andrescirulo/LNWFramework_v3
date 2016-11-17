package net.latin.server.security.config;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import net.latin.server.utils.exceptions.LnwException;

public class LnwMenuGroup {

	private String id;
	private String title;
	private List<LnwMenuItem> menuItems = new ArrayList<LnwMenuItem>();


	public LnwMenuGroup(Element element) {
		buildFromElement( element );
	}

	private void buildFromElement(Element element) {
		//id
		id = element.getAttributeValue( LnwGeneralConfig.ID_ATTR );

		//title
		title = element.getAttributeValue( LnwGeneralConfig.TITLE_ATTR );

		//read use cases
		List<Element> elements = element.getChildren();
		for (Element elemento : elements) {
			if( elemento.getName().matches(LnwGeneralConfig.USE_CASE_TAG)){
				menuItems.add(new LnwMenuUseCase( elemento ) );
			}else if( elemento.getName().matches(LnwGeneralConfig.EXTERNAL_TAG)){
				menuItems.add(new LnwMenuExternal(elemento));
			}

		}


	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<LnwMenuItem> getMenuItems() {
		return menuItems;
	}

	public LnwMenuItem getMenuItem(String item) {
		for (LnwMenuItem menuItem : menuItems) {
			if (menuItem.getId().equals(item)){
				return menuItem;
			}
		}
		throw new LnwException("No se encontro el item " + item);
	}



}
