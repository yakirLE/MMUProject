package com.hit.view;

import java.util.List;
import java.util.Observable;

public class MMUView extends Observable implements View 
{
	public static final int BYTES_IN_PAGE = 1;
	public static final int NUM_MMU_PAGES = 1;
	
	public MMUView()
	{
		
	}
	
	@Override
	public void open()
	{
		
	}
	
	public void setConfiguration(List<String> commands)
	{
		
	}
}
