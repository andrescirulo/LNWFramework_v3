package net.latin.server.utils.reports.tableDocument;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {

	private String[] columnNames;
	private Object[][] data;
	
	public CustomTableModel(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.fillData( data );		
	}
	
	private void fillData(Object[][] data) {
		if (data.length == 0 ||  data[0].length == 0) { return; }
		this.data = new String[data[0].length][data.length];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				this.data[j][i] = data[i][j];
			}
		}
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.columnNames[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return this.data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}

}
