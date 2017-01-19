package com.hit.processes;

import com.hit.memoryunits.MemoryManagementUnit;

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
		
	}
}
