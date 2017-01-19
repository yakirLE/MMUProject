package com.hit.memoryunits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hit.algorithm.IAlgoCache;

public class MemoryManagementUnit 
{
	/// TODO: change to private
	public IAlgoCache<Long, Long> algo;
	public RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		ram = new RAM(ramCapacity);
		this.algo = algo;
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds) throws IOException
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
			keys.removeAll(existingElementsInAlgo);
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
}
