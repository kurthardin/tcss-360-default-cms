/**
 * TopPanel.java
 * Scott Sanderson
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.User;

/**
 * Panel that will be at the top of the screen except 
 * when at the login panel.
 * @author Scott Sanderson
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TopPanel extends AbstractConferencesPanel
{
	
	private final JButton my_back_button;
	
	private final JLabel my_label;
	
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
		my_label = new JLabel();
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
		my_back_button.addActionListener(new BackAction());
//		JLabel label = new JLabel(getCurrentState().getCurrentUser().getEmail());
		my_label.setAlignmentX(RIGHT_ALIGNMENT);
		box.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
				ConferencesFrame.TOP_HEIGHT));
		User user = getCurrentState().getCurrentUser();
		if (user != null)
			my_label.setText(user.getName());
		else
			my_label.setText("no current user");//remove
		
		box.add(my_back_button);
		box.add(Box.createRigidArea(new Dimension((int) (ConferencesFrame.WIDTH - 
				my_label.getPreferredSize().getWidth() - 
				my_back_button.getPreferredSize().getWidth()),0)));
		box.add(my_label);
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
		removeAll();
		setupPanel();
	}
}
