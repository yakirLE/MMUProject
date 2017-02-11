package com.hit.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.View;

public class MMUController implements Controller, Observer
{
	Model model;
	View view;
	
	public MMUController(Model model, View view)
	{
		this.model = model;
		this.view = view;
	}
	
	public void update(Observable o, Object arg)
	{
		List<String> commands;
		
		if(o == model)
		{
			commands = ((MMUModel)model).getCommands();
		}
		else if(o == view)
		{
			
		}
		else
			throw new IllegalArgumentException();
	}
}
