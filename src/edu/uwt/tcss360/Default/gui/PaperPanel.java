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

	}

	private void setupPanel(final boolean is_test)
	{
	    //TODO: modify display contents based on current user
	    
		//new GridLayout(rows, cols);
		setLayout(new BorderLayout());
		ConferencesManager cm = null;
		if(!is_test)
		    cm = getCurrentState().getConferencesManager();
		
		//information for north: back button, author, title, download button
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
		
		JButton dl_button = new JButton("Download Manuscript");
		dl_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent the_event)
			{
				System.out.println("TODO: Add an action for the Download" +
						" Manuscript button...");
			}
		});
		JButton ul_button = new JButton("Upload Manuscript");
		ul_button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent the_event)
            {
                System.out.println("TODO: Add an action for the Upload" +
                        " Manuscript button...");
            }
        });
		JPanel downup = new JPanel(new GridLayout(0,2));
		downup.add(dl_button);
		if(is_test)
		    downup.add(ul_button);
		else if(getCurrentState().getCurrentUser().getID() 
		        == my_paper.getAuthorID())
		    downup.add(ul_button);
		north_panel.add(downup);
		
		
		
		//info for middle: reviewers, their scores, buttons to their 
		// review docs which will likely all be in a grid.
		JPanel center_panel = new JPanel(new BorderLayout());
		JPanel meta_review = new JPanel(new GridLayout(0,1));
		
		//Set<Review> reviews = my_paper.getReviews();
		int review_count = my_paper.my_reviews.size();//reviews.size();
		int reviews_complete = 0;
		for(Review r : my_paper.my_reviews)
		{
		    if(r.my_summary_rating != Review.NO_RATING)
		        reviews_complete += 1;
		}

		sb = new StringBuilder();
		sb.append("Reviews complete: ");
		sb.append(reviews_complete);
		sb.append(" of ");
		sb.append(review_count);
		meta_review.add(new JLabel(sb.toString()));
		
		sb = new StringBuilder();
		sb.append("Average Score: ");
		float avg = 0;
		for(Review r : my_paper.my_reviews)
		{
			if(r.my_summary_rating != Review.NO_RATING)
				avg += r.my_summary_rating;
		}
		avg = avg / (float)reviews_complete;
		sb.append(String.format("%.1f", avg));
		meta_review.add(new JLabel(sb.toString()));
		
		center_panel.add(meta_review, BorderLayout.NORTH);
		
		JPanel revs = new JPanel(new GridLayout(0,4));
		revs.add(new JLabel("Reviewer Name"));
		revs.add(new JLabel("Reviewer email"));
		revs.add(new JLabel("Score"));
		revs.add(new JLabel("")); // this col contains buttons to open reviews
		
		for(Review r : my_paper.my_reviews)
		{
			//real name
			if(!is_test)
			    revs.add(new JLabel(cm.getUser(r.getReviewerID()).getName()));
			else
			    revs.add(new JLabel("Some Name"));
			//email address
			revs.add(new JLabel(r.getReviewerID()));
			//score
			if(r.my_summary_rating == Review.NO_RATING)
				revs.add(new JLabel("N/A"));
			else
				revs.add(new JLabel(String.valueOf(r.my_summary_rating)));
			//open review button.
			JButton b = new JButton("Open Review");
			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent the_event)
				{
					//TODO: make this open a review panel...
					System.out.println("TODO: add action for Open Review" +
							"button");
				}
			});
			revs.add(b);
		}
		center_panel.add(revs, BorderLayout.CENTER);
		
		
		
		//info for south:
		//subprogram chair (recommender), recommendation score, download 
		//button for doc
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
		rec_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent the_event)
			{
				//TODO: make this open a recommendation panel...
				System.out.println("TODO: add action for Open " +
						"Recommendation button");
			}
		});
		south_panel.add(rec_button);
		
		
		add(north_panel, BorderLayout.NORTH);
		add(center_panel, BorderLayout.CENTER);
		add(south_panel, BorderLayout.SOUTH);
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
		Review rec = new Review(papers_dir, "recid",doc);
		paper.setRecommendation(rec);
		
		frame.add(new PaperPanel(paper,null,null));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
