package net.latin.client.rpc.commonUseCase;

import net.latin.client.widget.base.GwtBusinessObject;

public class LnwTableDocumentData extends GwtBusinessObject {
	private String title;
	private String[] columns;
	private String[][] rowsData;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String[][] getRowsData() {
		return rowsData;
	}
	public void setRowsData(String[][] rowsData) {
		this.rowsData = rowsData;
	}

	
}
