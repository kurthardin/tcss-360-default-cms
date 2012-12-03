/*
 * PaperPanel.java
 * Travis Lewis
 * 11-25-2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.Recommendation;
import edu.uwt.tcss360.Default.model.Review;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;

/**
 * Creates a PaperPanel, which displays information relevant to the
 * current Paper object.
 * @author Travis Lewis
 * @version 30 Nov 2012
 */
@SuppressWarnings("serial")
public class PaperPanel extends AbstractConferencesPanel 
{
	/*
	 * info that needs to be shown for a paper:
	 * - author
	 * - title
	 * - button to download paper
	 * - button to upload paper
	 * - reviewers
	 * - reviewers scores
	 * - button to open review panels
	 * - recommendationer (heh) (aka: SPC in charge)
	 * - recomendationateer score
	 * - button to open recomendation panel
	 */
	
	// probably only for testing.
	private Paper my_paper;
	
	//a paper can have an arbitrary number of reviewers. so put the review
	//grid in a JScrollPanel or JScrollFrame or JScrollPane

	public PaperPanel(CurrentState the_state, PanelManager the_panel_mgr) 
	{
		super(the_state, the_panel_mgr);
		my_paper = getCurrentState().getCurrentPaper();
		setupPanel(false);
	}
	
	public PaperPanel(CurrentState the_state, PanelManager the_panel_mgr,
			Paper the_paper)
	{
		super(the_state, the_panel_mgr);
		my_paper = the_paper;
		setupPanel(false);
	}
	
	//should be able to get the Paper from the CurrentState passed in,
	//this constructor is pretty much for testing only.
	public PaperPanel(final Paper the_paper, CurrentState the_state,
		PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_paper = the_paper;
		setupPanel(true);
		
	}

	@Override
	public void updatePanel() 
	{
		// TODO Auto-generated method stub
	    // set visible false
		this.setVisible(false);
	    // remove all components
		this.removeAll();
	    // call setup panel
		setupPanel(false);
	    // set visible true
		this.setVisible(true);
	    
	    //if there are components that have actionlisteners stored as a field
	    //for the class, make sure the listener is assigned in the constructor
	    //and not setuppanel, otherwise the component will have multiple
	    //action listeners.
	}

	private void setupPanel(final boolean is_test)
	{
	    //TODO: BR8/BR10: an author cannot review their own paper
	    //TODO: BR9: a user cannot be SPC on a paper they authored
		setLayout(new BorderLayout());

		add(createNorthPanel(is_test), BorderLayout.NORTH);
		add(createCenterPanel(is_test), BorderLayout.CENTER);
		add(createSouthPanel(is_test),BorderLayout.SOUTH);
	}
	
