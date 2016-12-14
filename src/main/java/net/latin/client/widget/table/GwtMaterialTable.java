package net.latin.client.widget.table;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;

import gwt.material.design.client.data.DataSource;
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
public class GwtMaterialTable<T> extends MaterialDataTable<T> implements Handler {

	private GwtTableCategoryFactory<T> categoryFactory;
	private GwtMaterialTableManager<T> manager;
	private Boolean inicializada;
	private GwtMaterialTablePager<T> pager;
	public GwtMaterialTable(GwtMaterialTableManager<T> manager) {
		this.manager = manager;
		this.inicializada=false;
		setCategoryFactory(new GwtDefaultCategoryFactory());
		addAttachHandler(this);
	}
	public GwtMaterialTable(GwtMaterialTableManager<T> manager,GwtTableCategoryFactory<T> catFactory) {
		this(manager);
		this.categoryFactory = catFactory;
		setRowFactory(new RowComponentFactory<T>(){
			public String getCategory(T obj) {
				return categoryFactory.getCategory(obj);
			}
		});
	}
	
	@Override
	@Deprecated
	/**
	 * Usar setUseCategories(GwtTableCategoryFactory catFactory)
	 * */
	public void setUseCategories(boolean useCategories) {
		super.setUseCategories(useCategories);
	}
	public void setUseCategories(GwtTableCategoryFactory<T> catFactory) {
		super.setUseCategories(true);
	}

	@Override
	public void onAttachOrDetach(AttachEvent event) {
		if (event.isAttached() && !inicializada){
			manager.addColumns(this);
			if (pager!=null){
				this.add(pager);
			}
			inicializada=true;
		}
	}

	public void setTableTitle(String titulo) {
		this.getTableTitle().setText(titulo);
	}
	public void setPager(DataSource<T> dataSource, int rows) {
		pager = new GwtMaterialTablePager<T>(this, dataSource);
		pager.setLimitOptions(5, 10, 15, 20);
		pager.setLimit(rows);
	}
	public GwtMaterialTablePager<T> getPager(){
		return pager;
	}
}
