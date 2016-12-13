package net.latin.server.utils.pageGeneraror.core;

import java.io.File;

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;

public class TemplateMakerData {

	private File page;
	private PageGeneratorElement generator;
	private String pageNameInGroup;


	public TemplateMakerData(File page, PageGeneratorElement generator) {
		super();
		this.page = page;
		this.generator = generator;

		this.pageNameInGroup = XslUtils.getGroupPageConstant(page.getAbsolutePath());
	}

	/**
	 * @return the page
	 */
	public File getPage() {
		return page;
	}
	/**
	 * @return the generator
	 */
	public PageGeneratorElement getGenerator() {
		return generator;
	}

	/**
	 * @return the pageNameInGroup
	 */
	public String getPageNameInGroup() {
		return pageNameInGroup;
	}


}
