/**
 * RecommendationPanel.java
 * Author: Travis Lewis
 * Date: 25 Nov 2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Review;

/**
 * A JPanel that displays information and allows actions relevant to the
 * current Review (recommendation) object.
 * @author Travis Lewis
 * @version 25 Nov 2012
 */
@SuppressWarnings("serial")
public class RecommendationPanel extends AbstractConferencesPanel 
{
	/////////////
	// FIELDS
	/////////////
	
	//likely for testing only
	private final String my_title;
	
	//likely for testing only
	private Review my_recommendation;
	
	public RecommendationPanel(CurrentState the_state,
			PanelManager the_panel_mgr) {
		super(the_state, the_panel_mgr);
		my_title = "A Manuscript Title";
		my_recommendation = null;
		// TODO Auto-generated constructor stub
	}
	
	public RecommendationPanel(final String the_manuscript_title,
		Review the_recommendation, CurrentState the_state,
		PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_title = the_manuscript_title;
		my_recommendation = the_recommendation;
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
		
		//TODO: add permission handling?
		
		setLayout(new BorderLayout());
		
		JPanel center_panel = new JPanel(new GridLayout(0,1));
		
		// probably should use the "First Last [email]" format here
		center_panel.add(new JLabel("Subprogram Chair: " + 
				my_recommendation.getReviewerID()));
		
		center_panel.add(new JLabel("Recommending: " + my_title));
		
		String summary = "Summary Recommendation: ";
		if(my_recommendation.my_summary_rating == Review.NO_RATING)
			summary += "N/A";
		else
			summary += my_recommendation.getSummaryRating();
		center_panel.add(new JLabel(summary));
		
		JButton download_button = new JButton("Download Recommendation" +
				" Document");
		download_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent the_event)
			{
				System.out.println("TODO: make Download Recommendation " +
						"Doc button do something");
			}
		});
		
		JButton upload_button = new JButton("Upload Recommendation" +
				" Document");
		upload_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent the_event)
			{
				System.out.println("TODO: make Upload Recommendation" +
						" Doc button do something");
			}
		});
		
		JPanel downup = new JPanel(new GridLayout(0,2));
		downup.add(download_button);
		downup.add(upload_button);
		
		center_panel.add(downup);
		
		add(center_panel, BorderLayout.CENTER);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Recommendation Panel");
		
		File doc = new File("README.txt");
		File dir = new File(".");
		
		Review rev = new Review(dir,"anSPC_rID@www.com", doc,4);
		
		frame.add(new RecommendationPanel("A Manuscript Title", rev,
				null, null));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
