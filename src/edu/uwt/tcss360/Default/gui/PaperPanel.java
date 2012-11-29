/**
 * PaperPanel.java
 * Author: Travis Lewis
 * Date: 25 Nov 2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
    
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.Review;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * @author Travis Lewis
 * @version 25 Nov 2012
 * Creates a PaperPanel, which displays information relevant to the
 * current Paper object.
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
	    // remove all components
	    // call setup panel
	    // set visible true
	    
	    //if there are components that have actionlisteners stored as a field
	    //for the class, make sure the listener is assigned in the constructor
	    //and not setuppanel, otherwise the component will have multiple
	    //action listeners.
	}

	private void setupPanel(final boolean is_test)
	{
	    //TODO: US10 author can upload/unsubmit their paper.
	    //TODO: BR18: author can only access reviews after PC makes a decision
	    //TODO: BR8/BR10: an author cannot review their own paper
	    //TODO: BR9: a user cannot be SPC on a paper they authored
	    //TODO: US2: allow PC to make a yes/no acceptence decision on a paper
	    //TODO: take out the 1-5 ratings
	    //TODO: need to be able to assign multiple reviewers, but no maximum
	    // amount of reviewers.
	    //TODO: RECOMMENDATION!!!
	    
	    Role current_role = getCurrentState().getCurrentRole();
	    User current_user = getCurrentState().getCurrentUser();
	    
		//new GridLayout(rows, cols);
		setLayout(new BorderLayout());
		ConferencesManager cm = null;
		if(!is_test)
		    cm = getCurrentState().getConferencesManager();
		
		//information for north: author, title, download button
		JPanel north_panel = new JPanel(new GridLayout(0,1));
		
		StringBuilder sb = new StringBuilder();
		sb.append("Author: ");
		if(!is_test)
		    sb.append(cm.getUser(my_paper.getAuthorID()).getName());
		else
    		sb.append("Some Name");
		sb.append(" [");
		sb.append(my_paper.getAuthorID());
		sb.append("]");
		north_panel.add(new JLabel(sb.toString()));
		
		north_panel.add(new JLabel("Title: " + my_paper.getTitle()));
		
		//under what conditions can a user download a manuscript?
		//
		
		JButton dl_button = new JButton("Download Manuscript");
		//TODO: add action for download manuscript button
		JButton ul_button = new JButton("Upload Manuscript");
		//TODO: add action for upload manuscript button

		JPanel downup = new JPanel(new GridLayout(0,2));
		downup.add(dl_button);
		if(is_test)
		    downup.add(ul_button);
		else if(getCurrentState().getCurrentUser().getID() 
		        == my_paper.getAuthorID())
		    downup.add(ul_button);
		north_panel.add(downup);
		
		
		
		//info for middle: reviewers, buttons to their review docs (grid)
		JPanel center_panel = new JPanel(new BorderLayout());

		JPanel revs = new JPanel(new GridLayout(5,2));
		revs.add(new JLabel("Reviewer Name"));
		revs.add(new JLabel("")); // this col contains buttons to open reviews
		
		if(!is_test)
		{
    		if(current_role == Role.REVIEWER)
    		{ // BR12
    		    Review r = getReviewByID(getCurrentState().
    		            getCurrentUser().getID());
    		    
    		    revs.add(new JLabel(current_user.getName()));
    		    
    		    JButton my_rev_button;
    		    if(r != null)
    		    {
    		        my_rev_button = new JButton("Open Review");
    		        //TODO: add open review action to the button
    		    }
    		    else
    		    {
    		        my_rev_button = new JButton("Add Review");
    		        //TODO: add "add review" action to the button
    		    }
    		    
    		    revs.add(my_rev_button);
    		}
    		else if( (current_role == Role.SUBPROGRAM_CHAIR && 
    		        my_paper.getSubprogramChairID() == current_user.getID()) ||
    		        current_role == Role.PROGRAM_CHAIR)
    		{// BR20
    		    for(Review r : my_paper.my_reviews)
    		    {
    		        revs.add(new JLabel(cm.getUser
    		                (r.getReviewerID()).getName()));
    		        JButton b = new JButton("Open Review");
    		        //TODO: add open review action to button
    		        revs.add(b);
    		    }
    		}
    		else if(current_role == Role.AUTHOR && 
    		        my_paper.getAuthorID().equals(current_user.getID()))
    		{ //BR18 (author can only see reviews after PC makes decision)
    		    int stat = my_paper.getAcceptanceStatus();
    		    if(stat != Paper.UNDECIDED)
    		    {
    	              for(Review r : my_paper.my_reviews)
    	                {
    	                    revs.add(new JLabel(cm.getUser
    	                            (r.getReviewerID()).getName()));
    	                    JButton b = new JButton("Open Review");
    	                    //TODO: add open review action to button
    	                    revs.add(b);
    	                }
    		    }
    		}
		}
		else
		{
    		for(Review r : my_paper.my_reviews)
    		{
    			//real name
    			if(!is_test)
    			    revs.add(new JLabel(cm.getUser(r.getReviewerID()).getName()));
    			else
    			    revs.add(new JLabel("Some Name"));
    
    			//open review button.
    			JButton b = new JButton("Open Review");
    			//TODO: add action to open review buttons
    			revs.add(b);
    		}
		}
		center_panel.add(revs, BorderLayout.CENTER);
		
		
		
		//info for south:
		//subprogram chair (recommender), recommendation score, download 
		//button for doc, assign subprogram chair (if PC)
		JPanel south_panel = new JPanel(new GridLayout(0,1));
		sb = new StringBuilder();
		sb.append("Subprogram Chair: ");
		if(!is_test)
		    sb.append(cm.getUser(my_paper.getSubprogramChairID()).getName());
		else
		    sb.append("Some Name");
		sb.append(" [");
		sb.append(my_paper.getSubprogramChairID());
		sb.append("]");
		south_panel.add(new JLabel(sb.toString()));
		
		sb = new StringBuilder();
		sb.append("Recommendation: ");
		if(my_paper.getRecommendation().my_summary_rating == Review.NO_RATING)
			sb.append("N/A");
		else
			sb.append(my_paper.getRecommendation().my_summary_rating);
		south_panel.add(new JLabel(sb.toString()));
		
		JButton rec_button = new JButton("Open Recommendation");
		JButton assign_spc_button = new JButton("Assign Subprogram Chair");
		JButton assign_rev_button = new JButton("Assign Reviewer");
		JPanel southbuttons = new JPanel(new GridLayout(0,2));
		
		//assign actions for buttons only if we aren't displaying a test
		if(!is_test)
		{
		    //TODO: add action to open recommendation button

        	//TODO: add action to assign program chair button

		    //TODO: add action to assign reviewer button

		    
		    //TODO: probably need to change what shows depending on the date
		    
		    //business rule 14
		    if(current_role == Role.SUBPROGRAM_CHAIR 
		            || current_role == Role.PROGRAM_CHAIR)
		        southbuttons.add(rec_button);
		    
		    if(current_role == Role.PROGRAM_CHAIR)
		        southbuttons.add(assign_spc_button);
		    else if(current_role == Role.SUBPROGRAM_CHAIR)
		        southbuttons.add(assign_rev_button);
		}
		else
		{
		    southbuttons.add(rec_button);
		    southbuttons.add(assign_spc_button);
		}
		south_panel.add(southbuttons);
		
		
		add(north_panel, BorderLayout.NORTH);
		add(center_panel, BorderLayout.CENTER);
		add(south_panel, BorderLayout.SOUTH);
	}
		
	/**
	 * Gets a given user ID's Review object if it exists.
	 * @param an_ID The ID of the user who's review you want.
	 * @return The given user's Review if it exists, <code>null</code> if
	 * it does not exist.
	 */
	private Review getReviewByID(final String an_ID)
	{
	    for(Review r : my_paper.my_reviews)
	    {
	        if(r.getReviewerID().equals(an_ID));
	            return r;
	    }
	    return null;
	}
	
	//test main
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Paper Panel");
		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
				ConferencesFrame.HEIGHT));
		
		
		//setup a paper object
		File doc = new File("README.txt");
		File papers_dir = new File(".");
		Paper paper = new Paper("somebody@www.com", "A Manuscript Title",
				doc, papers_dir);
		
		//add some reviews
		Review r1 = new Review(papers_dir, "revid1@www.com", doc, 3);
		Review r2 = new Review(papers_dir, "revid2@www.com", doc);
		Review r3 = new Review(papers_dir, "revid3@www.com", doc, 1);
		Review r4 = new Review(papers_dir, "revid4@wmw.com", doc, 4);
		
		paper.addReview(r1);
		paper.addReview(r2);
		paper.addReview(r3);
		paper.addReview(r4);
		
		paper.assignSubprogramChair("spc@blah.com");
		Review rec = new Review(papers_dir, "recid@www.com",doc);
		paper.setRecommendation(rec);
		
		frame.add(new PaperPanel(paper,null,null));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
