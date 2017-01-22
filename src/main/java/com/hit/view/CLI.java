package com.hit.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.Configuration;

public class CLI 
{
	private Scanner input;
	private PrintWriter output;
	private List<String> possibleAlgos = new ArrayList<>(Arrays.asList(
			"LRU",
			"MFU",
			"Second Chance"));
	private List<String> startStopTokens = new ArrayList<>(Arrays.asList(
			"start",
			"stop"
			));
	
	public CLI(InputStream in, OutputStream out)
	{
		this.input = new Scanner(in);
		this.output = new PrintWriter(out);
	}
	
	public String[] getConfiguration()
	{
		String inputCommand;
		String[] tokens;
		
		inputCommand = input.nextLine();
		tokens = inputCommand.split(" ");
		while(!isValidInput(tokens))
		{
			write("Not a valid command");
			write("Please enter required algorithm and RAM capacity");
			inputCommand = input.nextLine();
			tokens = inputCommand.split(" ");
		}
		
		return tokens;
	}
	
	private boolean isValidInput(String[] tokens)
	{
		int commandLength;
		boolean isValid = false;
		String firstToken, secondToken;
		
		commandLength = tokens.length;
		if(commandLength >= 2 && commandLength <= 3)
		{
			firstToken = tokens[0];
			secondToken = tokens[commandLength - 1];
			if(commandLength == 3)
				firstToken += " " + tokens[1];
			if(possibleAlgos.contains(firstToken) && secondToken.matches("\\d+"))
				isValid = true;
		}
		else if(commandLength == 1 && startStopTokens.contains(tokens[0]))
			isValid = true;
		
		return isValid;
	}
	
	public void write(String stringToWrite)
	{
		output.write(stringToWrite);
		output.flush();
	}
}
