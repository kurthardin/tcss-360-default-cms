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
	
	private boolean my_role_display_state;
	
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
		my_back_button.addActionListener(new BackAction());
		my_label = new JLabel();
		my_role_display_state = false;
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
		my_label.setAlignmentX(RIGHT_ALIGNMENT);
		box.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
				ConferencesFrame.TOP_HEIGHT));
		User user = getCurrentState().getCurrentUser();
		//used html for this string to use <br> (line break)
		StringBuilder sb = new StringBuilder("<html>");
		if (user != null)
		{
			sb.append("Name: ");
			sb.append(user.getName());
			sb.append("<br>");//line break
			if (my_role_display_state)
			{
				sb.append("Role: ");
				sb.append(getCurrentState().getCurrentRole());
			}
		}
		sb.append("</html>");
		my_label.setText(sb.toString());
		box.add(my_back_button);
		box.add(Box.createRigidArea(new Dimension((int) (ConferencesFrame.WIDTH - 
				my_label.getPreferredSize().getWidth() - 
				my_back_button.getPreferredSize().getWidth()),0)));
		box.add(my_label);
		add(box);
	}
	
	public void setRoleDisplay(final boolean the_choice)
	{
		my_role_display_state = the_choice;
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
		setVisible(false);
		removeAll();
		setupPanel();
		setVisible(true);
	}
}
