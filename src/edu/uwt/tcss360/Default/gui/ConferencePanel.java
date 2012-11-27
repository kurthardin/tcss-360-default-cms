package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Date;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * 
 * @author Brett Cate
 *
 */
@SuppressWarnings("serial")
public class ConferencePanel extends AbstractConferencesPanel 
{

	public final Conference my_conference;
	
	public ConferencePanel(final CurrentState the_current_state, final PanelManager the_manager,
			final Conference the_conference) 
	{
		super(the_current_state, the_manager);
		my_conference = the_conference;
		setupPanel();
	}
	
	private void setupPanel()
	{
		setLayout(new BorderLayout());
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.Y_AXIS));
		
		JTextPane conference_information = new JTextPane();
		conference_information.setText(getConferenceInfo());
		top_panel.add(conference_information);
		add(top_panel, BorderLayout.NORTH);
		
		JPanel role_buttons_panel = new JPanel();
		role_buttons_panel.setLayout(new FlowLayout());
		
		JButton program_chair_button = new JButton("Program Chair");
		JButton subprogram_chair_button = new JButton("Subprogram Chair");
		JButton reviewer_button = new JButton("Reviewer");
		JButton author_button = new JButton("Author");
		
		addProgramChairListener(program_chair_button);
		addSubprogramChairListener(subprogram_chair_button);
		addReviewerListener(reviewer_button);
		addAuthorListener(author_button);
		
		Set<Role> current_user_roles =
				my_conference.getRoles(super.getCurrentState().
						getCurrentUser().getID());
		role_buttons_panel.add(new JLabel("Please select a role: "));
		role_buttons_panel.add(program_chair_button);
		if (!current_user_roles.contains(Role.PROGRAM_CHAIR)) 
		{
			program_chair_button.setEnabled(false);
		}
		role_buttons_panel.add(subprogram_chair_button);
		if (!current_user_roles.contains(Role.SUBPROGRAM_CHAIR)) 
		{
			subprogram_chair_button.setEnabled(false);
		}
		role_buttons_panel.add(reviewer_button);
		if (!current_user_roles.contains(Role.REVIEWER)) 
		{
			reviewer_button.setEnabled(false);
		}
		role_buttons_panel.add(author_button);
		if (!current_user_roles.contains(Role.AUTHOR)) 
		{
			author_button.setEnabled(false);
		}
		add(role_buttons_panel, BorderLayout.WEST);
	}
	
	private void addProgramChairListener(final JButton the_button) 
	{
		
	}
	
	private void addSubprogramChairListener(final JButton the_button)
	{
		
	}
	
	private void addReviewerListener(final JButton the_button)
	{
		
	}
	
	private void addAuthorListener(final JButton the_button)
	{
		
	}
	
	private String getConferenceInfo() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Conference Title: 		");
		sb.append(my_conference.getname());
		sb.append(System.getProperty("line.separator"));
		sb.append("Start Date: 			");
		sb.append(my_conference.getStartDate());
		sb.append(System.getProperty("line.separator"));
		sb.append("End Date: 			");
		sb.append(my_conference.getEndDate());
		sb.append(System.getProperty("line.separator"));
		sb.append("Submission Deadline: 		");
		sb.append(my_conference.getSubmissionDeadline());
		sb.append(System.getProperty("line.separator"));
		sb.append("Review Deadline: 		");
		sb.append(my_conference.getReviewDeadline());
		sb.append(System.getProperty("line.separator"));
		sb.append("Recommendation Deadline: 	");
		sb.append(my_conference.getRecommendationDeadline());
		sb.append(System.getProperty("line.separator"));
		sb.append("Final Revision Deadline: 		");
		sb.append(my_conference.getFinalRevisionDeadline());
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
	
	@Override
	public void updatePanel() 
	{
		// TODO Auto-generated method stub

	}
	
	public static void main(final String[] the_arguments)
	{
		JFrame frame = new JFrame("Conference");
		Conference c = new Conference("brettcate",
				"Pacific Northwest Enterprise Risk Forum",
				new Date());
		c.setFinalRevisionDeadline(new Date());
		c.setRecommendationDeadline(new Date());
		c.setReviewDeadline(new Date());
		c.setSubmissionDeadline(new Date());
		c.authorizeUser("brettcate", Role.REVIEWER);
		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
											 ConferencesFrame.HEIGHT));
		ConferencesManager cmgr = new ConferencesManager();
		cmgr.addUser(new User("brettcate"));
		CurrentState cs = new CurrentState(cmgr);
		cs.setCurrentUser("brettcate");
		frame.add(new ConferencePanel(cs, null, c));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
