package com.hit.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class ListPanel extends JPanel implements ActionListener 
{
	private JList<String> list;
	private JLabel processesLabel;
	private String[] processes = {"process1", "process2", "process3", "process4", "process5"};

	public ListPanel() 
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		processesLabel = new JLabel("Processes");
		processesLabel.setAlignmentX(LEFT_ALIGNMENT);
		list = new JList<>(processes);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setPreferredSize(new Dimension(90, 400));
		list.setMinimumSize(new Dimension(80, 80));
		list.setMaximumSize(new Dimension(100, 300));
		list.setAlignmentX(LEFT_ALIGNMENT);
		add(processesLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(list);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}
	
}
