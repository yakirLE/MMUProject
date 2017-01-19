package com.hit.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CLI 
{
	private InputStream in;
	private OutputStream out;
	private List<String> syntax = new ArrayList<>(Arrays.asList(
			"start",
			"stop",
			"LRU",
			"MFU",
			"Second Chance"));
	
	public CLI(InputStream in, OutputStream out)
	{
		this.in = in;
		this.out = out;
	}
	
	public String[] getConfiguration()
	{
		
	}
	
	public void write(String string)
	{
		
	}
}
