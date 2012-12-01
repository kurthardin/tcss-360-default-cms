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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
    
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.Review;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;

/**
 * @author Travis Lewis
 * @version 30 Nov 2012
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
						//hopefully this is all it takes...
						getCurrentState().getCurrentConference()
							.removePaper(my_paper);
						getPanelManager().popPanel();
					}
				});
				downup.add(upload);
				downup.add(unsubmit);
				
				//TODO: add actions for author buttons
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
		
		JPanel revs = new JPanel(new GridLayout(5,2));
		
		revs.add(new JLabel("Reviewer Name"));
		revs.add(new JLabel("")); // this col contains buttons to open reviews
		
		if(!is_test)
		{
			Role current_role = getCurrentState().getCurrentRole();
		    User current_user = getCurrentState().getCurrentUser();
		    ConferencesManager cm = getCurrentState().getConferencesManager();
		    
    		if(current_role == Role.REVIEWER)
    		{ // BR12
    		    Review r = my_paper.getReviewByID(getCurrentState().
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

    		    //upload review button
		        JButton ul_rev = new JButton("Add Review");
		        ul_rev.addActionListener(new ActionListener()
		        {
		        	@Override
		        	public void actionPerformed(ActionEvent e)
		        	{
		        		File up = chooseFile();
		        		if(up != null)
		        		{
		        			User user = getCurrentState().getCurrentUser();
		        			Review rev = new Review(my_paper.
		        					getDirectory(), user.getID(), up);
		        			my_paper.addReview(rev);
		        			updatePanel();
		        		}
		        	}
		        });
		        revbuttons.add(ul_rev);
		        revs.add(revbuttons);
    		}
    		else if( (current_role == Role.SUBPROGRAM_CHAIR && 
    		        my_paper.getSubprogramChairID() == current_user.getID()) ||
    		        current_role == Role.PROGRAM_CHAIR)
    		{// BR20
    		    for(Review r : reviews)
    		    {
    		        revs.add(new JLabel(cm.getUser
    		                (r.getReviewerID()).getName()));
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
    		else if(current_role == Role.AUTHOR && 
    		        my_paper.getAuthorID().equals(current_user.getID()))
    		{ //BR18 (author can only see reviews after PC makes decision)
    		    int stat = my_paper.getAcceptanceStatus();
    		    if(stat != Paper.UNDECIDED)
    		    {
    		    	for(Review r : reviews)
	                {
	                    revs.add(new JLabel(cm.getUser
	                            (r.getReviewerID()).getName()));
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
		int cols = (role == Role.SUBPROGRAM_CHAIR) ? 3 : 2;
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
		    		List<String> formatted = formatUserIDs(getCurrentState()
		    				.getCurrentConference()
		    				.getUserIds(Role.SUBPROGRAM_CHAIR));
		    		
		    		String id = getPopupChoice(formatted, "Choose Subprogram" +
		    				" Chair");

		    		if(id != null && id != "")
		    		{
		    			List<Paper> papers = getCurrentState()
		    					.getCurrentConference().getPapers(id, 
		    					Role.SUBPROGRAM_CHAIR);
		    			//BR9
		    			if(id == my_paper.getAuthorID())
		    			{
		    				JOptionPane.showMessageDialog(null, 
		    						"Subprogram chair cannot be author", 
		    						"Error",JOptionPane.ERROR_MESSAGE);
		    			}
		    			else if (papers.size() >= 4)
		    			{ // BR17
		    				JOptionPane.showMessageDialog(null, 
		    						"Subprogram chair cannot be assigned more" 
		    						+ "than 4 papers", "Error", 
		    						JOptionPane.ERROR_MESSAGE);
		    			}
		    			else
		    				my_paper.setSubprogramChairID(id);
		    		}
		    	}
		    });

		    // add action to assign reviewer button
		    assign_rev_button.addActionListener(new ActionListener()
		    {
		    	@Override
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		List<String> formatted = formatUserIDs(getCurrentState()
		    				.getCurrentConference()
		    				.getUserIds(Role.REVIEWER));
		    		
		    		String id = getPopupChoice(formatted, "Choose Reviewer");
		    		
		    		if(id != null && id != "")
		    		{
		    			//BR8 and BR10
		    			if(id == my_paper.getAuthorID())
		    			{
		    				JOptionPane.showMessageDialog(null, 
		    						"Authors cannot review their own papers", 
		    						"Error",JOptionPane.ERROR_MESSAGE);
		    			}
		    			else
			    			{
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
			    						" is already workong on 4 papers.", 
			    						"Error",JOptionPane.ERROR_MESSAGE);
			    			}
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
		    if(current_role == Role.SUBPROGRAM_CHAIR 
		            || current_role == Role.PROGRAM_CHAIR)
		    {
		        southbuttons.add(get_rec_button);
		    }
		    
		    if(current_role == Role.PROGRAM_CHAIR)
		    	southbuttons.add(assign_spc_button);
		    else if(current_role == Role.SUBPROGRAM_CHAIR)
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
	
	/**
	 * Prompts the user to select a file, used for uploading.
	 * @return The chosen file.
	 */
	private File chooseFile()
	{
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();

			if(file == null)
				JOptionPane.showMessageDialog(null, "File cannot be null",
						"Error",JOptionPane.ERROR_MESSAGE);
			else if(!file.exists())
				JOptionPane.showMessageDialog(null, "File must exist",
						"Error",JOptionPane.ERROR_MESSAGE);
			else if(file.isDirectory())
				JOptionPane.showMessageDialog(null, "File cannot be a " +
						"directory", "Error",JOptionPane.ERROR_MESSAGE);
			else
				return file;
		}
		return null;
	}
	
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
			//fc.setSelectedFile(new File(FileHelper.getLeafString(my_file)));
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
					try
					{
						my_paper.getManuscript().delete();
						File newfile = FileHelper.createFile(
								my_paper.getDirectory(),
								up.getName());
						
						FileHelper.copyFile(up, my_paper.getDirectory());
						my_paper.setManuscript(newfile);
					}
					catch(SecurityException e2)
					{
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
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
				
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc = new JFileChooser();
//			fc.setSelectedFile(new File(FileHelper.getLeafString
//					(my_review.getReviewDoc())));
			fc.setSelectedFile(new File(my_review.getReviewDoc().getName()));
			
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
					try
					{
						my_review.getReviewDoc().delete();
						
//						File newfile = FileHelper.createFile(
//								my_review.getDirectory(), 
//								FileHelper.getLeafString(up));
						File newfile = FileHelper.createFile(
								my_review.getDirectory(), up.getName());
						
						FileHelper.copyFile(up, my_review.getDirectory());

						FileHelper.copyFile(up, newfile);
					}
					catch(SecurityException e2)
					{
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
					
				}
			}
		}
	}
	
	//test main
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("Paper Panel");
		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH,
				ConferencesFrame.HEIGHT));
		
		
		testPanel(frame);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
//		JOptionPane.showMessageDialog(null, "File must exist", "Error",
//				JOptionPane.ERROR_MESSAGE);
	}
	
	
	private static void testPanel(JFrame frame)
	{
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
	}
	
	//TODO: delete this when done testing
//	private void printFileStats(final File the_file)
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append("path: ");
//		sb.append(the_file.getAbsolutePath());
//		sb.append("\nexists: ");
//		sb.append(the_file.exists());
//		sb.append("\nis directory: ");
//		sb.append(the_file.isDirectory());
//		sb.append("\nis file: ");
//		sb.append(the_file.isFile());
//		System.out.println(sb.toString());
//	}
	
}
