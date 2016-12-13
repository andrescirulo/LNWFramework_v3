package net.latin.server.utils.pageGeneraror.core;

import java.util.List;

import net.latin.server.utils.pageGeneraror.core.server.ServerGeneratorElement;

public interface PageGeneratorTemplate {

	public List<PageGeneratorElement> getPageGenerators();

	public ServerGeneratorElement getServerGenerator();

}
