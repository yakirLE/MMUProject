package com.hit.memoryunits;

import com.hit.algorithm.IAlgoCache;

public class MemoryManagementUnit 
{
	IAlgoCache<Long, Long> algo;
	RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		ram = new RAM(ramCapacity);
		this.algo = algo;
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds) throws java.io.IOException
	{
		
	}
}
