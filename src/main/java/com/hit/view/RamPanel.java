package com.hit.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RamPanel extends JPanel
{
	private int ramCapacity;
	private int dataSize;
	private JTable table;
	private DefaultTableCellRenderer centerRenderer;
	
	public RamPanel(int ramCapacity, int dataSize) 
	{
		this.ramCapacity = ramCapacity;
		this.dataSize = dataSize;
		table = new JTable(new RamTableModel(this.dataSize, this.ramCapacity));
		centerDataInCells();
		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table);
	}
	
	private void centerDataInCells()
	{
		this.centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Integer.class, this.centerRenderer);
	}
	
	public void updateHeader(int index, String text)
	{
		this.table.getTableHeader().getColumnModel().getColumn(index).setHeaderValue(text);
		this.table.getTableHeader().repaint();
	}
	
	public void fillColumn(int index, List<String> data)
	{
		for(int i = 0; i < this.dataSize; i++)
			this.table.getModel().setValueAt(data.get(i), i, index);
		this.table.getColumnModel().getColumn(index).setCellRenderer(this.centerRenderer);
	}
}
