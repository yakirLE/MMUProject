package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel implements ActionListener
{
	private JButton play;
	private JButton playAll;
	
	public ButtonsPanel() 
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		play = new JButton("PLAY");
		playAll = new JButton("PLAY ALL");
		setLayout(new GridBagLayout());
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(0, 0, 0, 50);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(play, constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(playAll, constraints);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

}
