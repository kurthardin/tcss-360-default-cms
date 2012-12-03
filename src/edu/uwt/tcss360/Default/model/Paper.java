/**
 * Paper.java
 * Author: Travis Lewis
 * Date: 8 Nov 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;
import edu.uwt.tcss360.Default.util.xml.PaperDocument;
import edu.uwt.tcss360.Default.util.xml.parsers.CMSDParser;
import edu.uwt.tcss360.Default.util.xml.parsers.InfoHandler;

/**
 * Holds information about a manuscript along with reviews of it, etc.
 * 
 * @author Travis Lewis
 * @editor Kurt Hardin
 * @version 8 Nov 2012
 */
public class Paper implements Comparable<Paper>
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
	public static final String XML_ELEMENT_MY_REVIEWER_IDS = 
			"my_reviewer_ids";
	public static final String XML_ATTR_ID = "id";
	
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
	
	private Set<String> my_reviewer_ids = new TreeSet<String>();
	
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
		
		File input_file = new File(my_directory, FileHelper.DATA_FILE_NAME);
		PaperInfoHandler handler = new PaperInfoHandler();
		new CMSDParser(input_file, handler).parse();
		
		File recommendation_dir = new File(my_directory, "recommendation");
		if (recommendation_dir.exists()) 
		{
			my_recommendation = new Review(recommendation_dir);
		}
		
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
		
		
		for (String review_dir_name : review_dir_names) 
		{
			File review_dir = new File(my_directory, review_dir_name);
			
			Review review = new Review(review_dir);
			my_reviews.add(review);
			my_reviewer_ids.add(review.getReviewerID());
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
		new PaperDocument(output_file)
		.addMyReviewerIds(my_reviewer_ids)
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

	@Override
	public int compareTo(Paper another_paper) {
		int result = my_manuscript_title.compareTo(
				another_paper.my_manuscript_title);
		if (result == 0) {
			result = my_author_id.compareTo(another_paper.my_author_id);
		}
		return result;
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
		
		if(my_subprogram_chair_id != null && 
				my_subprogram_chair_id.equals(a_user_id))
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
		return Collections.unmodifiableSet(my_reviews);
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
				return r;
		}
		return null;
	}
	
	/**
	 * Adds the given user ID to the list of reviewers for this paper.
	 * @param an_id The user ID to add.
	 * @return <code>true</code> if the user was added, <code>false</code>
	 * if the user was not added because they were already in the list of 
	 * reviewers.
	 */
	public boolean addReviewer(final String an_id)
	{
		if(my_reviewer_ids.contains(an_id))
			return false;
		else
		{
			my_reviewer_ids.add(an_id);
			//TODO: create some kind of blank/empty directory for the reviewer
			// to put stuff into.
			// A review directory gets created when instantiating a new
			// Review object. -Kurt
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
	
	private void copyPaperDoc(final File the_paper_directory,
			final File the_manuscript_doc) 
	{
		String docname = the_manuscript_doc.getName();
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
		my_reviews = new TreeSet<Review>();
		my_recommendation = null;
		my_reviewer_ids = new TreeSet<String>();
		my_manuscript_title = NOT_AVAILIBLE;
	}
	
	private class PaperInfoHandler extends InfoHandler
	{
		private boolean my_inside_reviewer_ids;
		
		@Override
		public void handleFieldsAttributes(Attributes attr) 
		{
			String acceptStatusStr = attr.getValue(
					XML_ATTR_MY_ACCEPTANCE_STATUS);
			my_acceptance_status = InfoDocument.getIntFromAttributeValue(
					acceptStatusStr);
			
			my_author_id = attr.getValue(XML_ATTR_MY_AUTHOR_ID);
			my_subprogram_chair_id = attr.getValue(
					XML_ATTR_MY_SUBPROGRAM_CHAIR_ID);
			my_manuscript_title = attr.getValue(
					XML_ATTR_MY_MANUSCRIPT_TITLE);
			
			String doc_name = attr.getValue(
					XML_ATTR_MY_MANUSCRIPT_DOC);
			my_manuscript_doc = new File(my_directory, doc_name);
		}
		
		@Override
		public boolean startUnknownFieldsElement(String uri, 
				String localName, String qName, Attributes attr)
		{
			if (qName.equalsIgnoreCase(XML_ELEMENT_MY_REVIEWER_IDS)) 
			{
				//Started "my_reviewer_ids" element
				my_inside_reviewer_ids = true;
			}
			else if (my_inside_reviewer_ids) 
			{
				if (qName.equalsIgnoreCase(User.XML_ELEMENT_USER)) 
				{
					// Started new "user" element within "my_reviewer_ids"
					my_reviewer_ids.add(attr.getValue(XML_ATTR_ID));
				} 
			} 
			else 
			{
				return false;
			}
			
			return true;
		}
		
		@Override
		public final void endUnknownFieldsElement(String uri, String localName,
				String qName) throws SAXException 
		{
			if (qName.equalsIgnoreCase(XML_ELEMENT_MY_REVIEWER_IDS)) 
			{
				my_inside_reviewer_ids = false;
			}
		}
	}
}
