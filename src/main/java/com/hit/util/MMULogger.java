package com.hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger 
{
	public final static String DEFAULT_FILE_NAME = "log.txt";
	private FileHandler handler;
	private static MMULogger instance = new MMULogger();
	
	private MMULogger()
	{
		try 
		{
			handler = new FileHandler(DEFAULT_FILE_NAME);
			handler.setFormatter(new OnlyMessageFormatter());
		}
		catch (Exception e)
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			System.exit(0);
		}
	}
	
	public static MMULogger getInstance()
	{
		return instance;
	}
	
	public synchronized void write(String command, Level level)
	{
		LogRecord logRecord = new LogRecord(level, command);
		
		handler.publish(logRecord);
	}
	
	public class OnlyMessageFormatter extends Formatter
	{
		public OnlyMessageFormatter()
		{
			super();
		}
		
		@Override 
		public String format(final LogRecord record)
		{
			return record.getMessage() + System.lineSeparator();
		}
	}
}
