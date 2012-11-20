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
import edu.uwt.tcss360.Default.util.InfoHandler;

public class Paper 
{
	/////////////
	// CONSTANTS
	/////////////
    /** The value of my_acceptance_status until one is set. */
	public static final int NO_ACCEPTANCE_STATUS = -1;
	
	/** The value of a field that hasn't been set yet. */
	private static final String NOT_AVAILIBLE = "n/a";
	
	
	/////////////
	// FIELDS
	/////////////
	/** The location on disk of the directory belonging to the paper. */
	private final File my_directory;
	
	// TODO Remove my_manuscript_doc or include in XML data file.
	/** The actual document (docx, pdf, etc), of the manuscript. */
	private File my_manuscript_doc;
	
	/** 1-5 value for acceptance status. (not recommendation the 1-5) */
	private int my_acceptance_status;
	
	/** User ID of the manuscript author. */
	private String my_author_id;
	
	/** User ID of the subprogram chair for the paper. */
	private String my_subprogram_chair_id;
	
	/** List of review objects. */
	private Set<Review> my_reviews;
	// that ^ might as well be public if we're just going to have getReviews()
	// unless we're returning a read-only copy of the list
	
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
								"my_acceptance_status");
						my_acceptance_status = (acceptStatusStr == null) ? 
								NO_ACCEPTANCE_STATUS : 
								Integer.valueOf(acceptStatusStr);
						
						my_author_id = attr.getValue("my_author_id");
						my_subprogram_chair_id = attr.getValue(
								"my_subprogram_chair_id");
						my_manuscript_title = attr.getValue(
								"my_manuscript_title");
					}
				};

				saxParser.parse(info, handler);
				
				File recommendation_dir = new File(
						my_directory.getAbsolutePath() + "/recommendation");
				my_recommendation = new Review(recommendation_dir);
				
				String[] review_dir_names = my_directory.list(
						new FilenameFilter() 
						{
							@Override
							public boolean accept(File dir, String name) 
							{
								return name.startsWith("review_") &&
										(new File(dir, name).isDirectory());
							}
						});
				
				my_reviewer_ids = new HashSet<String>(review_dir_names.length);
				
				for (String review_dir_name : review_dir_names) 
				{
					File review_dir = new File(
							my_directory.getAbsolutePath() + "/" + 
							review_dir_name);
					
					Review review = new Review(review_dir);
					my_reviews.add(review);
					my_reviewer_ids.add(review.getReviewerID());
				}

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
// TODO Remove old code...
//			String str;
//			try 
//			{
//				str = info.readLine();
//				my_author_id = str;
//				
//				str = info.readLine();
//				my_manuscript_title = str;
//				
//				str = info.readLine();
//				my_subprogram_chair_id = str;
//				
//				str = info.readLine();
//				if(str != NOT_AVAILIBLE)
//					my_recommendation = new Review(new 
//							File(my_directory.getAbsolutePath() +
//							"/" + str));
//				
//				while((str = info.readLine()) != NOT_AVAILIBLE) 
//				{
//					File review_folder = new File(
//							my_directory.getAbsolutePath() + "/" + str);
//					
//					Review r = new Review(review_folder);
//					my_reviews.add(r);
//				}
//			} 
//			catch (IOException e) 
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
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
	    
	    String directory_name = my_manuscript_title + my_author_id;
		File directory = new File(the_papers_directory, directory_name);
		if (!directory.exists()) {
			directory = FileHelper.createDirectory(the_papers_directory, 
					directory_name);
		}
		my_directory = directory;
		
		copyPaperDoc(my_directory, the_manuscript_doc);		
	}
	
	
	/////////////
	// METHODS
	/////////////
	
	public File getDirectory() 
	{
		return new File(my_directory.getAbsolutePath());
	}
	
	public List<Review> getReviews() 
	{
		return new ArrayList<Review>(my_reviews);
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
	    
		if(a_user_id == my_author_id)
			return Role.AUTHOR;
		
		if(my_subprogram_chair_id == a_user_id)
			return Role.SUBPROGRAM_CHAIR;
		
		for(Review r : my_reviews) 
			if(r.getReviewerID() == a_user_id)
				return Role.REVIEWER;
		
		return Role.USER;
	}
	
	public List<String> getUserIDs(final Role a_user_role) 
	{
	    if(a_user_role == null)
	        throw new IllegalArgumentException("Role cannot be null");
	    
		List<String> ids = new ArrayList<String>();
		
		//TODO: actually get the IDs.
		
		return new ArrayList<String>(ids);
	}
	
	public String getAuthorID() 
	{
		return my_author_id;
	}
	
	public String getSubprogramChairID() 
	{
		return my_subprogram_chair_id;
	}
	
	public int getAcceptanceStatus() 
	{ 
	    return my_acceptance_status; 
	}
	
	/**
	 * Sets the acceptance status for the paper, If I understand correctly
	 * only the Program Chair for the conference can set the status.
	 * @param the_status The status to set.
	 * @param a_role The role of the user trying to set the status.
	 * @return <code>true</code> if the status was successfully set.
	 */
	public boolean setAcceptanceStatus(final int the_status, 
			final Role a_role) 
	{
	    if(the_status > 5 || the_status < -1 || the_status == 0)
	        throw new IllegalArgumentException("Status is an invalid value");
	    if(a_role == null)
	        throw new IllegalArgumentException("Role cannot be null");
	    
		if(a_role.equals(Role.PROGRAM_CHAIR)) 
		{
		    if(the_status >= 0 && the_status <= 5)
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
	 * Allows an authorized user to set the title of the paper. This only
	 * changes what will show in the software, not what's written in the
	 * manuscript document itself.
	 * @param a_title The new title of the paper.
	 * @param a_role The role of the user attempting to change the title.
	 * @return <code>true</code> if the title was successfully changed.
	 */
	public boolean setTitle(final String a_title, final Role a_role) 
	{
	    if(a_title == null)
	        throw new IllegalArgumentException("Title cannot be null");
	    if(a_role == null)
	        throw new IllegalArgumentException("Role cannot be null");
	    
		if(a_role.equals(Role.PROGRAM_CHAIR) || 
				a_role.equals(Role.SUBPROGRAM_CHAIR) ||
				a_role.equals(Role.AUTHOR)) 
		{
			my_manuscript_title = a_title;
			return true;
		}
		
		return false;
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
	    return new File(my_manuscript_doc.getAbsolutePath());
	}
	
	/**
	 * Sets the manuscript document to the one given if the action is 
	 * requested by the author. Used to update the manuscript to a 
	 * new version.
	 * @param the_manuscript The manuscript doc to set.
	 * @param the_role The role of the user requesting the action.
	 * @return <code>true</code> if the manuscript was successfully changed.
	 */
	public boolean setManuscript(final File the_manuscript, 
	        final Role the_role)
	{
	    if(the_manuscript == null)
	        throw new IllegalArgumentException("Manuscript cannot be null");
	    if(!the_manuscript.exists())
	        throw new IllegalArgumentException("Manuscript must exist");
	    if(the_manuscript.isDirectory())
	        throw new IllegalArgumentException("Manuscript cannot be " +
	        		"a directory");
	    if(the_role == null)
	        throw new IllegalArgumentException("Role cannot be null");
	    
	    if(the_role != Role.AUTHOR)
	        return false;
	    
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
	 * Assigns the given userid as the subprogram chair for the paper.
	 * @param userid The user ID to make subprogram chair for the paper.
	 * @return <code>true</code> if the user was made subprogram chair,
	 * <code>false</code> if they couldn't be made subprogram chair for some
	 * reason (ex: if the given user is the author).
	 */
	public boolean assignSubprogramChair(final String the_user_id) 
	{
	    if(the_user_id == null)
	        throw new IllegalArgumentException("Subprogram Chair user " +
	        		"ID cannot be null");
	    
		Role r = getRole(the_user_id);
		
		//business rule 9
		if(r != Role.AUTHOR) 
		{
			my_subprogram_chair_id = the_user_id;
			return true;
		}
		
		return false;
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
		if(r != Role.AUTHOR && r != Role.REVIEWER) 
		{
			my_reviewer_ids.add(a_user_id);
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method will save the Paper (and any changes) to disk.
	 * @return <code>true</code> if successfully saved.
	 */
	public boolean savePaper() 
	{	
		// TODO Implement save to XML
		
		return false;
// TODO Remove old code
//		File info = new File(my_directory.getAbsolutePath() + "/" +
//				FileHelper.DATA_FILE_NAME);
//		
//		if(info.exists())
//			info.delete();
//		
//		try 
//		{
//			info.createNewFile();
//			BufferedWriter bw = FileHelper.getFileWriter(info);
//			if(bw == null)
//				return false;
//			
//			//author id
//			bw.write(my_author_id + '\n');
//			//paper title
//			bw.write(my_manuscript_title + '\n');
//			//subprogram chair id
//			bw.write(my_subprogram_chair_id + '\n');
//			//recommendation folder
//			if(my_recommendation != null)
//				bw.write(FileHelper.getLeafString(
//						my_recommendation.getDirectory()));
//			else
//				bw.write(NOT_AVAILIBLE + '\n');
//			//review folders
//			if(my_reviews.size() > 0) 
//			{
//				for(Review r : my_reviews)
//					bw.write(FileHelper.getLeafString(r.getDirectory()));
//			} 
//			bw.write(NOT_AVAILIBLE);
//			bw.close();	
//		} 
//		catch (IOException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		
//		return true;
	}
	
	private void copyPaperDoc(final File the_paper_directory,
			final File the_manuscript_doc) 
	{
		String docname = FileHelper.getLeafString(the_manuscript_doc);
		File copy = new File(the_paper_directory.getAbsolutePath() + "/" +
				docname);
		
		FileHelper.copyFile(the_manuscript_doc, copy);
		my_manuscript_doc = copy;
	}
	
	private void initFields() 
	{
		my_manuscript_doc = null;
		my_acceptance_status = NO_ACCEPTANCE_STATUS;
		my_author_id = NOT_AVAILIBLE;
		my_subprogram_chair_id = NOT_AVAILIBLE;
		my_reviews = new HashSet<Review>();
		my_recommendation = null;
		my_reviewer_ids = new HashSet<String>();
		my_manuscript_title = NOT_AVAILIBLE;
	}
}
