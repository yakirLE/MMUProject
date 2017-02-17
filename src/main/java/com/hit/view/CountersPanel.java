package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CountersPanel extends JPanel
{
	private JLabel pageFaultLabel;
	private JLabel pageReplacementLabel;
	private JTextField pageFaultTextField;
	private JTextField pageReplacementTextField;
	
	public CountersPanel() 
	{
		setLayout(new GridBagLayout());
		createAndAddPageFaultLabel();
		createAndAddPageFaultTextField();
		createAndAddPageReplacementLabel();
		createAndAddPageReplacementTextField();
	}
	
	public JTextField getPageFaultTextField()
	{
		return this.pageFaultTextField;
	}
	
	public JTextField getPageReplacementTextField()
	{
		return this.pageReplacementTextField;
	}
	
	private void createAndAddPageFaultLabel()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		pageFaultLabel = new JLabel("Page Fault Amount");
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.insets = new Insets(0, 0, 0, 20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(pageFaultLabel, constraints);
	}
	
	private void createAndAddPageReplacementLabel()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		pageReplacementLabel = new JLabel("Page Replacement Amount");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 20);
		add(pageReplacementLabel, constraints);
	}
	
	private void createAndAddPageFaultTextField()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		pageFaultTextField = new JTextField("0", 2);
		pageFaultTextField.setEditable(false);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(pageFaultTextField, constraints);
	}
	
	private void createAndAddPageReplacementTextField()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		pageReplacementTextField = new JTextField("0", 2);
		pageReplacementTextField.setEditable(false);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(pageReplacementTextField, constraints);
	}
}
