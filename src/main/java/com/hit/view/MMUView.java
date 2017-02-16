package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MMUView extends Observable implements View 
{
	public static final int BYTES_IN_PAGE = 5;
	public static final int NUM_MMU_PAGES = 1;
	private static final String EMPTY_STRING = "";
	private int ramCapacity;
	private int currentCommandToPlayIndex;
	private int ramIndex;
	private int pageFaultCounter;
	private int pageReplacementCounter;
	private boolean isRamFull;
	private Map<String, String> pageReplacementMap;
	private Map<String, Integer> pageLocationInRamMap;
	private Map<String, String> indexOfProcessesInRamMap;
	private List<String> processsesCurrentlySelected;
	private List<String> commands;
	private JFrame frame;
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
		this.ramIndex = 0;
		this.isRamFull = false;
		this.processsesCurrentlySelected = new ArrayList<>();
		this.pageReplacementMap = new HashMap<>();
		this.pageLocationInRamMap = new HashMap<>();
		this.indexOfProcessesInRamMap = new HashMap<>();
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
		int processesAmount = Integer.parseInt(commands.get(PROCESSES_NUMBER_INDEX));
		
		this.ramCapacity = Integer.parseInt(commands.get(RAM_CAPACITY_INDEX));
		this.commands = commands.subList(2, commands.size() - 1);
		table = new RamPanel(this.ramCapacity, BYTES_IN_PAGE);
        table.setOpaque(true);
        counters = new CountersPanel();
        counters.setOpaque(true);
        buttons = new ButtonsPanel(this);
        buttons.setOpaque(true);
        list = new ListPanel(this, getProcessesAndInitializeCurrentlySelectedProcesses(processesAmount));
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
	
	private String[] getProcessesAndInitializeCurrentlySelectedProcesses(int processesNumber)
	{
		String[] names;
		
		names = new String[processesNumber];
		for(int i = 0; i < processesNumber; i++)
			names[i] = "Process" + i;
		this.processsesCurrentlySelected = Arrays.asList(names);
		
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
			/// TODO use MMULogger
			e.printStackTrace();
		}
	}
	
	public void playAllButton_Clicked()
	{
		while(currentCommandToPlayIndex < this.commands.size())
			playButton_Clicked();
		JOptionPane.showMessageDialog(null, "Done");
	}
	
	public void processesList_Clicked(List<String> processesPicked)
	{
		this.processsesCurrentlySelected = processesPicked;
	}
	
	private void processCommand() throws IOException
	{
		String currentCommand;
		
		currentCommand = this.commands.get(this.currentCommandToPlayIndex++);
		System.out.println(currentCommand);
		if(currentCommand.startsWith("PF"))
			this.counters.getPageFaultTextField().setText(Integer.toString(++this.pageFaultCounter));
		else if(currentCommand.startsWith("PR"))
			handlePageReplacementCommand(currentCommand);
		else if(currentCommand.startsWith("GP"))
			handleGetPagesCommand(currentCommand);
		else
			throw new IOException();
	}

	private void handlePageReplacementCommand(String currentCommand) 
	{
		String[] pagesToReplace;
		
		this.counters.getPageReplacementTextField().setText(Integer.toString(++this.pageReplacementCounter));
		pagesToReplace = currentCommand.split(" ");
		this.pageReplacementMap.put(pagesToReplace[4], pagesToReplace[2]);
	}

	private void handleGetPagesCommand(String currentCommand) 
	{
		String currentProcess;
		String currentPage;
		String currentData;
		String[] splittedCommand;
		
		currentCommand = currentCommand.replace("GP: ", EMPTY_STRING);
		splittedCommand = currentCommand.split(" ");
		currentProcess = getProcessID(splittedCommand[0]);
		currentPage = splittedCommand[1];
		currentData = getDataAsString(currentCommand);
		if(/*this.processsesCurrentlySelected.contains("Process" + currentProcess) && */!doesPageExistInRam(currentPage))
		{
			checkIfRamIsFull();
			this.pageLocationInRamMap.put(currentPage, ramIndex);
			this.table.getRamTable().getTableHeader().getColumnModel().getColumn(getRamIndex(currentPage)).setHeaderValue(currentPage);
			this.table.getRamTable().getTableHeader().repaint();
			setDataForPage(getDataAsList(currentData));
			increaseRamIndexIfNeeded();
		}
	}

	private void checkIfRamIsFull() 
	{
		if(this.ramIndex == this.ramCapacity)
			this.isRamFull = true;
	}
	
	private void increaseRamIndexIfNeeded()
	{
		if(!this.isRamFull)
			this.ramIndex++;
	}
	
	private int getRamIndex(String currentPage)
	{
		int index;
		
		if(this.isRamFull)
		{
			index = this.pageLocationInRamMap.get(pageReplacementMap.get(currentPage));
			this.pageLocationInRamMap.remove(pageReplacementMap.get(currentPage));
			this.pageLocationInRamMap.put(currentPage, index);
			this.ramIndex = index;
		}
		else
			index = this.ramIndex;
		
		return index;
	}
	
	private boolean doesPageExistInRam(String pageNumber)
	{
		boolean pageExistInRam = false;
		
		for(int i = 0; i < this.ramCapacity; i++)
			if(this.table.getRamTable().getTableHeader().getColumnModel().getColumn(i).getHeaderValue().equals(pageNumber))
				pageExistInRam = true;
		
		return pageExistInRam;
	}
	
	private String getProcessID(String str)
	{
		return str.replaceAll("[a-zA-Z]", EMPTY_STRING);
	}
	
	private String getDataAsString(String str)
	{
		str = str.replaceAll(".*\\[", EMPTY_STRING);
		str = str.replace("]", EMPTY_STRING);
		
		return str;
	}
	
	private List<String> getDataAsList(String str)
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
			this.table.getRamTable().getModel().setValueAt(data.get(i), i, ramIndex);
		this.table.centerDataInCells();
	}
}
