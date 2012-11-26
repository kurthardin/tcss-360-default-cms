package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Review;

public class ReviewPanel extends AbstractConferencesPanel 
{
	
	/////////////
	// FIELDS
	/////////////
	
	//likely for testing only
	private Review my_review;
	
	//likely for testing only
	private final String my_manuscript_title;

	public ReviewPanel(CurrentState the_state, PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		// TODO Auto-generated constructor stub
		
		my_manuscript_title = "Manuscript Title Here";
		my_review = null;
	}
	
	//should be able to get the manuscript title and the review from
	// the CurrentState passed in. This is pretty much for testing only
	public ReviewPanel(final String the_manuscript_title, 
		Review the_review, CurrentState the_state, 
		PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_manuscript_title = the_manuscript_title;
		my_review = the_review;
		setupPanel();
	}

	@Override
	public void updatePanel() 
	{
		// TODO Auto-generated method stub

	}
	
	private void setupPanel()
	{
		//just need to show: back button, reviewer name, 
		// what they're reviewing, summary rating, download review
		// doc button, upload review doc button
		
		//TODO: add permission handling
		
		setLayout(new BorderLayout());
		
		JPanel center_panel = new JPanel(new GridLayout(0,1));
		
		center_panel.add(new JLabel("Reviewer: " + 
				my_review.getReviewerID()));
		
		center_panel.add(new JLabel("Reviewing: " + my_manuscript_title));
		
		String summary = "Summary Rating: ";
		if(my_review.my_summary_rating == Review.NO_RATING)
			summary += "N/A";
		else
			summary += my_review.getSummaryRating();
		center_panel.add(new JLabel(summary));
		
		JButton download_button = new JButton("Download Review Document");
		download_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent the_event)
			{
				System.out.println("TODO: make Download Review Doc button" +
						" do something");
			}
		});
		
		JButton upload_button = new JButton("Upload Review Document");
		upload_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent the_event)
			{
				System.out.println("TODO: make Upload Review Doc button" +
						" do something");
			}
		});
		
		JPanel downup = new JPanel(new GridLayout(0,2));
		downup.add(download_button);
		downup.add(upload_button);
		
		center_panel.add(downup);
		
		add(center_panel, BorderLayout.CENTER);
	}

	//test main
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Review Panel");
		
		File doc = new File("README.txt");
		File dir = new File(".");
		
		Review rev = new Review(dir,"areviewerID@www.com", doc);
		
		frame.add(new ReviewPanel("A Manuscript Title", rev, null, null));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
