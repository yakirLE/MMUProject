package com.hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.hit.view.CLI;

public class MMUDriver 
{
	public static final String CONFIG_FILE_NAME = "Configuration.json";
	
	public static void main(String[] args) throws InterruptedException, InvocationTargetException 
	{
		int capacity;
		boolean wasStartInitiated = false;
		String[] configuration;
		CLI cli = new CLI(System.in, System.out);
		IAlgoCache<Long, Long> algo;
		MemoryManagementUnit mmu;
		RunConfiguration runConfig;
		List<ProcessCycles> processCycles;
		List<Process> processes;
		
		while((configuration = cli.getConfiguration()) != null)
		{
			algo = null;
			capacity = 0;
			if(configuration[0].equals("start"))
			{
				wasStartInitiated = true;
				cli.write("Please enter required algorithm and RAM capacity\r\n");
			}
			else if(wasStartInitiated)
			{
				cli.write("Processing...\r\n");
				algo = createAndGetAlgo(configuration);
				capacity = Integer.parseInt(configuration[configuration.length - 1]);
				mmu = new MemoryManagementUnit(capacity, algo);
				try
				{
					runConfig = readConfigurationFile();
					processCycles = runConfig.getProcessesCycles();
					processes = createProcesses(processCycles, mmu);
					runProcesses(processes);
					HardDisk.getInstance().recreateHdFile();
					cli.write("Done\r\n");
					wasStartInitiated = false;
				}
				catch(Exception e)
				{
					cli.write(e.getMessage());
				}
			}
		}
		
		cli.write("Thank you\r\n");
	}
	
	public static IAlgoCache<Long, Long> createAndGetAlgo(String[] tokens)
	{
		String algoToken = tokens[0];
		String capacityToken = tokens[tokens.length - 1];
		IAlgoCache<Long, Long> algo = null;
		
		if(algoToken.equals("LRU"))
			algo = new LRUAlgoCacheImpl<>(Integer.parseInt(capacityToken));
		else if(algoToken.equals("MFU"))
			algo = new MFUAlgoCacheImpl<>(Integer.parseInt(capacityToken));
		else if(algoToken.equals("Second"))
			algo = new SecondChanceAlgoCacheImpl<>(Integer.parseInt(capacityToken));
		
		return algo;
	}
	
	public static RunConfiguration readConfigurationFile() 
			throws JsonIOException, JsonSyntaxException, FileNotFoundException
	{	
		return new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE_NAME)), RunConfiguration.class);
	}
	
	public static List<Process> createProcesses(List<ProcessCycles> processCycles, MemoryManagementUnit mmu)
	{
		List<Process> processes = new ArrayList<>();
		ProcessCycles currentProcessCycles;
		Process process;
		
		for(int i = 0; i < processCycles.size(); i++)
		{
			currentProcessCycles = processCycles.get(i);
			process = new Process(i, mmu, currentProcessCycles);
			processes.add(process);
		}
		
		return processes;
	}
	
	public static void runProcesses(List<Process> processes)
	{
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for(Process process : processes)
			executorService.execute(process);
		
		executorService.shutdown();
		while(!executorService.isTerminated())
			performSleep(100);
	}
	
	public static void performSleep(int sleepMs)
	{
		try 
		{
			Thread.sleep(sleepMs);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
