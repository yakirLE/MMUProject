package com.hit.processes;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Runnable 
{
	private int processId;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles)
	{
		this.processId = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
	}
	
	public int getId()
	{
		return this.processId;
	}
	
	public void setId(int id)
	{
		this.processId = id;
	}
	
	@Override
	public void run()
	{
		final String EMPTY_STRING = "";
		String msgToLog;
		List<ProcessCycle> cycles = this.processCycles.getProcessCycles();
		List<Long> pagesId;
		List<byte[]> data;
		Page<byte[]> page;
		Page<byte[]>[] pages;
		
		for(ProcessCycle cycle : cycles)
		{
			pagesId = cycle.getPages();
			data = cycle.getData();
			try
			{
				pages = this.mmu.getPages(pagesId.toArray(new Long[pagesId.size()]));
				for(int i = 0; i < pages.length; i++)
				{
					page = pages[i];
					page.setContent(data.get(i));
					msgToLog = "GP: P" + this.processId + " " + page.getPageId() + " " + Arrays.toString(data.get(i));
					MMULogger.getInstance().write(msgToLog, Level.SEVERE);
				}
				
				MMULogger.getInstance().write(EMPTY_STRING, Level.INFO);
			}
			catch(Exception e)
			{
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
			
			performSleep(cycle.getSleepMs());
		}
	}
	
	private void performSleep(int sleepMs)
	{
		try 
		{
			Thread.sleep(sleepMs);
		}
		catch (InterruptedException e) 
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
	}
}