	private JPanel createNorthPanel(final boolean is_test)
	{
		//North panel requirements:
		// author name and email (done)
		// paper title (done)
		// download manuscript button (done)
		// upload manuscript button for author (done)
		// unsubmit manuscript button for author (done?)
		
		JPanel north_panel = new JPanel(new GridLayout(0,1));
		
		StringBuilder sb = new StringBuilder();
		sb.append("Author: ");
		if(!is_test)
		    sb.append(getCurrentState().getConferencesManager()
		    		.getUser(my_paper.getAuthorID()).getName());
		else
    		sb.append("Some Name");
		sb.append(" [");
		sb.append(my_paper.getAuthorID());
		sb.append("]");
		north_panel.add(new JLabel(sb.toString()));
		
		north_panel.add(new JLabel("Title: " + my_paper.getTitle()));
		
		JPanel downup = new JPanel(new GridLayout(1,0));
		if(!is_test)
		{
			JButton download = new JButton("Download Manuscript");
			//add action for download
			download.addActionListener(new DownloadFileAction(
					my_paper.getManuscript()));
			
			
			//everybody who can access this panel is authorized to download
			downup.add(download);
			
			User current_user = getCurrentState().getCurrentUser();
			if(current_user.getID().equals(my_paper.getAuthorID()))
			{//download, upload, unsubmit
				JButton upload = new JButton("Upload Manuscript");
				upload.addActionListener(new UploadManuscriptAction());
			
				JButton unsubmit = new JButton("Unsubmit Manuscript");
				unsubmit.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
					    final int result = JOptionPane.showConfirmDialog(null, 
					            "Are you sure you want to unsubmit your manuscript?");
					    
					    if(result == JOptionPane.OK_OPTION)
					    {
    						getCurrentState().getCurrentConference()
    							.removePaper(my_paper);
    						getPanelManager().popPanel();
					    }
					}
				});
				downup.add(upload);
				downup.add(unsubmit);
				
			}
		}
		else
		{
			downup.add(new JButton("Download Manuscript"));
			downup.add(new JButton("Upload Manuscript"));
			downup.add(new JButton("Unsubmit Manuscript"));
		}
		north_panel.add(downup);
		return north_panel;
	}
	
	/**
	 * @editor Kurt Hardin
	 * @param is_test whether or not this panel should be created in test mode
	 * @return the newly created panel
	 */
	private JPanel createCenterPanel(final boolean is_test)
	{
		//Center panel requirements:
		// show all reviews for PC and SPC (done)
		// show all reviews for author only if decision is made (done)
		// show only the reviewers review...for a reviewer... (done)
		// open review button (done)
		// upload review button (done?)
		
		JPanel center_panel = new JPanel(new BorderLayout());
		Set<Review> reviews = my_paper.getReviews();
		
		JPanel revs = new JPanel(new GridLayout(0,2));
		
		revs.add(new JLabel("Reviewer Name"));
		revs.add(new JLabel("")); // this col contains buttons to open reviews
		
		if(!is_test)
		{
			Role current_role = getCurrentState().getCurrentRole();
		    User current_user = getCurrentState().getCurrentUser();
		    ConferencesManager cm = getCurrentState().getConferencesManager();
		    
    		if(current_role.equals(Role.REVIEWER))
    		{ // BR12
    		    final Review r = my_paper.getReviewByID(getCurrentState().
    		            getCurrentUser().getID());
    		    
    		    revs.add(new JLabel(current_user.getName()));
    		    
    		    JPanel revbuttons = new JPanel(new GridLayout(1,0));
    		    if(r != null)
    		    {
    		        JButton dl_rev = new JButton("Download Review");
    		        if (r.getReviewDoc() == null) 
    		        {
    		        	dl_rev.setEnabled(false);
    		        } 
    		        else
    		        {
    		        	dl_rev.addActionListener(new DownloadFileAction(
    		        			r.getReviewDoc()));
    		        }
    		        revbuttons.add(dl_rev);
    		    }
    		    else
    		    {
        		    //upload review button
    		        JButton ul_rev = new JButton("Add Review");
    		        ul_rev.addActionListener(new UploadReviewAction(r));
    		        revbuttons.add(ul_rev);
    		    }
		        revs.add(revbuttons);
    		}
    		else if( (current_role.equals(Role.SUBPROGRAM_CHAIR) && 
    		        my_paper.getSubprogramChairID().equals(
    		        		current_user.getID())) ||
    		        current_role.equals(Role.PROGRAM_CHAIR))
    		{// BR20
    		    for(String reviewer_id : my_paper.getUserIDs(Role.REVIEWER))
    		    {
    		    	User user = cm.getUser(reviewer_id);
    		        revs.add(new JLabel(user.getName()));
    		        JButton b = new JButton("Download Review");
    		        Review review = my_paper.getReviewByID(reviewer_id);
    		        if (review == null) 
    		        {
    		        	b.setText("No Review Submitted");
    		        	b.setEnabled(false);
    		        } 
    		        else
    		        {
    		        	b.addActionListener(new DownloadFileAction(
    		        			review.getReviewDoc()));
    		        }
    		        revs.add(b);
    		    }
    		}
    		else if(current_role.equals(Role.AUTHOR) && 
    		        my_paper.getAuthorID().equals(current_user.getID()))
    		{ //BR18 (author can only see reviews after PC makes decision)
    		    int stat = my_paper.getAcceptanceStatus();
    		    if(stat != Paper.UNDECIDED)
    		    {
    		    	for(Review r : reviews)
	                {
//	                    revs.add(new JLabel(cm.getUser
//	                            (r.getReviewerID()).getName()));
    		    	    revs.add(new JLabel("[Reviewer name hidden]"));
	                    JButton b = new JButton("Download Review");
	    		        if (r.getReviewDoc() == null) 
	    		        {
	    		        	b.setEnabled(false);
	    		        } 
	    		        else
	    		        {
	    		        	b.addActionListener(new DownloadFileAction(
	    		        			r.getReviewDoc()));
	    		        }
	                    revs.add(b);
	                }
    		    }
    		}
		}
		else
		{
    		for(Review r : reviews)
    		{
   			    revs.add(new JLabel("Some Name"));
    
    			//open review button.
    			JButton b = new JButton("Download Review");
		        if (r.getReviewDoc() == null) 
		        {
		        	b.setEnabled(false);
		        } 
		        else
		        {
		        	b.addActionListener(
		        			new DownloadFileAction(r.getReviewDoc()));
		        }
    			revs.add(b);
    		}
		}
		JPanel cop = new JPanel(new BorderLayout());
		cop.add(revs, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(cop);
		center_panel.add(scroll,BorderLayout.CENTER);
		return center_panel;
	}
	
	/**
	 * @editor Kurt Hardin
	 * @editor Scott Sanderson
	 * @param is_test whether or not this panel should be created in test mode
	 * @return the newly created panel
	 */
	private JPanel createSouthPanel(final boolean is_test)
	{
		//South panel requirements:
		// show SPC and email (done)
		// show acceptance status for PC (done)
		// allow PC to set acceptance status (done)
		// open recommendation button (done)
		// add recommendation button  (done)
		// assign SPC button, used by PC 
		// assign reviewer button, used by SPC (done)
		
		JPanel south_panel = new JPanel(new GridLayout(0,1));

		JButton get_rec_button = new JButton("Download Recommendation");
		JButton add_rec_button = new JButton("Upload Recommendation");
		JButton assign_spc_button = new JButton("Assign Subprogram Chair");
		JButton assign_rev_button = new JButton("Assign Reviewer");
		
		Role role = getCurrentState().getCurrentRole();
		int cols = (role.equals(Role.SUBPROGRAM_CHAIR)) ? 3 : 2;
		JPanel southbuttons = new JPanel(new GridLayout(0,cols));
		
	    JPanel accept = new JPanel(new GridLayout(0,4));
	    
	    ButtonGroup accept_group = new ButtonGroup();
	    JRadioButton yes = new JRadioButton("Accepted");
	    JRadioButton dunno = new JRadioButton("Undecided");
	    JRadioButton no = new JRadioButton("Rejected");
	    accept_group.add(yes);
	    accept_group.add(dunno);
	    accept_group.add(no);
	    accept.add(new JLabel("Acceptance status:"));
	    accept.add(yes);
	    accept.add(dunno);
	    accept.add(no);
		
		//assign actions for buttons only if we aren't displaying a test
		if(!is_test)
		{
			Role current_role = getCurrentState().getCurrentRole();
		    
		    // add action to open recommendation button
	        if (my_paper.getRecommendation() == null) 
	        {
	        	get_rec_button.setEnabled(false);
	        } 
	        else
	        {
	        	get_rec_button.addActionListener(
	        			new DownloadFileAction(my_paper.getRecommendation()
	        					.getReviewDoc()));
	        }
		    
		    // add action to upload recommendation button
		    add_rec_button.addActionListener(
		    		new UploadReviewAction(my_paper.getRecommendation()));

        	// add action to assign subprogram chair button
		    assign_spc_button.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		List<String> reviewer_ids = getCurrentState()
		    				.getCurrentConference()
		    				.getUserIds(Role.REVIEWER);
		    		//BR9
		    		reviewer_ids.remove(my_paper.getAuthorID());
		    		
		    		for (int i = 0; i < reviewer_ids.size(); i++) {
		    			String reviewer_id = reviewer_ids.get(i);
		    			if (my_paper.getUserIDs(
		    					Role.SUBPROGRAM_CHAIR).contains(reviewer_id)) {
		    				reviewer_ids.remove(i);
		    			}
		    		}
		    		
		    		List<String> formatted = formatUserIDs(reviewer_ids);
		    		String id = getPopupChoice(formatted, "Choose Subprogram" +
		    				" Chair");

		    		if(id != null && id != "")
		    		{
		    			int start = id.indexOf('[') + 1;
		    			int end = id.length() - 1;
		    			id = (String) id.subSequence(start, end);
		    			List<Paper> papers = getCurrentState()
		    					.getCurrentConference().getPapers(id, 
		    					Role.SUBPROGRAM_CHAIR);
		    			if (papers.size() >= 4)
		    			{ // BR17
		    				JOptionPane.showMessageDialog(null, 
		    						"Subprogram chair cannot be assigned" +
		    						" more than 4 papers", "Error", 
		    						JOptionPane.ERROR_MESSAGE);
		    			}
		    			else
		    			{
		    				Set<Role> roles = getCurrentState().getCurrentConference().getRoles(id);
		    				if (!roles.contains(Role.SUBPROGRAM_CHAIR))
		    				{
		    					getCurrentState().getCurrentConference().authorizeUser(id, 
		    							Role.SUBPROGRAM_CHAIR);
		    				}
		    				my_paper.setSubprogramChairID(id);
		    			}
		    		}
		    	}
		    });

		    // add action to assign reviewer button
		    assign_rev_button.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		List<String> reviewer_ids = getCurrentState()
		    				.getCurrentConference()
		    				.getUserIds(Role.REVIEWER);
		    		//BR8 and BR10
		    		reviewer_ids.remove(my_paper.getAuthorID());
		    		
		    		for (int i = 0; i < reviewer_ids.size(); i++) {
		    			String reviewer_id = reviewer_ids.get(i);
		    			if (my_paper.getUserIDs(
		    					Role.REVIEWER).contains(reviewer_id)) {
		    				reviewer_ids.remove(i);
		    			}
		    		}
		    		
		    		List<String> formatted = formatUserIDs(reviewer_ids);
		    		
		    		String id = getPopupChoice(formatted, "Choose Reviewer");
		    		
		    		if(id != null && id != "")
		    		{
		    			int start = id.indexOf('[') + 1;
		    			int end = id.length() - 1;
		    			id = (String) id.subSequence(start, end);
		    			List<Paper> papers = getCurrentState().
		    					getCurrentConference().
		    					getPapers(id, Role.REVIEWER);

		    			//BR16
		    			if(papers.size() < 4)
		    			{
		    				my_paper.addReviewer(id);
		    				//TODO: add the buttons and stuff to the window
		    				//and refresh it
		    			}
		    			else
		    			{
		    				JOptionPane.showMessageDialog(null, id + 
		    						" is already working on 4 papers.", 
		    						"Error",JOptionPane.ERROR_MESSAGE);
		    			}
		    		}
		    	}
		    });
		    
		    //select the correct radio button based on current acceptance stat
		    if(my_paper.getAcceptanceStatus() == Paper.ACCEPTED)
		    	yes.setSelected(true);
		    else if(my_paper.getAcceptanceStatus() == Paper.UNDECIDED)
		    	dunno.setSelected(true);
		    else
		    	no.setSelected(true);
		    
		    if(current_role != Role.PROGRAM_CHAIR)
		    {
		    	yes.setEnabled(false);
		    	dunno.setEnabled(false);
		    	no.setEnabled(false);
		    }
		    
		    // add action listeners for acceptance radio buttons
		    yes.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{ my_paper.setAcceptanceStatus(Paper.ACCEPTED); }
		    });
		    dunno.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{ my_paper.setAcceptanceStatus(Paper.UNDECIDED); }
		    });
		    no.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{ my_paper.setAcceptanceStatus(Paper.REJECTED);}
		    });

		    //BR14
		    if(current_role.equals(Role.SUBPROGRAM_CHAIR) 
		            || current_role.equals(Role.PROGRAM_CHAIR))
		    {
		        southbuttons.add(get_rec_button);
		    }
		    
		    if(current_role.equals(Role.PROGRAM_CHAIR))
		    	southbuttons.add(assign_spc_button);
		    else if(current_role.equals(Role.SUBPROGRAM_CHAIR))
		    {
		    	southbuttons.add(add_rec_button);
		        southbuttons.add(assign_rev_button);
		    }
		    south_panel.add(accept);
		   
		}
		else // if !is_test
		{
		    southbuttons.add(get_rec_button);
		    southbuttons.add(assign_spc_button);
		    south_panel.add(accept);
		}
		south_panel.add(southbuttons);
		return south_panel;
	}
	
