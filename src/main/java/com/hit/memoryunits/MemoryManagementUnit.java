package com.hit.memoryunits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds) throws IOException
	{
		Long keyOfRemoveablePage;
		Page<byte[]> newPage, pageToHd;
		List<Page<byte[]>> requestedPages = new ArrayList<>();
		
		for(Long id : pageIds)
		{
			if(this.algo.getElement(id) == null)
			{
				if(this.ram.getInitialCapacity() > this.ram.getCurrentCapacity())
				{
					newPage = HardDisk.getInstance().pageFault(id);
					this.algo.putElement(newPage.getPageId(), newPage.getPageId());
					this.ram.addPage(newPage);
					requestedPages.add(newPage);
				}
				else
				{
					keyOfRemoveablePage = this.algo.putElement(id, id);
					pageToHd = this.ram.getPage(keyOfRemoveablePage);
					newPage = HardDisk.getInstance().pageReplacement(pageToHd, id);
					this.ram.removePage(pageToHd);
					this.ram.addPage(newPage);
					requestedPages.add(newPage);
				}
			}
			else
				requestedPages.add(this.ram.getPage(id));
		}
		
		return (Page<byte[]>[]) requestedPages.toArray();
	}
}
