package com.hit.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MFUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.memoryunits.HardDisk;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model 
{
	public static final String CONFIG_FILE_NAME = "Configuration.json";
	public int numProcesses;
	public int ramCapacity;
	private List<String> commands;
	private MemoryManagementUnit mmu;
	
	public MMUModel(String[] configuration)
	{
		createMMU(configuration);
	}
	
	private void createMMU(String[] configuration)
	{
		IAlgoCache<Long, Long> algo;
		
		algo = createAndGetAlgo(configuration);
		this.mmu = new MemoryManagementUnit(ramCapacity, algo);
	}
	
	private void runProcesses(List<Process> processes)
	{
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for(Process process : processes)
			executorService.execute(process);
		
		executorService.shutdown();
		waitForThreadsToFinish(executorService);
	}
	
	private void waitForThreadsToFinish(ExecutorService executorService)
	{
		try 
		{
			executorService.awaitTermination(10, TimeUnit.MINUTES);
		}
		catch (InterruptedException e) 
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
	}
	
	private List<Process> createProcesses(List<ProcessCycles> processCycles)
	{
		List<Process> processes = new ArrayList<>();
		ProcessCycles currentProcessCycles;
		Process process;
		
		for(int i = 0; i < processCycles.size(); i++)
		{
			currentProcessCycles = processCycles.get(i);
			process = new Process(i, this.mmu, currentProcessCycles);
			processes.add(process);
		}
		
		return processes;
	}
	
	private RunConfiguration readConfigurationFile() 
			throws JsonIOException, JsonSyntaxException, FileNotFoundException
	{	
		return new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE_NAME)), RunConfiguration.class);
	}
	
	private IAlgoCache<Long, Long> createAndGetAlgo(String[] tokens)
	{
		String algoToken = tokens[0];
		IAlgoCache<Long, Long> algo = null;

		this.ramCapacity = Integer.parseInt(tokens[tokens.length - 1]);
		if(algoToken.equals("LRU"))
			algo = new LRUAlgoCacheImpl<>(this.ramCapacity);
		else if(algoToken.equals("MFU"))
			algo = new MFUAlgoCacheImpl<>(this.ramCapacity);
		else if(algoToken.equals("Second"))
			algo = new SecondChanceAlgoCacheImpl<>(this.ramCapacity);
		
		return algo;
	}
	
	public List<String> getCommands()
	{
		return this.commands;
	}
	
	@Override
	public void readData()
	{
		String line;
		FileReader fileReader;
		BufferedReader bufferedReader = null;
		
		commands = new ArrayList<>();
		try 
		{
			fileReader = new FileReader(MMULogger.DEFAULT_FILE_NAME);
			bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
				if(!line.isEmpty())
					commands.add(line);
			}
		}
		catch (Exception e)
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		finally
		{
			try
			{
				if(bufferedReader != null)
					bufferedReader.close();
			}
			catch(IOException e)
			{
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
		}
		
		System.out.println(commands.toString());
	}
	
	@Override
	public void start()
	{
		final String EMPTY_STRING = "";
		RunConfiguration runConfig;
		List<ProcessCycles> processCycles;
		List<Process> processes;
		
		try
		{
			runConfig = readConfigurationFile();
			processCycles = runConfig.getProcessesCycles();
			processes = createProcesses(processCycles);
			this.numProcesses = processes.size();
			MMULogger.getInstance().write("PN: " + this.numProcesses, Level.INFO);
			MMULogger.getInstance().write(EMPTY_STRING, Level.INFO);
			runProcesses(processes);
			HardDisk.getInstance().recreateHdFile();
		}
		catch(Exception e)
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
	}
}