//	/**
//	 * Prompts the user to select a file, used for uploading.
//	 * @return The chosen file.
//	 */
//	private File chooseFile()
//	{
//		JFileChooser fc = new JFileChooser();
//		int result = fc.showOpenDialog(null);
//		
//		if(result == JFileChooser.APPROVE_OPTION)
//		{
//			File file = fc.getSelectedFile();
//
//			if(file == null)
//				JOptionPane.showMessageDialog(null, "File cannot be null",
//						"Error",JOptionPane.ERROR_MESSAGE);
//			else if(!file.exists())
//				JOptionPane.showMessageDialog(null, "File must exist",
//						"Error",JOptionPane.ERROR_MESSAGE);
//			else if(file.isDirectory())
//				JOptionPane.showMessageDialog(null, "File cannot be a " +
//						"directory", "Error",JOptionPane.ERROR_MESSAGE);
//			else
//				return file;
//		}
//		return null;
//	}
	
	/**
	 * Turns a given list of user IDs and formats them into "Real name [email]"
	 *  format.
	 * @param the_ids The IDs (emails) to format.
	 * @return The formatted IDs.
	 */
	private List<String> formatUserIDs(final List<String> the_ids)
	{
		ArrayList<String> formatted = new ArrayList<String>(the_ids.size());
		StringBuilder sb;
		for(String s : the_ids)
		{
			sb = new StringBuilder();
			sb.append(getCurrentState().getConferencesManager()
					.getUser(s).getName());
			sb.append(" [");
			sb.append(s);
			sb.append("]");
			formatted.add(sb.toString());
		}
		return formatted;
	}
	
	/**
	 * Uses a popup to prompt the user to pick from a list of choices.
	 * @param the_choices The choices the user can pick from
	 * @param the_message The message to prompt the user with.
	 * @return The chosen option.
	 */
	private String getPopupChoice(final List<String> the_choices, final
			String the_message)
	{
		String[] choices = new String[the_choices.size()];
		
		int i = 0;
		for(String s : the_choices)
			choices[i++] = s;
			
		String result = (String) JOptionPane.showInputDialog(null, the_message,
				"Select",JOptionPane.PLAIN_MESSAGE, null, choices,choices[0]);
		
		return result;
	}
	
	private class DownloadFileAction extends AbstractAction
	{
		final File my_file;
		
		public DownloadFileAction(final File the_review)
		{
			my_file = the_review;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc = new JFileChooser();
			fc.setSelectedFile(new File(my_file.getName()));
			
			int result = fc.showSaveDialog(null);
						
			if(result == JFileChooser.APPROVE_OPTION 
					&& fc.getSelectedFile() != null)
			{
				FileHelper.copyFile(my_file, fc.getSelectedFile());;
			}
		}
	}
	
	private class UploadManuscriptAction extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc = new JFileChooser();
			int result = fc.showOpenDialog(null);
			
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File up = fc.getSelectedFile();
				
				if(up == null)
					JOptionPane.showMessageDialog(null, "File cannot be null",
							"Error",JOptionPane.ERROR_MESSAGE);
				else if(!up.exists())
					JOptionPane.showMessageDialog(null, "File must exist",
							"Error",JOptionPane.ERROR_MESSAGE);
				else if(up.isDirectory())
					JOptionPane.showMessageDialog(null, "File cannot be a " +
							"directory", "Error",JOptionPane.ERROR_MESSAGE);
				else
				{
						my_paper.setManuscript(up);
						updatePanel(); //TODO: verify this works
				}
			}
		}
	}
	
	private class UploadReviewAction extends AbstractAction
	{
		final Review my_review;
		
		public UploadReviewAction(final Review the_review)
		{
			my_review = the_review;
		}
			
		/**
		 * @editor Kurt Hardin
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc = new JFileChooser();
			if (my_review != null) {
				fc.setSelectedFile(
						new File(my_review.getReviewDoc().getName()));
			}
			
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				File selected_file = fc.getSelectedFile();

				if(selected_file == null)
					JOptionPane.showMessageDialog(null, "File cannot be null",
							"Error",JOptionPane.ERROR_MESSAGE);
				else if(!selected_file.exists())
					JOptionPane.showMessageDialog(null, "File must exist",
							"Error",JOptionPane.ERROR_MESSAGE);
				else if(selected_file.isDirectory())
					JOptionPane.showMessageDialog(null, "File cannot be a " +
							"directory", "Error",JOptionPane.ERROR_MESSAGE);
				else
				{
					if (my_review == null) 
					{
						
						if (getCurrentState().getCurrentRole().equals(
								Role.REVIEWER)) 
						{
							Review new_review = new Review(
									my_paper.getDirectory(),
									getCurrentState().getCurrentUser().getID(),
									selected_file);
							my_paper.addReview(new_review);
						} 
						else if (getCurrentState().getCurrentRole().equals(
								Role.SUBPROGRAM_CHAIR)) 
						{
							Review new_review = new Recommendation(
									my_paper.getDirectory(),
									getCurrentState().getCurrentUser().getID(),
									selected_file);
							my_paper.setRecommendation(new_review);
						}	
					}
					else 
						my_review.setReviewDoc(selected_file);	
				}
				updatePanel();
			}	
		}
	}
}
