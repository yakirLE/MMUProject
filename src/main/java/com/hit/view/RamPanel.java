package com.hit.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTable;

public class RamPanel extends JPanel implements ActionListener
{
	private int ramCapacity;
	private int dataSize;
	private JTable table;
	
	public RamPanel(int ramCapacity, int dataSize) 
	{
		super(new GridLayout(1,0));
		this.ramCapacity = ramCapacity;
		this.dataSize = dataSize;
		table = new JTable();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}

}
