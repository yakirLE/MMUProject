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
import java.util.Map.Entry;
import java.util.Observable;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hit.util.MMULogger;

public class MMUView extends Observable implements View 
{
	public static final int BYTES_IN_PAGE = 5;
	public static final int NUM_MMU_PAGES = 1;
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";
	private final String PAGE_FAULT_COMMAND = "PF";
	private final String PAGE_REPLACEMENT_COMMAND = "PR";
	private final String GET_PAGES_COMMAND = "GP";
	private final String DONE_MESSAGE = "Done!";
	private final String PREFIX_FOR_PROCESS_IN_LIST = "Process";
	private int ramCapacity;
	private int currentCommandToPlayIndex;
	private int ramIndex;
	private int pageFaultCounter;
	private int pageReplacementCounter;
	private boolean isRamFull;
	private List<String> emptyArray;
	private Map<String, String> pageReplacementMap;
	private Map<String, Integer> pageLocationInRamMap;
	private Map<String, TableProperties> actualRamTableMap;
	private List<String> processsesCurrentlySelected;
	private List<String> commands;
	private JFrame frame;
	private RamPanel table;
	private CountersPanel counters;
	private ButtonsPanel buttons;
	private ListPanel list;
	private CommandsPanel currentCommands;
	
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
		this.actualRamTableMap = new HashMap<>();
		createAndInitializeDataArray();
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
        frame.setVisible(true);
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
        currentCommands = new CommandsPanel();
        currentCommands.setOpaque(true);
        createPanelWithConstraints(table, GridBagConstraints.FIRST_LINE_START, 0, 0, 0, 0, 0, 0, new Insets(20, 0, 0, 0), GridBagConstraints.HORIZONTAL);
        createPanelWithConstraints(list, GridBagConstraints.LINE_START, 0, 0, 0, 0, 0, 0, new Insets(130, 20, 20, 0), GridBagConstraints.NONE);
        createPanelWithConstraints(buttons, GridBagConstraints.CENTER, 0, 0, 0, 0, 0, 0, new Insets(0, 200, 0, 300), GridBagConstraints.NONE);
        createPanelWithConstraints(counters, GridBagConstraints.LINE_END, 0, 0, 0, 0, 0, 0, new Insets(0, 0, 0, 40), GridBagConstraints.NONE);
        createPanelWithConstraints(currentCommands, GridBagConstraints.PAGE_END, 0, 3, 0, 0, 0, 0, new Insets(0, 0, 10, 0), GridBagConstraints.NONE);
        frame.pack();
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
			names[i] = PREFIX_FOR_PROCESS_IN_LIST + i;
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
				JOptionPane.showMessageDialog(null, DONE_MESSAGE);
		} 
		catch (IOException e) 
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
	}
	
	public void playAllButton_Clicked()
	{
		while(currentCommandToPlayIndex < this.commands.size())
			playButton_Clicked();
		JOptionPane.showMessageDialog(null, DONE_MESSAGE);
	}
	
	public void processesList_Clicked(List<String> processesPicked)
	{
		this.processsesCurrentlySelected = processesPicked;
		showDataRelevantToSelectedProcesses();
	}
	
	private void showDataRelevantToSelectedProcesses()
	{
		int index;
		TableProperties currentData;
		
		for(Entry<String, TableProperties> entry : this.actualRamTableMap.entrySet())
		{
			currentData = entry.getValue();
			index = currentData.getIndex();
			if(this.processsesCurrentlySelected.contains(currentData.getProcessName()))
				updateViewedRamTable(index, entry.getKey(), currentData.getData());
			else
				clearValuesInViewedRamTable(index);
		}
	}
	
	private void processCommand() throws IOException
	{
		String currentCommand;
		
		currentCommand = this.commands.get(this.currentCommandToPlayIndex++);
		this.currentCommands.setText(currentCommand);
		if(currentCommand.startsWith(PAGE_FAULT_COMMAND))
			this.counters.getPageFaultTextField().setText(Integer.toString(++this.pageFaultCounter));
		else if(currentCommand.startsWith(PAGE_REPLACEMENT_COMMAND))
			handlePageReplacementCommand(currentCommand);
		else if(currentCommand.startsWith(GET_PAGES_COMMAND))
			handleGetPagesCommand(currentCommand);
		else
			throw new IOException();
	}

	private void handlePageReplacementCommand(String currentCommand) 
	{
		String[] pagesToReplace;
		
		this.counters.getPageReplacementTextField().setText(Integer.toString(++this.pageReplacementCounter));
		pagesToReplace = currentCommand.split(SPACE_STRING);
		this.pageReplacementMap.put(pagesToReplace[4], pagesToReplace[2]);
	}

	private void handleGetPagesCommand(String currentCommand) 
	{
		int index;
		String currentProcess;
		String currentPage;
		String currentData;
		String[] splittedCommand;
		List<String> dataAsList;
		
		currentCommand = currentCommand.replace(GET_PAGES_COMMAND + ": ", EMPTY_STRING);
		splittedCommand = currentCommand.split(SPACE_STRING);
		currentProcess = getProcessID(splittedCommand[0]);
		currentPage = splittedCommand[1];
		currentData = getDataAsString(currentCommand);
		dataAsList = getDataAsList(currentData);
		if(!doesPageExistInRam(currentPage))
		{
			checkIfRamIsFull();
			this.pageLocationInRamMap.put(currentPage, ramIndex);
			index = getRamIndex(currentPage);
			updateActualTable(currentPage, index, currentProcess, dataAsList);
			increaseRamIndexIfNeeded();
			if(this.processsesCurrentlySelected.contains(PREFIX_FOR_PROCESS_IN_LIST + currentProcess))
				updateViewedRamTable(index, currentPage, dataAsList);
		}
		else
			updateDataIfDifferent(currentPage, dataAsList, currentProcess);
	}

	private void updateDataIfDifferent(String currentPage, List<String> currentData, String currentProcess) 
	{
		TableProperties propertiesForPage;
		
		propertiesForPage = this.actualRamTableMap.get(currentPage);
		if(!propertiesForPage.getData().equals(currentData))
		{
			if(this.processsesCurrentlySelected.contains(PREFIX_FOR_PROCESS_IN_LIST + currentProcess))
				this.table.fillColumn(propertiesForPage.getIndex(), currentData);
			this.actualRamTableMap.get(currentPage).setData(currentData);
		}
	}

	private void updateViewedRamTable(int index, String currentPage, List<String> dataAsList) 
	{
		this.table.updateHeader(index, currentPage);
		this.table.fillColumn(index, dataAsList);
	}
	
	private void clearValuesInViewedRamTable(int index) 
	{
		this.table.updateHeader(index, EMPTY_STRING);
		this.table.fillColumn(index, emptyArray);
	}

	private void createAndInitializeDataArray() 
	{
		this.emptyArray = new ArrayList<>();
		for(int i = 0; i < BYTES_IN_PAGE; i++)
			this.emptyArray.add(EMPTY_STRING);
	}

	private TableProperties updateActualTable(String currentPage, int index, String currentProcess, List<String> dataAsList) 
	{
		TableProperties tableProperties = new TableProperties();
		
		tableProperties.setIndex(index);
		tableProperties.setProcessName(PREFIX_FOR_PROCESS_IN_LIST + currentProcess);
		tableProperties.setData(dataAsList);
		this.actualRamTableMap.put(currentPage, tableProperties);
		
		return tableProperties;
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
		String pageNumberToRemove;
		
		if(this.isRamFull)
		{
			pageNumberToRemove = pageReplacementMap.get(currentPage);
			index = this.pageLocationInRamMap.get(pageNumberToRemove);
			this.pageLocationInRamMap.remove(pageNumberToRemove);
			this.actualRamTableMap.remove(pageNumberToRemove);
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
			if(this.actualRamTableMap.containsKey(pageNumber))
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
		str = str.replaceAll(",", EMPTY_STRING);
		
		return str;
	}
	
	private List<String> getDataAsList(String str)
	{
		String newString = str;
		String[] data;
		
		data = newString.split(SPACE_STRING);
		
		return Arrays.asList(data);
	}
}
