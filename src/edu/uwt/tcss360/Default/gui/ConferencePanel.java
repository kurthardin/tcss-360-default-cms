package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * Panel to be shown when a conference has been selected.
 * @author Brett Cate
 *
 */
@SuppressWarnings("serial")
public class ConferencePanel extends AbstractConferencesPanel 
{

	/**
	 * The Conference whose information is displayed by this panel.
	 */
	public final Conference my_conference;
	
	/**
	 * Creates a ConferencePanel object with the given CurrentState, the given
	 * PanelManager, and the Conference.
	 * 
	 * @param the_current_state The CurrentState to be used.
	 * @param the_manager The PanelManager to be used.
	 * @param the_conference The Conference whose information is displayed by
	 * this panel.
	 */
	public ConferencePanel(final CurrentState the_current_state, final PanelManager the_manager,
			final Conference the_conference) 
	{
		super(the_current_state, the_manager);
		my_conference = the_conference;
		setupPanel();
	}
	
	/**
	 * Sets up the Panel for display, adding the information for the conference
	 * as well as the list of papers.
	 */
	private void setupPanel()
	{
		setLayout(new BorderLayout());
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.Y_AXIS));
		
		JTextPane conference_information = new JTextPane();
		conference_information.setText(getConferenceInfo());
		top_panel.add(conference_information);
		add(top_panel, BorderLayout.NORTH);
		
		JPanel papers_panel = new JPanel();
		addPapersButtons(papers_panel);
		add(papers_panel, BorderLayout.CENTER);
		
//		JPanel role_buttons_panel = new JPanel();
//		role_buttons_panel.setLayout(new FlowLayout());
//		
//		JButton program_chair_button = new JButton("Program Chair");
//		JButton subprogram_chair_button = new JButton("Subprogram Chair");
//		JButton reviewer_button = new JButton("Reviewer");
//		JButton author_button = new JButton("Author");
//		
//		addProgramChairListener(program_chair_button);
//		addSubprogramChairListener(subprogram_chair_button);
//		addReviewerListener(reviewer_button);
//		addAuthorListener(author_button);
//		
//		Set<Role> current_user_roles =
//				my_conference.getRoles(super.getCurrentState().
//						getCurrentUser().getID());
//		role_buttons_panel.add(new JLabel("Please select a role: "));
//		role_buttons_panel.add(program_chair_button);
//		if (!current_user_roles.contains(Role.PROGRAM_CHAIR)) 
//		{
//			program_chair_button.setEnabled(false);
//		}
//		role_buttons_panel.add(subprogram_chair_button);
//		if (!current_user_roles.contains(Role.SUBPROGRAM_CHAIR)) 
//		{
//			subprogram_chair_button.setEnabled(false);
//		}
//		role_buttons_panel.add(reviewer_button);
//		if (!current_user_roles.contains(Role.REVIEWER)) 
//		{
//			reviewer_button.setEnabled(false);
//		}
//		role_buttons_panel.add(author_button);
//		if (!current_user_roles.contains(Role.AUTHOR)) 
//		{
//			author_button.setEnabled(false);
//		}
//		add(role_buttons_panel, BorderLayout.WEST);
	}
	
	/**
	 * Adds the buttons for the papers to the given panel.
	 * @param the_panel The panel the buttons are added to.
	 */
	private void addPapersButtons(final JPanel the_panel)
	{
		List<Paper> papers;
		if(getCurrentState().getCurrentRole() == Role.PROGRAM_CHAIR)
			papers = my_conference.getPapers();
		else
			papers = my_conference.
				getPapers(super.getCurrentState().getCurrentUser().getID(),
				super.getCurrentState().getCurrentRole());
		

		Role role = getCurrentState().getCurrentRole();
		int cols = (role == Role.PROGRAM_CHAIR) ? 3 : 2;
		JPanel buttonspanel = new JPanel(new GridLayout(0,cols));
		buttonspanel.add(new JLabel("Paper Title"));
		if(role == Role.PROGRAM_CHAIR)
			buttonspanel.add(new JLabel("Accepted"));
		buttonspanel.add(new JLabel(""));
		for(Paper p : papers)
		{
			buttonspanel.add(new JLabel(p.getTitle()));
			if(role == Role.PROGRAM_CHAIR)
			{
				if(p.getAcceptanceStatus() == Paper.ACCEPTED)
					buttonspanel.add(new JLabel("Yes"));
				else if(p.getAcceptanceStatus() == Paper.REJECTED)
					buttonspanel.add(new JLabel("No"));
				else
					buttonspanel.add(new JLabel("Undecided"));	
			}
			JButton b = new JButton("Open Paper");
			b.addActionListener(new OpenPaperAction(p));
			buttonspanel.add(b);
		}
		//add paperspanel to jscrollpane, add scrollpane to the_panel
		JPanel holder = new JPanel(new BorderLayout());
		holder.add(buttonspanel,BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane();
		scroll.add(holder);
		the_panel.add(holder);
		//TODO: figure out why adding the scrollpane doesn't work.
		
		
		
		//old code
//		List<Paper> papers = my_conference.
//				getPapers(super.getCurrentState().getCurrentUser().getID(),
//				super.getCurrentState().getCurrentRole());
//		for (Paper a_paper : papers)
//		{
//			the_panel.add(new JButton(a_paper.getTitle()));
//		}
	}
	
//	private void addProgramChairListener(final JButton the_button) 
//	{
//		
//	}
//	
//	private void addSubprogramChairListener(final JButton the_button)
//	{
//		
//	}
//	
//	private void addReviewerListener(final JButton the_button)
//	{
//		
//	}
//	
//	private void addAuthorListener(final JButton the_button)
//	{
//		
//	}
	
	/**
	 * Returns a string containing all of the conference information.
	 * @return
	 */
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
	
	/**
	 * An action for opening a paper panel.
	 * @author Travis Lewis
	 * @version 29 Nov 2012
	 */
	private class OpenPaperAction extends AbstractAction
	{
		final Paper my_paper;
		
		public OpenPaperAction(final Paper the_paper)
		{
			my_paper = the_paper;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			getCurrentState().setCurrentPaper(my_paper);
			getPanelManager().pushPanel(new PaperPanel(getCurrentState(),
					getPanelManager(), my_paper));
		}
		
	}
	
//	public static void main(final String[] the_arguments)
//	{
//		JFrame frame = new JFrame("Conference");
//		Conference c = new Conference("brettcate",
//				"Pacific Northwest Enterprise Risk Forum",
//				new Date());
//		c.setFinalRevisionDeadline(new Date());
//		c.setRecommendationDeadline(new Date());
//		c.setReviewDeadline(new Date());
//		c.setSubmissionDeadline(new Date());
//		c.authorizeUser("brettcate", Role.REVIEWER);	
//		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
//											 ConferencesFrame.HEIGHT));
//		ConferencesManager cmgr = new ConferencesManager();
//		cmgr.addUser(new User("brettcate"));
//		CurrentState cs = new CurrentState(cmgr);
//		cs.setCurrentUser("brettcate");
//		cs.setCurrentRole(Role.PROGRAM_CHAIR);
//		frame.add(new ConferencePanel(cs, null, c));
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}

}
