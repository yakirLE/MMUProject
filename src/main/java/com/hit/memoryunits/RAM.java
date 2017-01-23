package com.hit.memoryunits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RAM 
{
	private int initialCapacity;
	private Map<Long, Page<byte[]>> pages;
	
	public RAM(int initialCapacity)
	{
		this.initialCapacity = initialCapacity;
		this.pages = new HashMap<>();
	}
	
	public int getInitialCapacity()
	{
		return this.initialCapacity;
	}
	
	public void setInitialCapacity(int initialCapacity)
	{
		this.initialCapacity = initialCapacity;
	}
	
	public int getCurrentRamSize()
	{
		return this.pages.size();
	}
	
	public Map<Long, Page<byte[]>> getPages()
	{
		return this.pages;
	}
	
	public void setPages(Map<Long, Page<byte[]>> pages)
	{
		this.pages = pages;
	}
	
	public void addPage(Page<byte[]> addPage)
	{
		if(! this.pages.containsKey(addPage.getPageId()))
			this.pages.put(addPage.getPageId(), addPage);
	}
	
	public void addPages(Page<byte[]>[] addPages)
	{
		for(Page<byte[]> p : addPages)
			this.addPage(p);
	}
	
	public Page<byte[]> getPage(Long pageId)
	{
		return this.pages.get(pageId);
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds)
	{
		List<Page<byte[]>> requestedPages = new ArrayList<>();
		
		for(Long id : pageIds)
			requestedPages.add(this.getPage(id));
		
		return (Page<byte[]>[])requestedPages.toArray();
	}
	
	public void removePage(Page<byte[]> removePage)
	{
		if(this.pages.containsKey(removePage.getPageId()))
			this.pages.remove(removePage.getPageId());
	}
	
	public void removePages(Page<byte[]>[] removePages)
	{
		for(Page<byte[]> p : removePages)
			this.removePage(p);
	}
	
	private String getMapKeys()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Entry<Long, Page<byte[]>> entry : pages.entrySet())
		{
			sb.append(entry.getKey() + " ");
		}
		
		return sb.toString();
	}
	
	public String toString()
	{
		return "Current Size: " + pages.size() + ", Keys: " + getMapKeys();
	}
}
