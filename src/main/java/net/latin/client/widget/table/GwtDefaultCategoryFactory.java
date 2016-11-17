package net.latin.client.widget.table;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;

public class GwtDefaultCategoryFactory extends CategoryComponentFactory {
	@Override
	public CategoryComponent generate(String categoryName) {
		CategoryComponent category = super.generate(categoryName);

        if(!(category instanceof OrphanCategoryComponent)) {
            category = new GwtDefaultCategoryComponent(categoryName);
        }
        return category;
	}
}
