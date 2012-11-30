/**
 * Paper.java
 * Author: Travis Lewis
 * Date: 8 Nov 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;
import edu.uwt.tcss360.Default.util.xml.parsers.InfoHandler;

/**
 * Holds information about a manuscript along with reviews of it, etc.
 * 
 * @author Travis Lewis
 * @editor Kurt Hardin
 * @version 8 Nov 2012
 */
public class Paper 
{
	/////////////
	// CONSTANTS
	/////////////
	
	// XML attribute names
	public static final String XML_ATTR_MY_MANUSCRIPT_DOC = 
			"my_manuscript_doc";
	public static final String XML_ATTR_MY_ACCEPTANCE_STATUS = 
			"my_acceptance_status";
	public static final String XML_ATTR_MY_AUTHOR_ID = 
			"my_author_id";
	public static final String XML_ATTR_MY_SUBPROGRAM_CHAIR_ID = 
			"my_subprogram_chair_id";
	public static final String XML_ATTR_MY_MANUSCRIPT_TITLE = 
			"my_manuscript_title";
	
    //values for Program Chair's acceptance choices
	public static final int REJECTED = -1;
	public static final int UNDECIDED = 0;
	public static final int ACCEPTED = 1;
	
	/** The value of a field that hasn't been set yet. */
	private static final String NOT_AVAILIBLE = "n/a";
	
	
	/////////////
	// FIELDS
	/////////////
	
	/** Set of review objects. */
    private Set<Review> my_reviews;
    // that ^ might as well be public if we're just going to have getReviews()
    // unless we're returning a read-only copy of the list, but that would
    // make it kind of hard to modify the Reviews...
	
	/** The location on disk of the directory belonging to the paper. */
	private final File my_directory;
	
	/** The actual document (docx, pdf, etc), of the manuscript. */
	private File my_manuscript_doc;
	
	/** The yes/no value for if the PC accepted the paper */
	private int my_acceptance_status;
	
	/** User ID of the manuscript author. */
	private String my_author_id;
	
	/** User ID of the subprogram chair for the paper. */
	private String my_subprogram_chair_id;
	
	/** The recommendation of the subprogram chair. */
	private Review my_recommendation;
	
	private Set<String> my_reviewer_ids;
	
	private String my_manuscript_title;
	
	
	
