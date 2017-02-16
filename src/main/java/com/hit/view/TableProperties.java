package com.hit.view;

import java.util.List;

public class TableProperties 
{
	private int index;
	private String processName;
	private List<String> data;
	
	public int getIndex() 
	{
		return index;
	}
	
	public void setIndex(int index) 
	{
		this.index = index;
	}
	
	public String getProcessName() 
	{
		return processName;
	}
	
	public void setProcessName(String processName) 
	{
		this.processName = processName;
	}
	
	public List<String> getData() 
	{
		return data;
	}
	
	public void setData(List<String> data) 
	{
		this.data = data;
	}
	
	public String toString()
	{
		return this.processName + " ";//idx=" + this.index + " data=" + this.data.toString();
	}
}
