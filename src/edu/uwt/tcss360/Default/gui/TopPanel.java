package edu.uwt.tcss360.Default.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import edu.uwt.tcss360.Default.model.CurrentState;

public class TopPanel extends AbstractConferencesPanel
{
	
	private final JButton my_back_button;
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 * @param the_panel_mgr the panel manager object creating this panel.
	 */
	public TopPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_back_button = new JButton("Back");
		setupPanel();
	}
	
	
	/**
	 * sets up the top panel to be used.
	 */
	private void setupPanel()
	{
		setPreferredSize(new Dimension(ConferencesFrame.WIDTH, 
				ConferencesFrame.TOP_HEIGHT));
		Box box = Box.createHorizontalBox();
		my_back_button.setAlignmentX(LEFT_ALIGNMENT);
		JLabel label = new JLabel("temporary name");
		label.setAlignmentX(RIGHT_ALIGNMENT);
		box.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
				ConferencesFrame.TOP_HEIGHT));
		
		box.add(my_back_button);
		box.add(Box.createRigidArea(new Dimension((int) (ConferencesFrame.WIDTH - 
				label.getPreferredSize().getWidth() - 
				my_back_button.getPreferredSize().getWidth()),0)));
		box.add(label);
		add(box);
	}
	
	private class BackAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent the_event)
		{
			getPanelManager().popPanel();
		}
	}

	@Override
	public void updatePanel()
	{
		// TODO Auto-generated method stub
		
	}
}
