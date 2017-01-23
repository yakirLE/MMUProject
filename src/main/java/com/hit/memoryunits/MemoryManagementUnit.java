package com.hit.memoryunits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hit.algorithm.IAlgoCache;

public class MemoryManagementUnit 
{
	private IAlgoCache<Long, Long> algo;
	private RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		ram = new RAM(ramCapacity);
		this.algo = algo;
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
		//System.out.println("Pages to get: " + Arrays.toString(pageIds));
		//System.out.println("RAM before change = " + this.ram.toString());
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
				//System.out.println("PF: " + key);
				newPage = HardDisk.getInstance().pageFault(key);
				this.ram.addPage(newPage);
				requestedPages.add(newPage);
			}
			else
			{
				pageToHd = this.ram.getPage(keysOfRemoveablePages.get(firstElement));
				keysOfRemoveablePages.remove(firstElement);
				//System.out.println("PR: toHD=" + pageToHd.getPageId() + ", toRAM=" + key);
				newPage = HardDisk.getInstance().pageReplacement(pageToHd, key);
				this.ram.removePage(pageToHd);
				this.ram.addPage(newPage);
				requestedPages.add(newPage);
			}
		}
		
		//System.out.println("RAM after change = " + this.ram.toString() + "\r\n");
		
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
