package com.hit.memoryunits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HardDisk 
{
	private final int _SIZE = 10000;
	private final String DEFAULT_FILE_NAME = "HD.txt";
	private static HardDisk instance = new HardDisk();
	private Map<Long, Page<byte[]>> pages;
	
	private HardDisk()
	{	
		createHdFile();
		initializeHdFile();
	}

	private void initializeHdFile()
	{
		Long key;
		
		pages = new HashMap<>();
		for(Integer i = 0; i < _SIZE; i++)
		{
			key = i.longValue();
			pages.put(key, new Page<byte[]>(key, new byte[]{0}));
		}
		
		try 
		{
			writePagesToHd();
		}
		catch (Exception e)
		{
			System.out.println("Error writing to file. " + e.getMessage());
		}
	}

	private void createHdFile() 
	{
		File pathToHdFile = new File(DEFAULT_FILE_NAME);
		
		try 
		{
			pathToHdFile.createNewFile();
		} 
		catch (IOException e) 
		{
			System.out.println("Error creating file " + DEFAULT_FILE_NAME + ". " + e.getMessage());
		}
	}
	
	private void writePagesToHd() throws FileNotFoundException, IOException
	{
		FileOutputStream fos;
		ObjectOutputStream oos = null;
		
		fos = new FileOutputStream(DEFAULT_FILE_NAME);
		oos = new ObjectOutputStream(fos);
		oos.writeObject(pages);
		oos.flush();
		oos.close();
	}
	
	@SuppressWarnings("unchecked")
	private void readPagesFromHd() throws FileNotFoundException, IOException
	{
		FileInputStream fis;
		ObjectInputStream ois;
		
		fis = new FileInputStream(DEFAULT_FILE_NAME);
		ois = new ObjectInputStream(fis);
		try
		{
			pages = (Map<Long, Page<byte[]>>) ois.readObject();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Error converting to Page type. " + e.getMessage());
		}
		finally
		{
			ois.close();
		}
	}
	
	public static HardDisk getInstance()
	{
		return instance;
	}
	
	public int getDiskSize()
	{
		return _SIZE;
	}
	
	public String getDefaultFileName()
	{
		return DEFAULT_FILE_NAME;
	}
	
	public Page<byte[]> pageFault(Long pageId) throws FileNotFoundException, IOException
	{
		readPagesFromHd();
		if(pages.get(pageId) == null)
			System.out.println("Page " + pageId + "doesnt exist in HD");
		
		return pages.get(pageId);
	}
	
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException
	{
		Page<byte[]> page;
		
		readPagesFromHd();
		page = pages.get(moveToRamId);
		if(page == null)
			System.out.println("Page " + moveToRamId + "doesnt exist in HD");
		pages.remove(moveToRamId);
		pages.put(moveToHdPage.getPageId(), moveToHdPage);
		writePagesToHd();
		
		return page;
	}
	
	public void recreateHdFile()
	{
		File pathToHdFile = new File(DEFAULT_FILE_NAME);
		
		pathToHdFile.delete();
		createHdFile();
		initializeHdFile();
	}
}
