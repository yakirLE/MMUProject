package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MMUView extends Observable implements View 
{
	public static final int BYTES_IN_PAGE = 5;
	public static final int NUM_MMU_PAGES = 1;
	private static final String EMPTY_STRING = "";
	private int currentCommandToPlayIndex;
	private int pageFaultCounter;
	private int pageReplacementCounter;
	private JFrame frame;
	private List<String> processsesCurrentlySelected;
	private List<String> commands;
	private RamPanel table;
	private CountersPanel counters;
	private ButtonsPanel buttons;
	private ListPanel list;
	
	public MMUView()
	{
		super();
		this.currentCommandToPlayIndex = 0;
		this.pageFaultCounter = 0;
		this.pageReplacementCounter = 0;
		this.processsesCurrentlySelected = new ArrayList<>();
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
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
	}
	
	public void setConfiguration(List<String> commands)
	{
		final int RAM_CAPACITY_INDEX = 0;
		final int PROCESSES_NUMBER_INDEX = 1;
		
		this.commands = commands.subList(2, commands.size() - 1);
		table = new RamPanel(Integer.parseInt(commands.get(RAM_CAPACITY_INDEX)), BYTES_IN_PAGE);
        table.setOpaque(true);
        counters = new CountersPanel();
        counters.setOpaque(true);
        buttons = new ButtonsPanel(this);
        buttons.setOpaque(true);
        list = new ListPanel(this, getProcesses(Integer.parseInt(commands.get(PROCESSES_NUMBER_INDEX))));
        list.setOpaque(true);
        createPanelWithConstraints(table, GridBagConstraints.FIRST_LINE_START, 0, 0, 0, 0, 0, 0, new Insets(20, 0, 0, 0), GridBagConstraints.HORIZONTAL);
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
	
	private String[] getProcesses(int processesNumber)
	{
		String[] names;
		
		names = new String[processesNumber];
		for(int i = 0; i < processesNumber; i++)
			names[i] = "Process" + i;
		
		return names;
	}
	
	public void playButton_Clicked()
	{
		try 
		{
			if(currentCommandToPlayIndex < this.commands.size())
				processCommand();
			else
				JOptionPane.showMessageDialog(null, "Done");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playAllButton_Clicked()
	{			
	}
	
	public void processesList_Clicked(List<String> processesPicked)
	{
	}
	
	private void processCommand() throws IOException
	{
		String currentProcess;
		String currentPage;
		String currentData;
		String currentCommand;
		String[] splittedCommand;
		
		currentCommand = this.commands.get(this.currentCommandToPlayIndex++);
		if(currentCommand.startsWith("PF"))
			this.counters.getPageFaultTextField().setText(Integer.toString(++this.pageFaultCounter));
		else if(currentCommand.startsWith("PR"))
			this.counters.getPageReplacementTextField().setText(Integer.toString(++this.pageReplacementCounter));
		else if(currentCommand.startsWith("GP"))
		{
			System.out.println(currentCommand);
			currentCommand = currentCommand.replace("GP: ", EMPTY_STRING);
			splittedCommand = currentCommand.split(" ");
			currentProcess = splittedCommand[0];
			currentPage = splittedCommand[1];
			currentData = currentCommand.replaceAll(".*\\[", EMPTY_STRING);
			System.out.println(getProcessID(currentProcess));
			this.table.getRamTable().getTableHeader().getColumnModel().getColumn(4).setHeaderValue(currentPage);
			this.table.getRamTable().getTableHeader().repaint();
			setDataForPage(getData(currentData));
		}
		else
			throw new IOException();
	}
	
	private int getProcessID(String str)
	{
		return Integer.parseInt(str.replaceAll("[a-zA-Z]", EMPTY_STRING));
	}
	
	private List<String> getData(String str)
	{
		String newString = str;
		String[] data;
		
		newString = newString.replace("\\[", EMPTY_STRING);
		newString = newString.replace("]", EMPTY_STRING);
		newString = newString.replaceAll(",", EMPTY_STRING);
		data = newString.split(" ");
		
		return Arrays.asList(data);
	}
	
	private void setDataForPage(List<String> data)
	{
		for(int i = 0; i < BYTES_IN_PAGE; i++)
			this.table.getRamTable().getModel().setValueAt(data.get(i), i, 4);
		this.table.centerDataInCells();
	}
}
