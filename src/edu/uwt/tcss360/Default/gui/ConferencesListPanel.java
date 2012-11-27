/**
 * ConferencesListPanel.java
 * Scott Sanderson
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;


/**
 * Panel that lists the conferences and allows the user to select
 * a conference.
 * @author Scott Sanderson
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConferencesListPanel extends AbstractConferencesPanel
{
	/**
	 * Holds the dialog strings for the new conference creation dialog.
	 */
	public static final String[] DATE_TITLES = {"Start Date:", "End Date:", 
		"Submission Deadline:", "Review Deadline:", 
		"Recommendation Deadline:", "Final Submission Deadline:"};
	
	/**
	 * the new conference button.
	 */
	final JButton my_new_conf_button;
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 * @param the_panel_mgr the panel manager object creating this panel.
	 */
	public ConferencesListPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_new_conf_button = new JButton("New Conference");
		my_new_conf_button.addActionListener(new NewConferenceAction());
		setupPanel();
	}
	
	public void setupPanel()
	{
		setLayout(new BorderLayout());
		Box upper_box = Box.createHorizontalBox();
		JLabel title_label = new JLabel("Conferences:");
		title_label.setFont(new Font(null, Font.BOLD, 20));
		//TODO: add listener to new conf button.
		my_new_conf_button.setVerticalAlignment(SwingConstants.BOTTOM);
		upper_box.add(title_label);
		upper_box.add(Box.createRigidArea(new Dimension(ConferencesFrame.WIDTH / 2,0)));
		upper_box.add(my_new_conf_button);
		upper_box.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));
		JPanel center_panel = new JPanel(new GridLayout(0, 1));
		

		Set<Conference> conferences = 
				getCurrentState().getConferencesManager().getAllConferences();
		for (Conference c : conferences)
		{
			//This button panel was used to make the buttons only stretch
			//horizontally and not vertically.
			JButton button = new JButton(c.getname());
			button.addActionListener(new ConferenceSelectAction(c));
			center_panel.add(button);
		}
		
		JPanel center_outer_panel = new JPanel(new BorderLayout());
		//fixes the vertical stretching problem with the conference buttons
		//since panels in BorderLayout.NORTH only stretch horizontally.
		center_outer_panel.add(center_panel, BorderLayout.NORTH);
		JScrollPane scroll_pane = new JScrollPane(center_outer_panel);
		add(upper_box, BorderLayout.NORTH);
		add(scroll_pane, BorderLayout.CENTER);
	}
	
	private class ConferenceSelectAction extends AbstractAction
	{
		private final Conference my_conference;
		
		public ConferenceSelectAction(final Conference the_conference)
		{
			super();
			my_conference = the_conference;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.out.println(my_conference);
			Set<Role> roles = my_conference.getRoles(
					getCurrentState().getCurrentUser().getID());
			Role[] roles_array = new Role[roles.size() + 1];
			roles_array[0] = Role.USER;
			int i = 1;
			for(Role role : roles)
			{
				roles_array[i] = role;
				i++;
			}
			Role role = (Role) JOptionPane.showInputDialog(null, "Select a role.", 
					"Role", JOptionPane.PLAIN_MESSAGE, null, roles_array, 
					Role.PROGRAM_CHAIR);
			if (role != null)
			{
				getCurrentState().setCurrentRole(role);
				getCurrentState().setCurrentConference(my_conference.getID());
				//commented out until ConferencePanel is implemented.
				getPanelManager().pushPanel(new ConferencePanel(
						getCurrentState(), getPanelManager(), my_conference));
			}
		}
		
	}
	
	private class NewConferenceAction extends AbstractAction
	{		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JPanel creation_panel = new JPanel(new GridLayout(0,1));
			JPanel name_panel = new JPanel(new FlowLayout());
			name_panel.add(new JLabel("Conference Name:"));
			JTextField name_field = new JTextField(30);
			name_panel.add(name_field);
			creation_panel.add(name_panel);
			JPanel[] date_panels = new JPanel[DATE_TITLES.length];
			JTextField[][] date_fields = new JTextField[date_panels.length][3];
			for (int i = 0; i < date_panels.length; i++)
			{
				date_panels[i] = new JPanel(new FlowLayout());
				date_panels[i].add(new JLabel(DATE_TITLES[i]));
				for (int j = 0; j < 3; j++)
				{
					String field_format = "**";//day or month
					if (j == 2)//year
						field_format = "****";
					try
					{
						date_fields[i][j] = new JFormattedTextField(new MaskFormatter(field_format));
					} catch (ParseException e1) {e1.printStackTrace();}
					switch (j)
					{
					case 0: date_fields[i][j].setText("MM");
						break;
					case 1: date_fields[i][j].setText("DD");
						break;
					default: date_fields[i][j].setText("YYYY");
					}
					date_panels[i].add(date_fields[i][j]);
					if (j != 2)
						date_panels[i].add(new JLabel("/"));
				}
				
				creation_panel.add(date_panels[i]);
			}
			int result = JOptionPane.showConfirmDialog(null, creation_panel, 
		               "Create Conference", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION)
			{
				//TODO: create conference given date information, not just the title.
				String conference_name = name_field.getText();
				if (conference_name.trim().length() < 1)
				{
					JOptionPane.showMessageDialog(new JFrame(), "Conference Name Invalid", "Error",
					        JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					User current = getCurrentState().getCurrentUser();
					Conference new_conf = new Conference(current.getID(), conference_name, new Date());
					getCurrentState().getConferencesManager().addConference(new_conf);
					updatePanel();
				}
			}
			
			
			
			
//			String conference_name = JOptionPane.showInputDialog("Enter Conference Name");
//			if (conference_name.trim().length() < 1)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Conference Name Invalid", "Error",
//				        JOptionPane.ERROR_MESSAGE);
//			}
//			else
//			{
//				if (getCurrentState() == null)
//					System.out.println("asafsdsd");
//				User current = getCurrentState().getCurrentUser();
//				Conference new_conf = new Conference(current.getID(), conference_name, new Date());
//				getCurrentState().getConferencesManager().addConference(new_conf);
//				updatePanel();
//			}
		}
	}
	
	
	@Override
	public void updatePanel() 
	{
		// TODO Auto-generated method stub
		setVisible(false);
		removeAll();
		setupPanel();
		setVisible(true);
	}
	
	//test main
//	public static void main(String[]args)
//	{
//		JFrame frame = new JFrame("Conferences Manager");
//		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH, 
//				ConferencesFrame.HEIGHT));
//		frame.add(new ConferencesListPanel(null, null));
//		
//		
//		
//		
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}
}
