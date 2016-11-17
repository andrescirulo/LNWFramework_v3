package net.latin.client.widget.table;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.ui.table.TableSubHeader;

public class GwtDefaultCategoryComponent extends CategoryComponent {
	public GwtDefaultCategoryComponent(String category) {
		super(category);
	}

	@Override
	protected void render(TableSubHeader subheader) {
		super.render(subheader);

		subheader.setOpenIcon(IconType.FOLDER_OPEN);
		subheader.setCloseIcon(IconType.FOLDER);
	}
}