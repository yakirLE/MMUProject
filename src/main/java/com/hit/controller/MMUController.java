package com.hit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller, Observer
{
	private Model model;
	private View view;
	
	public MMUController(Model model, View view)
	{
		this.model = model;
		this.view = view;
	}
	
	public void update(Observable o, Object arg)
	{
		if(o == model)
			view.open();
		else if(o == view)
		{
			model.readData();
			((MMUView)view).setConfiguration(createConfigurationFromModel());
		}
		else
			throw new IllegalArgumentException();
	}
	
	private List<String> createConfigurationFromModel()
	{
		List<String> configuration = new ArrayList<>();
		MMUModel asMMUModel;
		
		asMMUModel = (MMUModel)model;
		configuration.add(Integer.toString(asMMUModel.ramCapacity));
		configuration.add(Integer.toString(asMMUModel.numProcesses));
		configuration.addAll(asMMUModel.getCommands());
		
		return configuration;
	}
}
