package com.hit.memoryunits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;
import com.hit.util.MMULogger;

public class MemoryManagementUnit 
{
	private IAlgoCache<Long, Long> algo;
	private RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		ram = new RAM(ramCapacity);
		this.algo = algo;
		MMULogger.getInstance().write("RC: " + ramCapacity, Level.INFO);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Page<byte[]>[] getPages(Long[] pageIds) throws IOException
	{
		final int firstElement = 0;
		List<Long> keysOfRemoveablePages;
		List<Long> keys = Arrays.asList(pageIds);
		List<Page<byte[]>> requestedPages = new ArrayList<>();
		List<Long> existingElementsInAlgo = this.algo.getElement(keys);
		Page<byte[]> newPage;
		Page<byte[]> pageToHd;
		
		keysOfRemoveablePages = this.algo.putElement(keys, keys);
		if(existingElementsInAlgo != null)
		{
			for(Long key : existingElementsInAlgo)
				requestedPages.add(this.ram.getPage(key));
			keys = removeExistingKeys(keys, existingElementsInAlgo);
		}
		
		for(Long key : keys)
		{
			if(this.ram.getInitialCapacity() > this.ram.getCurrentRamSize())
			{
				newPage = HardDisk.getInstance().pageFault(key);
				this.ram.addPage(newPage);
				requestedPages.add(newPage);
			}
			else
			{
				pageToHd = this.ram.getPage(keysOfRemoveablePages.get(firstElement));
				keysOfRemoveablePages.remove(firstElement);
				newPage = HardDisk.getInstance().pageReplacement(pageToHd, key);
				this.ram.removePage(pageToHd);
				this.ram.addPage(newPage);
				requestedPages.add(newPage);
			}
		}
				
		return requestedPages.toArray((Page<byte[]>[]) new Page[requestedPages.size()]);
	}

	private List<Long> removeExistingKeys(List<Long> keys, List<Long> existingElementsInAlgo) 
	{
		List<Long> keysToInsertToAlgo = new ArrayList<>();
		
		for(Long k : keys)
			if(!existingElementsInAlgo.contains(k))
				keysToInsertToAlgo.add(k);
		
		return keysToInsertToAlgo;
	}
}
