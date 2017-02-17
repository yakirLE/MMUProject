package com.hit.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CommandsPanel extends JPanel 
{
	private JLabel currentCommandLabel;
	private JTextField currentCommandTextField;
	
	public CommandsPanel() 
	{
		setLayout(new FlowLayout());
		createAndAddCurrentCommandLabel();
		createAndAddCurrentCommandTextField();
	}

	private void createAndAddCurrentCommandTextField() 
	{
		currentCommandTextField = new JTextField(20);
		currentCommandTextField.setEditable(false);
		currentCommandTextField.setBackground(Color.WHITE);
		add(currentCommandTextField);
	}

	private void createAndAddCurrentCommandLabel() 
	{
		currentCommandLabel = new JLabel("Current Command:");
		add(currentCommandLabel);
	}
	
	public void setText(String text)
	{
		currentCommandTextField.setText(text);
	}
}
