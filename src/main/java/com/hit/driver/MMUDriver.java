package com.hit.driver;

import java.lang.reflect.InvocationTargetException;

import com.hit.controller.MMUController;
import com.hit.model.MMUModel;
import com.hit.view.CLI;
import com.hit.view.MMUView;

public class MMUDriver 
{
	public static void main(String[] args) throws InterruptedException, InvocationTargetException 
	{
		boolean wasStartInitiated = false;
		String[] configuration;
		CLI cli = new CLI(System.in, System.out);
		MMUModel model;
		MMUView view;
		MMUController controller;
		
		while((configuration = cli.getConfiguration()) != null)
		{
			if(configuration[0].equals("start"))
			{
				wasStartInitiated = true;
				cli.write("Please enter required algorithm and RAM capacity\r\n");
			}
			else if(wasStartInitiated)
			{
				cli.write("Processing...\r\n");
				model = new MMUModel(configuration);
				view = new MMUView();
				controller = new MMUController(model, view);
				model.addObserver(controller);
				view.addObserver(controller);
				model.start();
				cli.write("Done\r\n");
				wasStartInitiated = false;
			}
		}
		
		cli.write("Thank you.\r\n");
	}
}
