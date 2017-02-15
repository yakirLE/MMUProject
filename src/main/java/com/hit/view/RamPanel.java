package com.hit.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RamPanel extends JPanel
{
	private int ramCapacity;
	private int dataSize;
	private JTable table;
	
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

	public JTable getRamTable()
	{
		return this.table;
	}
	
	public void centerDataInCells()
	{
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Integer.class, centerRenderer);
	}
}
