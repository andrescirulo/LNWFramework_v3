package net.latin.client.widget.table;

import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.MaterialDataTable;

public class GwtMaterialTablePager<T> extends MaterialDataPager<T> {

	public GwtMaterialTablePager(MaterialDataTable<T> table, DataSource<T> dataSource) {
        super(table,dataSource);
        setPageText("Página");
        setRowPerPageText("Filas por Página");
    }
	
	public GwtMaterialTablePager() {
		super();
	}
	
	public void setPageText(String text){
		pageLabel.setText(text);
	}
	
	public void setRowPerPageText(String text){
		rowsPerPageLabel.setText(text);
	}
	
	@Override
	protected void updateUi() {
		super.updateUi();
		String txt=actionLabel.getText();
		txt=txt.replace(" of ", " de ");
		actionLabel.setText(txt);
	}
	
	
}
