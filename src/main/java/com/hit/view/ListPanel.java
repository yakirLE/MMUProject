package com.hit.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListPanel extends JPanel implements ListSelectionListener
{
	private JList<String> list;
	private JLabel processesLabel;
	private MMUView view;

	public ListPanel(MMUView view, String[] listObjects) 
	{
		JScrollPane scrollPane;
		
		this.view = view;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createLabel();
		createList(listObjects);
		selectAllItemsInList();
		add(processesLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		scrollPane = createScrollPanel();
		add(scrollPane);
		list.addListSelectionListener(this);
	}

	private void selectAllItemsInList() 
	{
		int start = 0;
		int end = list.getModel().getSize() - 1;
		
		if(end >= 0)
			list.setSelectionInterval(start, end);
	}
	
	private JScrollPane createScrollPanel() 
	{
		JScrollPane scrollPane;
		
		scrollPane = new JScrollPane(list);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		scrollPane.setPreferredSize(new Dimension(90, 290));
		
		return scrollPane;
	}

	private void createList(String[] listObjects) 
	{
		list = new JList<>(listObjects);
		list.setVisibleRowCount(15);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setAlignmentX(LEFT_ALIGNMENT);
	}

	private void createLabel() 
	{
		processesLabel = new JLabel("Processes");
		processesLabel.setAlignmentX(LEFT_ALIGNMENT);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		if(e.getValueIsAdjusting())
			return;
		this.view.processesList_Clicked(this.list.getSelectedValuesList());
	}
	
}
