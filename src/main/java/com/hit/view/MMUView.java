package com.hit.view;

import java.util.List;
import java.util.Observable;

import javax.swing.SwingUtilities;

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
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				createAndShowGui();
			}
		});
		
		setChanged();
		notifyObservers();
	}
	
	private void createAndShowGui() 
	{
		
	}
	
	public void setConfiguration(List<String> commands)
	{
		
	}
}