	/////////////
	// CONSTRUCTORS
	/////////////
	/**
	 * Constructs a Paper object from a previously created and saved Paper
	 * object's data file.
	 * @author Kurt Hardin
	 * @param the_paper_directory The directory containing the data file for 
	 * the paper and the review/recommendation folders.
	 */
	public Paper(final File the_paper_directory) 
	{
	    if(the_paper_directory == null)
	        throw new IllegalArgumentException("The conference directory " +
	                "cannot be null");
	    
		my_directory = the_paper_directory;
		initFields();
		 
		InputSource info = FileHelper.getInputSource(my_directory, 
				FileHelper.DATA_FILE_NAME);
		
		if(info != null)
		{
			try 
			{
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();

				InfoHandler handler = new InfoHandler() 
				{
					@Override
					public void handleFieldsAttributes(Attributes attr) 
					{
						String acceptStatusStr = attr.getValue(
								XML_ATTR_MY_ACCEPTANCE_STATUS);
						my_acceptance_status = (acceptStatusStr == null) ? 
								UNDECIDED : 
								Integer.valueOf(acceptStatusStr);
						
						my_author_id = attr.getValue(XML_ATTR_MY_AUTHOR_ID);
						my_subprogram_chair_id = attr.getValue(
								XML_ATTR_MY_SUBPROGRAM_CHAIR_ID);
						my_manuscript_title = attr.getValue(
								XML_ATTR_MY_MANUSCRIPT_TITLE);
						
						String doc_name = attr.getValue(
								XML_ATTR_MY_MANUSCRIPT_DOC);
						my_manuscript_doc = new File(my_directory, doc_name);
					}
				};

				saxParser.parse(info, handler);

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			my_recommendation = new Review(
					FileHelper.getRecommendationDirectory(
							my_directory));
			
			String[] review_dir_names = my_directory.list(
					new FilenameFilter() 
					{
						@Override
						public boolean accept(File dir, String name) 
						{
							return name.startsWith(
									FileHelper.REVIEW_DIRECTORY_PREFIX) &&
									((new File(dir, name)).isDirectory());
						}
					});
			
			my_reviewer_ids = new HashSet<String>(review_dir_names.length);
			
			for (String review_dir_name : review_dir_names) 
			{
				File review_dir = new File(my_directory, review_dir_name);
				
				Review review = new Review(review_dir);
				my_reviews.add(review);
				my_reviewer_ids.add(review.getReviewerID());
			}

			// TODO Write unit tests for Paper(File)
			
		}
	}
	
	/**
	 * Constructs a new Paper object from the given information.
	 * @param the_author_id The user ID of the author.
	 * @param the_manuscript_doc The document file (.pdf, .docx, etc) of the
	 * manuscript.
	 * @param the_papers_directory The directory that contains all the papers
	 * for the conference. The directory needs to be created or has to exist 
	 * before calling this constructor.
	 */
	public Paper(final String the_author_id, String the_title, 
			final File the_manuscript_doc,
			final File the_papers_directory) 
	{
	    if(the_author_id == null) {
	        throw new IllegalArgumentException("Author ID cannot be null");
	    }
	    if (the_title == null) {
	    	throw new IllegalArgumentException("Title cannot be null");
	    }
	    if(the_manuscript_doc == null) {
	        throw new IllegalArgumentException("Manuscript doc cannot " +
	        		"be null");
	    }
	    if(!the_manuscript_doc.exists()) {
	        throw new IllegalArgumentException("Manuscript doc must exist");
	    }
	    if(the_papers_directory == null) {
	        throw new IllegalArgumentException("The Paper directory cannot " +
	        		"be null");
	    }
	    if(!the_papers_directory.exists()) {
	        throw new IllegalArgumentException("The Paper directory " +
	        		"must exist");
	    }
		
		initFields();
		my_author_id = the_author_id;
	    
	    my_manuscript_title = the_title;
	    
	    String directory_name = FileHelper.formatFilename(
	    		my_manuscript_title + "_" + my_author_id);
		my_directory = FileHelper.getDirectory(the_papers_directory, 
				directory_name);
		
		copyPaperDoc(my_directory, the_manuscript_doc);		
	}
	
	
	/////////////
	// METHODS
	/////////////
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	public void writeData() 
	{
		File output_file = new File(my_directory, 
					FileHelper.DATA_FILE_NAME);
		new InfoDocument(output_file)
		.setField(XML_ATTR_MY_MANUSCRIPT_TITLE, my_manuscript_title)
		.setField(XML_ATTR_MY_MANUSCRIPT_DOC, my_manuscript_doc)
		.setField(XML_ATTR_MY_ACCEPTANCE_STATUS, my_acceptance_status)
		.setField(XML_ATTR_MY_AUTHOR_ID, my_author_id)
		.setField(XML_ATTR_MY_SUBPROGRAM_CHAIR_ID, my_subprogram_chair_id)
		.write();
		
		if (my_recommendation != null) 
		{
			my_recommendation.writeData();
		}
		
		for (Review review : my_reviews) 
		{
			review.writeData();
		}
	}
	
	public File getDirectory() 
	{
		return my_directory.getAbsoluteFile();
	}
	
	public Review getRecommendation()
	{
		return my_recommendation;
	}
	
	public void setRecommendation(Review the_recommendation)
	{
		//permission checking should be done by the GUI
		my_recommendation = the_recommendation;
	}
	
	/**
	 * Gets the role that the given user id has with this paper.
	 * @param a_user_id The user to find the role of.
	 * @return Role.USER if the given user isn't involved with the paper.
	 */
	public Role getRole(final String a_user_id) 
	{
	    if(a_user_id == null)
	        throw new IllegalArgumentException("User ID cannot be null");
	    
		if(a_user_id.equals(my_author_id))
			return Role.AUTHOR;
		
		if(my_subprogram_chair_id.equals(a_user_id))
			return Role.SUBPROGRAM_CHAIR;
		
		for(String s : my_reviewer_ids) 
			if(s.equals(a_user_id))
				return Role.REVIEWER;
		
		return Role.USER;
	}
	
	/**
	 * Gets a List of strings containing the user IDs of the given role for
	 * the Paper.
	 * @param a_user_role The role of the users to put in the list.
	 * @return A List of users with the given role associated with the Paper.
	 */
	public List<String> getUserIDs(final Role a_user_role) 
	{
	    if(a_user_role == null)
	        throw new IllegalArgumentException("Role cannot be null");
	    
		List<String> ids = null;// = new ArrayList<String>();
		
		if(a_user_role == Role.REVIEWER)
		    ids = new ArrayList<String>(my_reviewer_ids);
		else if(a_user_role == Role.AUTHOR)
		{
		    ids = new ArrayList<String>();
		    ids.add(my_author_id);
		}
		else if(a_user_role == Role.SUBPROGRAM_CHAIR)
		{
		    ids = new ArrayList<String>();
		    ids.add(my_subprogram_chair_id);
		}
		
		return new ArrayList<String>(ids);
	}
	
	/**
	 * Returns the ID of the author, not the name.
	 * @return The ID (email address) of the author.
	 */
	public String getAuthorID() 
	{
		return my_author_id;
	}
	
	public String getSubprogramChairID() 
	{
		return my_subprogram_chair_id;
	}
	
	public void setSubprogramChairID(final String the_id)
	{
		my_subprogram_chair_id = the_id;
	}
	
	public int getAcceptanceStatus() 
	{ 
	    return my_acceptance_status; 
	}
	
	/**
	 * Sets the acceptance status for the paper, should only be used
	 * by the program chair.
	 * @param the_status The status to set.
	 * @return <code>true</code> if the status was successfully set.
	 */
	public boolean setAcceptanceStatus(final int the_status) 
	{
	    if(the_status == REJECTED || the_status == ACCEPTED ||
	            the_status == UNDECIDED)
	    {
	        my_acceptance_status = the_status;
	        return true;
	    }
	    return false;
	}
	
	public String getTitle() 
	{ 
	    return my_manuscript_title; 
	}
	
	
	/**
	 * Sets the title of the paper. This only changes what will show in the 
	 * software, not what's written in the manuscript document itself.
	 * @param a_title The new title of the paper.
	 */
	public void setTitle(final String a_title) 
	{
	    if(a_title == null)
	        throw new IllegalArgumentException("Title cannot be null");
	    if(a_title == "")
	        throw new IllegalArgumentException("Title cannot be empty");
	    
	    my_manuscript_title = a_title;
	}
	
	/**
	 * Gets a File object for the manuscript.
	 * @return The manuscript the Paper object is for. Returns 
	 * <code>null</code> if there isn't a manuscript.
	 */
	public File getManuscript()
	{
	    if(my_manuscript_doc == null)
	        return null;
	    return my_manuscript_doc.getAbsoluteFile();
	}
	
	/**
	 * Sets the manuscript document to the one given. Used to update 
	 * the manuscript to a new version.
	 * @param the_manuscript The manuscript doc to set.
	 * @return <code>true</code> if the manuscript was successfully changed.
	 */
	public boolean setManuscript(final File the_manuscript)
	{
	    //permissions checking should be handled by the GUI
	    if(the_manuscript == null)
	        throw new IllegalArgumentException("Manuscript cannot be null");
	    if(!the_manuscript.exists())
	        throw new IllegalArgumentException("Manuscript must exist");
	    if(the_manuscript.isDirectory())
	        throw new IllegalArgumentException("Manuscript cannot be " +
	        		"a directory");

	    if(my_manuscript_doc.exists())
	        my_manuscript_doc.delete();
	    
	    copyPaperDoc(my_directory, the_manuscript);
	    return true;
	}
	
	public void addReview(final Review a_review) 
	{
	    if(a_review == null)
	        throw new IllegalArgumentException("Review cannot be null");
	    
		my_reviews.add(a_review);
	}
	
	/**
	 * Removes the Review from this paper created by the given reviewer ID.
	 * @param reviewer_id The ID of the use whose Review we want to remove.
	 * @return <code>true</code> if the Review was removed successfully.
	 */
	public boolean removeReview(final String reviewer_id)
	{
		for(Review r : my_reviews)
		{
			if(r.getReviewerID().equals(reviewer_id))
				return my_reviews.remove(r);
		}
		return false;
	}
	
	public Set<Review> getReviews()
	{
		return new HashSet<Review>(my_reviews);
	}
	
	/**
	 * Gets a given user ID's Review object if it exists.
	 * @param an_ID The ID of the user who's review you want.
	 * @return The given user's Review if it exists, <code>null</code> if
	 * it does not exist.
	 */
	public Review getReviewByID(final String an_id)
	{
		for(Review r : my_reviews)
		{
			if(r.getReviewerID().equals(an_id))
				return r.getCopy();
		}
		return null;
	}
	
	public boolean addReviewer(final String an_id)
	{
		if(my_reviewer_ids.contains(an_id))
			return false;
		else
		{
			my_reviewer_ids.add(an_id);
			return true;
		}
	}
	
	
	/**
	 * Assigns the given userid as the subprogram chair for the paper.
	 * @param userid The user ID to make subprogram chair for the paper.
	 */
	public void assignSubprogramChair(final String the_user_id) 
	{
	    if(the_user_id == null)
	        throw new IllegalArgumentException("Subprogram Chair user " +
	        		"ID cannot be null");
	    
	    //business rule 9 should be handled by the GUI...
	    my_subprogram_chair_id = the_user_id;
	}
	
	/**
	 * Adds the given user ID to the list of reviewers for this paper.
	 * @param userid The user ID to add.
	 * @return <code>true</code> if the user was added, <code>false</code>
	 * if they weren't added for some reason (ex: if the given user is the 
	 * author, or they're already a reviewer).
	 */
	public boolean assignReviewer(final String a_user_id) 
	{
	    if(a_user_id == null)
	        throw new IllegalArgumentException("Reviewer user ID cannot " +
	        		"be null");
	    
		Role r = getRole(a_user_id);
		
		//business rules 8 and 10 (they're pretty much the same thing...)
		// should be handled by the GUI, (but it isn't required by the official
		// requirements...
		
		if(r != Role.AUTHOR && r != Role.REVIEWER) 
			return my_reviewer_ids.add(a_user_id);
		
		return false;
	}
	
	private void copyPaperDoc(final File the_paper_directory,
			final File the_manuscript_doc) 
	{
		String docname = FileHelper.getLeafString(the_manuscript_doc);
		File copy = new File(the_paper_directory, docname);
		
		FileHelper.copyFile(the_manuscript_doc, copy);
		my_manuscript_doc = copy;
	}
	
	private void initFields() 
	{
		my_manuscript_doc = null;
		my_acceptance_status = UNDECIDED;
		my_author_id = NOT_AVAILIBLE;
		my_subprogram_chair_id = NOT_AVAILIBLE;
		my_reviews = new HashSet<Review>();
		my_recommendation = null;
		my_reviewer_ids = new HashSet<String>();
		my_manuscript_title = NOT_AVAILIBLE;
	}
}
