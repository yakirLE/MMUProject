package com.hit.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CountersPanel extends JPanel implements ActionListener
{
	private JLabel pageFaultLabel;
	private JLabel pageReplacementLabel;
	private JTextField pageFaultTextField;
	private JTextField pageReplacementTextField;
	
	public CountersPanel() 
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		pageFaultLabel = new JLabel("Page Fault Amount");
		pageReplacementLabel = new JLabel("Page Replacement Amount");
		pageFaultTextField = new JTextField("0", 2);
		pageReplacementTextField = new JTextField("0", 2);
		setLayout(new GridBagLayout());
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.insets = new Insets(0, 0, 0, 20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(pageFaultLabel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(pageFaultTextField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 20);
		add(pageReplacementLabel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(pageReplacementTextField, constraints);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

}
