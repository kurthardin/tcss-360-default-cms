/*
 * LoginPanel.java
 * Scott Sanderson
 * 11-15-2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private JTextField my_text_field;
	
	/**
	 * login button.
	 */
	private final JButton my_login_button;
	
	/*
	 * methods
	 */
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 * @param the_panel_mgr the panel manager object creating this panel.
	 */
	public LoginPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_login_button = new JButton("Login");
		my_login_button.addActionListener(new LoginAction());
		my_text_field = new JTextField(15);
		setupPanel();
	}
	
	/**
	 * sets up the login panel to be used.
	 */
	private void setupPanel()
	{
		Box login_box = Box.createVerticalBox();
		JPanel email_panel = new JPanel(new FlowLayout());
		JLabel title_label = new JLabel("Login with your Email");
		title_label.setFont(new Font(null, Font.BOLD, 18));
		JLabel enter_label = new JLabel("Email:");
		email_panel.add(enter_label);
		email_panel.add(my_text_field);
		
		title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		email_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		my_login_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		login_box.add(title_label);
		login_box.add(email_panel);
		login_box.add(my_login_button);
		//creates an empty "border" above the login box so that the login box
		//is more centered in the middle of the frame.
		setBorder(BorderFactory.createEmptyBorder((ConferencesFrame.HEIGHT + 
				ConferencesFrame.TOP_HEIGHT) / 3, 0, 0, 0));
		add(login_box);
	}
	
	/**
	 * action class that will be used for the login buttom.
	 * @author Scott Sanderson
	 * @version 1.0
	 */
	private class LoginAction extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent the_event)
		{
			String text = my_text_field.getText().trim();
			if (text.trim().length() > 0)
			{
				ConferencesManager cf = getCurrentState()
						.getConferencesManager();
				if (cf.getUser(text) != null)
				{
					getCurrentState().setCurrentUser(text);
					getPanelManager().pushPanel(new ConferencesListPanel(
							getCurrentState(), getPanelManager()));
				}
				else
				{
					String message = text + " is not a valid username";
					JOptionPane.showMessageDialog(new JFrame(), message, 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	@Override
	public void updatePanel()
	{
		//no need to do anything for this panel...
	}
}
