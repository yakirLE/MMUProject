package com.hit.view;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

public class RamTableModel extends AbstractTableModel
{
	private String[] columnNames;
	private Object[][] data;
	
	public RamTableModel(int rows, int cols) 
	{
		columnNames = new String[cols];
		Arrays.fill(columnNames, " ");
		createAndResetData(rows, cols);
	}
	
	private void createAndResetData(int rows, int cols)
	{
		data = new Object[rows][cols];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				data[i][j] = new Integer(0);
	}
		
	@Override
	public int getRowCount() 
	{
		return data.length;
	}

	@Override
	public int getColumnCount() 
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		return data[rowIndex][columnIndex];
	}
	
	public String getColumnName(int col) 
	{
        return columnNames[col];
    }
	
	public Class getColumnClass(int c) 
	{
        return getValueAt(0, c).getClass();
    }
	
	public void setValueAt(Object value, int row, int col) 
	{
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
