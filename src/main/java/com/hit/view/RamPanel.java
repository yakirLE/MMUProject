package com.hit.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RamPanel extends JPanel implements ActionListener
{
	private int ramCapacity;
	private int dataSize;
	private JTable table;
	
	public RamPanel(int ramCapacity, int dataSize) 
	{
		super(new GridLayout(1,0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		
		this.ramCapacity = ramCapacity;
		this.dataSize = dataSize;
		table = new JTable(new RamTableModel(this.dataSize, this.ramCapacity));
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Integer.class, centerRenderer);
		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}

}
