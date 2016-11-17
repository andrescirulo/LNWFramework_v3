package net.latin.client.widget.table;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.ui.table.MaterialDataTable;

/**
 * 
 * Tabla que permite: 
 * Paginar (con MaterialDataPager)
 * ordenar por columna, elegir que columnas mostrar, 
 * seleccionar filas (selectionType)
 * agregar categorias (useCategories)
 * usar sticky headers (useStickyHeader)
 * 
 */
public class GwtMaterialTable<T> extends MaterialDataTable<T> {

	private GwtTableCategoryFactory categoryFactory;
	public GwtMaterialTable() {
		
	}
	
	@Override
	@Deprecated
	/**
	 * Usar setUseCategories(GwtTableCategoryFactory catFactory)
	 * */
	public void setUseCategories(boolean useCategories) {
		super.setUseCategories(useCategories);
	}
	public void setUseCategories(GwtTableCategoryFactory catFactory) {
		super.setUseCategories(true);
		this.categoryFactory = catFactory;
		setRowFactory(new RowComponentFactory<T>(){
			public String getCategory(T model) {
				return categoryFactory.getCategory();
			}
		});
		setCategoryFactory(new GwtDefaultCategoryFactory());
	}
}
