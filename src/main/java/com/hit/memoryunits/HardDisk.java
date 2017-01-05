package com.hit.memoryunits;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HardDisk 
{
	private static HardDisk instance = new HardDisk();
	
	private HardDisk()
	{
		
	}
	
	public static HardDisk getInstance()
	{
		return instance;
	}
	
	public Page<byte[]> pageFault(Long pageId) throws FileNotFoundException, IOException
	{
		
	}
	
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException
	{
	
	}
}
