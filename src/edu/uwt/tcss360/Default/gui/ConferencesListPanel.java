/*
 * ConferencesListPanel.java
 * Scott Sanderson
 * 11-21-2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.CurrentState;
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
		"Submission Deadline:"};
	
	/**
	 * the number of date fields (being 3 for month day and year)
	 */
	public static final int DATE_FIELD_NUM = 3;
	
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
	
	/**
	 * @editor Brett Cate
	 */
	public void setupPanel()
	{
		setLayout(new BorderLayout());
		Box upper_box = Box.createHorizontalBox();
		JLabel title_label = new JLabel("Conferences:");
		title_label.setFont(new Font(null, Font.BOLD, 20));
		//TODO: add listener to new conf button.
		my_new_conf_button.setVerticalAlignment(SwingConstants.BOTTOM);
		upper_box.add(title_label);
		upper_box.add(Box.createRigidArea(new 
				Dimension(ConferencesFrame.WIDTH / 2,0)));
		upper_box.add(my_new_conf_button);
		upper_box.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));
		JPanel center_panel = new JPanel(new GridLayout(0, 1));
		

		Set<Conference> conferences = 
				getCurrentState().getConferencesManager().getAllConferences();
		for (Conference c : conferences)
		{
			StringBuilder sb = new StringBuilder();
			//I used html for this string since /n wont work for
			//a new line but <br> will.
			sb.append("<html>Conference Name: ");
			sb.append(c.getname());
			sb.append("<br>Program Chair: ");
			String id = c.getUserIds(Role.PROGRAM_CHAIR).get(0);
			String name = getCurrentState()
					.getConferencesManager().getUser(id).getName();
			sb.append(name);
			sb.append("</html>");
			JButton button = new JButton(sb.toString());
			button.setHorizontalAlignment(SwingConstants.LEFT);
			button.addActionListener(new ConferenceSelectAction(c));
			center_panel.add(button);
		}
		//This button panel was used to make the buttons only stretch
		//horizontally and not vertically.
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
		
		/**
		 * @editor Brett Cate
		 */
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
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
			Role role = (Role) JOptionPane.showInputDialog(null, 
					"Select a role.", "Role", JOptionPane.PLAIN_MESSAGE, null, 
					roles_array, roles_array[0]);
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
		/**
		 * @editor Kurt Hardin
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			//creating dialog panel
			JPanel creation_panel = new JPanel(new GridLayout(0,1));
			JPanel name_panel = new JPanel(new FlowLayout());
			name_panel.add(new JLabel("Conference Name:"));
			JTextField name_field = new JTextField(30);
			name_panel.add(name_field);
			creation_panel.add(name_panel);
			//the panels that will hold the date fields.
			JPanel[] date_panels = new JPanel[DATE_TITLES.length];
			//holds the date fields for the start date, end date, and 
			// submission deadline.
			JTextField[][] date_fields = 
					new JTextField[date_panels.length][DATE_FIELD_NUM];
			//creates the date JPanels and inserts them into the panel
			//that will be inserted into the dialog window.
			for (int i = 0; i < date_panels.length; i++)
			{
				date_panels[i] = new JPanel(new FlowLayout());
				date_panels[i].add(new JLabel(DATE_TITLES[i]));
				for (int j = 0; j < DATE_FIELD_NUM; j++)
				{
					String field_format = "**";//day or month format
					if (j == 2)
						field_format = "****";//year format
					try
					{
						date_fields[i][j] = new JFormattedTextField(new 
								MaskFormatter(field_format));
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
			
			//show dialog window.
			int result = JOptionPane.showConfirmDialog(null, creation_panel, 
		               "Create Conference", JOptionPane.OK_CANCEL_OPTION);
			//if OK is pressed:
			if (result == JOptionPane.OK_OPTION)
			{
				Date start_date = null;
				Date end_date = null;
				Date sub_deadline = null;
				try
				{
					//empty conference name
					if (name_field.getText().trim().length() < 1)
						throw new IllegalArgumentException("Conference " +
								"Name Empty");
					String name = name_field.getText().trim();
					//gathers data from the fields to create a conference object.
					StringBuilder date_str = new StringBuilder(10);
					date_str.append(date_fields[0][0].getText()).append("-")
							.append(date_fields[0][1].getText()).append("-")
							.append(date_fields[0][2].getText());
					start_date = Conference.CONFERENCE_DATE_FORMAT.parse(
							 date_str.toString());
					date_str.setLength(0);
					date_str.append(date_fields[1][0].getText()).append("-")
							.append(date_fields[1][1].getText()).append("-")
							.append(date_fields[1][2].getText());
					end_date = Conference.CONFERENCE_DATE_FORMAT.parse(
							date_str.toString());
					Conference conf = new Conference(getCurrentState()
							.getCurrentUser().getID(), 
							name, start_date, end_date);
					//sets the submission deadline for the conference.
					date_str.setLength(0);
					date_str.append(date_fields[2][0].getText()).append("-")
							.append(date_fields[2][1].getText()).append("-")
							.append(date_fields[2][2].getText());
					sub_deadline = Conference.CONFERENCE_DATE_FORMAT.parse(
							date_str.toString());
					conf.setSubmissionDeadline(sub_deadline);
					//adds the conference to the manager and updates the 
					// conference list panel.
					getCurrentState().getConferencesManager()
						.addConference(conf);
					updatePanel();
				}
				catch(ParseException error)
				{
					String message;
					if (start_date == null) 
					{
						message = "Invalid start date";
					} 
					else if (end_date == null) 
					{
						message = "Invalid end date";
					}
					else
					{
						message = "Invalid submission deadline";
					}
					JOptionPane.showMessageDialog(null, message, "Error",
					        JOptionPane.ERROR_MESSAGE);
				}
				catch(IllegalArgumentException error)
				{
					String message = error.getMessage();
					JOptionPane.showMessageDialog(null, message, "Error",
					        JOptionPane.ERROR_MESSAGE);
				}
			}
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
