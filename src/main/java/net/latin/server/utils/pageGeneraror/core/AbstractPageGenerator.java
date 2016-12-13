package net.latin.server.utils.pageGeneraror.core;

import org.jdom2.Element;

public abstract class AbstractPageGenerator implements PageGeneratorElement {


//	@Override
//	public DraggablePanel getDraggablePanel() {
//		return null;
//	}

	@Override
	public void loadFromXml(Element element) {
	}

	@Override
	public Element saveToXml() {
		return new Element("generator");
	}

	@Override
	public void setProperties() {
	}

}
