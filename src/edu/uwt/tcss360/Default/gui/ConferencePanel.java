package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import edu.uwt.tcss360.Default.model.Conference;
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
	public ConferencePanel(final CurrentState the_current_state, 
			final PanelManager the_manager,
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
		conference_information.setEditable(false);
		top_panel.add(conference_information);
		add(top_panel, BorderLayout.NORTH);
		
		JPanel papers_panel = new JPanel(new BorderLayout());
		addPapersButtons(papers_panel);
		add(papers_panel, BorderLayout.CENTER);
		
		JPanel paper_submission_panel = new JPanel();
		addPaperSubmissionButton(paper_submission_panel);
		add(paper_submission_panel, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Adds the button for submitting a paper to the given panel.
	 * @param the_panel	The panel to which the button is added.
	 */
	private void addPaperSubmissionButton(final JPanel the_panel)
	{
		final JButton paper_submission_button = new JButton("Submit a Paper");
		if (getCurrentState().getCurrentConference().
				getSubmissionDeadline().compareTo(new Date()) < 0)
		{
			paper_submission_button.setEnabled(false);
			paper_submission_button.setText("The submission deadline has passed.");
		}
		paper_submission_button.addActionListener(new PaperSubmissionAction(the_panel));
		the_panel.add(paper_submission_button);
	}
	
	
	
	/**
	 * Adds the buttons for the papers to the given panel.
	 * @param the_panel The panel the buttons are added to.
	 * edited by Scott Sanderson to add SPC display for program chair.
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
		int cols = 2;
		if (role == Role.AUTHOR)
			cols = 3;
		if (role == Role.PROGRAM_CHAIR)
			cols = 4;
//		int cols = (role == Role.PROGRAM_CHAIR || role == Role.AUTHOR) ? 3 : 2;
		JPanel buttonspanel = new JPanel(new GridLayout(0,cols));
		buttonspanel.add(new JLabel("Paper Title"));
		if(role == Role.PROGRAM_CHAIR || role == Role.AUTHOR)
		    buttonspanel.add(new JLabel("Acceptance Status"));
		if(role == Role.PROGRAM_CHAIR)
			buttonspanel.add(new JLabel("Subprogram Chair"));
		buttonspanel.add(new JLabel(""));
		for(Paper p : papers)
		{
			buttonspanel.add(new JLabel(p.getTitle()));
			
			if(role == Role.PROGRAM_CHAIR || role == Role.AUTHOR)
			{
    			if(p.getAcceptanceStatus() == Paper.ACCEPTED)
    				buttonspanel.add(new JLabel("   Yes"));
    			else if(p.getAcceptanceStatus() == Paper.REJECTED)
    				buttonspanel.add(new JLabel("   No"));
    			else
    				buttonspanel.add(new JLabel("   Undecided"));
			}
			if(role == Role.PROGRAM_CHAIR)
			{
				User spc = getCurrentState().getConferencesManager().
						getUser(p.getSubprogramChairID());
				String name = (spc == null) ? "None" : spc.getName();
				buttonspanel.add(new JLabel(name));
			}
			JButton b = new JButton("Open Paper");
			b.addActionListener(new OpenPaperAction(p));
			buttonspanel.add(b);
		}
		//add paperspanel to jscrollpane, add scrollpane to the_panel
		JPanel holder = new JPanel(new BorderLayout());
		holder.add(buttonspanel,BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(holder);
		the_panel.add(scroll);
	}
	
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
		sb.append(Conference.CONFERENCE_DATE_FORMAT.
				format(my_conference.getStartDate()));
		sb.append(System.getProperty("line.separator"));
		sb.append("End Date: 			");
		sb.append(Conference.CONFERENCE_DATE_FORMAT.
				format(my_conference.getEndDate()));
		sb.append(System.getProperty("line.separator"));
		sb.append("Submission Deadline: 		");
		sb.append(Conference.CONFERENCE_DATE_FORMAT.
				format(my_conference.getSubmissionDeadline()));		
		return sb.toString();
	}
	
	@Override
	public void updatePanel() 
	{
		setVisible(false);
		removeAll();
		setupPanel();
		setVisible(true);
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
		public void actionPerformed(final ActionEvent arg0) 
		{
			getCurrentState().setCurrentPaper(my_paper);
			getPanelManager().pushPanel(new PaperPanel(getCurrentState(),
					getPanelManager(), my_paper));
		}
		
	}
	
	private class PaperSubmissionAction extends AbstractAction
	{
		
		private final JPanel my_parent_panel;
				
		public PaperSubmissionAction(final JPanel the_parent_panel)
		{
			my_parent_panel = the_parent_panel;
		}
		
		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
		    //check for 4 papers
		    List<Paper> papers = getCurrentState().getCurrentConference()
		    .getPapers(getCurrentState().getCurrentUser().getID(), 
		            Role.AUTHOR);
		    
			
			if(papers.size() < 4)
			{
			    JFileChooser chooser = new JFileChooser();
    			if (chooser.showOpenDialog(my_parent_panel) == 
    					JFileChooser.APPROVE_OPTION) 
    			{
    				String title = (String) JOptionPane.
    						showInputDialog(null, "Please enter the paper title");
    				CurrentState cs = getCurrentState();
    				//TODO: fix where the paper is saved
    				cs.getCurrentConference().addPaper(cs.getCurrentUser().getID(),
    						new Paper(cs.getCurrentUser().getID(), title,
    						chooser.getSelectedFile(),
    						cs.getCurrentConference().getDirectory()));
    				updatePanel();
    				JOptionPane.showMessageDialog(null, "Your paper has been " +
    						"submitted. If it is not displayed, please go back and" +
    						" select Author as your role.");
    			}
			}
			else
			{
			    JOptionPane.showMessageDialog(null, "You've already " +
			    		"submitted the maximum number of papers");

			}
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
