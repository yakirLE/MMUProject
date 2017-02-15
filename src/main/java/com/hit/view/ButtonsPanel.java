package com.hit.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel
{
	private JButton play;
	private JButton playAll;
	
	public ButtonsPanel(MMUView view) 
	{
		GridBagConstraints constraints;
		
		setLayout(new GridBagLayout());
		constraints = createPlayButton();
		add(play, constraints);
		constraints = createPlayAllButton();
		add(playAll, constraints);
		registerToPlayButton(view);
		registerToPlayAllButton(view);
	}

	private void registerToPlayAllButton(MMUView view) 
	{
		playAll.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				view.playAllButton_Clicked();
			}
		});
	}

	private void registerToPlayButton(MMUView view) 
	{
		play.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				view.playButton_Clicked();
			}
		});
	}

	private GridBagConstraints createPlayAllButton() 
	{
		GridBagConstraints constraints = new GridBagConstraints();
		playAll = new JButton("PLAY ALL");
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 2;
		constraints.gridy = 0;
		
		return constraints;
	}

	private GridBagConstraints createPlayButton() 
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		play = new JButton("PLAY");
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(0, 0, 0, 50);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		return constraints;
	}
}
