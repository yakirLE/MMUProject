package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MMUView extends Observable implements View 
{
	public static final int BYTES_IN_PAGE = 5;
	public static final int NUM_MMU_PAGES = 1;
	private JFrame frame;
	
	public MMUView()
	{
		super();
	}
	
	@Override
	public void open()
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				createAndShowGUI();
			}
		});
		
		setChanged();
		notifyObservers();
	}
	
	private void createAndShowGUI() 
	{
		frame = new JFrame("Memory Management Unit Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1000, 700);
//        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
	}
	
	public void setConfiguration(List<String> commands)
	{
		final int RAM_CAPACITY_INDEX = 0;
		final int PROCESSES_NUMBER_INDEX = 1;
		GridBagConstraints constraints = new GridBagConstraints();
		RamPanel table;
		CountersPanel counters;
		ButtonsPanel buttons;
		ListPanel list;
		
		table = new RamPanel(Integer.parseInt(commands.get(RAM_CAPACITY_INDEX)), BYTES_IN_PAGE);
        table.setOpaque(true);
        counters = new CountersPanel();
        counters.setOpaque(true);
        buttons = new ButtonsPanel();
        buttons.setOpaque(true);
        list = new ListPanel();
        list.setOpaque(true);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.8;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.getContentPane().add(table, constraints);
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 0.04;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(50, 10, 0, 10);
        frame.getContentPane().add(counters, constraints);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridwidth = 0;
        constraints.insets = new Insets(40, 0, 40, 600);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.getContentPane().add(buttons, constraints);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 2;
        constraints.gridy = 6;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 8;
        constraints.insets = new Insets(20, 40, 0, 0);
//        constraints.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(list, constraints);
        frame.pack();
        frame.setVisible(true);
	}
}
