/**
 * LoginPanel.java
 * Scott Sanderson
 * 11/15/2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;

/**
 * the Login Panel class that will be used when a user wants to log in.
 * @author Scott Sanderson
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginPanel extends AbstractConferencesPanel
{
	/*
	 * fields
	 */
	
	/**
	 * text field where the user will input their email/ID
	 */
	private JTextField my_text_field = new JTextField(15);
	
	/**
	 * login button.
	 */
	private final JButton my_login_button = new JButton("Login");
	
	/*
	 * methods
	 */
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 */
	public LoginPanel(final CurrentState the_state)
	{
		super(the_state);
		setupPanel();
	}
	
	/**
	 * sets up the panel to be used.
	 * TODO: Fix positioning for login panel and add more
	 * text indicating that it is the login screen.
	 */
	private void setupPanel()
	{
		JPanel login_panel = new JPanel(new FlowLayout());
		login_panel.add(my_text_field);
		login_panel.add(my_login_button);
		my_login_button.addActionListener(new LoginAction());
		this.add(login_panel, BorderLayout.CENTER);
	}
	
	/**
	 * action class that will be used for the login buttom.
	 * @author Scott Sanderson
	 * @version 1.0
	 */
	private class LoginAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent the_event)
		{
			String text = my_text_field.getText();
			if (text.trim().length() > 0)
			{
				ConferencesManager cf = my_current_state.getConferencesManager();
				if (cf.getUser(text) == null)
				{
					//TODO: prompt user for name for registration,
					//set up a User object given that information
					//and add them to the ConferencesManager object.
				}
				my_current_state.setCurrentUser(text);
			}
		}
	}
}
