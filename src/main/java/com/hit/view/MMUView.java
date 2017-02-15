package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
//        createPanelWithConstraints(table, GridBagConstraints.FIRST_LINE_START, 0, 0, 0, 0, 0, 0, new Insets(20, 0, 50, 0), GridBagConstraints.HORIZONTAL);
//        createPanelWithConstraints(buttons, GridBagConstraints.CENTER, 2, 12, 0, 0, 0, 0, new Insets(40, 0, 40, 600), GridBagConstraints.NONE);
//        createPanelWithConstraints(list, GridBagConstraints.LINE_START, 1, 6, 0.5, 0, 0, 0, new Insets(150, 40, 20, 0), GridBagConstraints.NONE);
//        createPanelWithConstraints(counters, GridBagConstraints.LINE_END, 4, 6, 0, 0, 1, 0, new Insets(50, 10, 0, 10), GridBagConstraints.NONE);
        createPanelWithConstraints(table, GridBagConstraints.FIRST_LINE_START, 0, 0, 0, 0, 0, 0, new Insets(0, 0, 0, 0), GridBagConstraints.HORIZONTAL);
        createPanelWithConstraints(list, GridBagConstraints.LINE_START, 0, 0, 0, 0, 0, 0, new Insets(130, 20, 20, 0), GridBagConstraints.NONE);
        createPanelWithConstraints(buttons, GridBagConstraints.CENTER, 0, 0, 0, 0, 0, 0, new Insets(0, 200, 0, 300), GridBagConstraints.NONE);
        createPanelWithConstraints(counters, GridBagConstraints.LINE_END, 0, 0, 0, 0, 0, 0, new Insets(0, 0, 0, 20), GridBagConstraints.NONE);
        
        frame.pack();
        frame.setVisible(true);
	}
	
	private void createPanelWithConstraints(JPanel panel, int anchor, int gridx, int gridy, double weightx, double weighty, 
			int gridwidth, int gridheight, Insets insets, int fill)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.anchor = anchor;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.insets = insets;
        constraints.fill = fill;
		this.frame.getContentPane().add(panel, constraints);
	}
}
