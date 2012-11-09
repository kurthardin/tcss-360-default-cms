/**
 * Paper.java
 * Author: Travis Lewis
 * Date: 8 Nov 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.uwt.tcss360.Default.model.Review;
import edu.uwt.tcss360.Default.model.User.Role;

public class Paper {

	/////////////
	// FIELDS
	/////////////
	/** The location on disk of the directory belonging to the paper. */
	private File my_directory;
	
	/** The relative location of the info.dat for the paper. */
	private String my_paper_info_location;
	
	/** The relative location of the manuscript document file. */
	private String my_manuscript_location;
	
	/** 1-5 value for acceptance status. (not recommendation the 1-5) */
	private int my_acceptance_status;
	
	/** User ID of the manuscript author. */
	private String my_author_id;
	
	/** User ID of the subprogram chair for the paper. */
	private String my_subprogram_chair_id;
	
	/** List of review objects. */
	private List<Review> my_reviews;
	// that ^ might as well be public if we're just going to have getReviews()
	// unless we're returning a read-only copy of the list
	
	private List<String> my_reviewer_ids;
	
	/////////////
	// CONSTRUCTORS
	/////////////
	/**
	 * Constructs a Paper object from a previously created and saved Paper
	 * object's info.dat file.
	 * @param the_paper_directory The directory containing the info.dat for 
	 * the paper and the review/recommendation folders.
	 */
	public Paper(final File the_paper_directory) {
		my_directory = the_paper_directory;
		
		//TODO: load/parse the data from disk
	}
	
	/**
	 * Constructs a new Paper object from the given information.
	 * @param the_author_id The user ID of the author.
	 * @param the_manuscript_doc The document file (.pdf, .docx, etc) of the
	 * manuscript.
	 * @param the_paper_directory The directory that is going to contain
	 * the info.dat for the paper and the review/recommendation folders. None
	 * of which need to currently exist, since this constructor will create
	 * them.
	 */
	public Paper(final String the_author_id, final File the_manuscript_doc,
			final File the_paper_directory) {
		my_author_id = the_author_id;
		
		//TODO: create the_paper_directory and copy the manuscript to it
	}
	
	
	/////////////
	// METHODS
	/////////////
	
	public List<Review> getReviews() {
		return new ArrayList<Review>(my_reviews);
	}
	
	/**
	 * Gets the role that the given user id has with this paper.
	 * @param a_user_id The user to find the role of.
	 * @return Role.USER if the given user isn't involved with the paper.
	 */
	public Role getRole(final String a_user_id) {
		if(a_user_id == my_author_id)
			return Role.AUTHOR;
		
		if(my_subprogram_chair_id == a_user_id)
			return Role.SUBPROGRAM_CHAIR;
		
		for(Review r : my_reviews) {
			if(r.getReviewerID() == a_user_id)
				return Role.REVIEWER;
		}
		
		return Role.USER;
	}
	
	public List<String> getUserIDs(final Role a_user_role) {
		List<String> ids = new ArrayList<String>();
		
		//TODO: actually get the IDs.
		
		return new ArrayList<String>(ids);
	}
	
	public String getAuthorID() {
		return my_author_id;
	}
	
	public String getSubprogramChairID() {
		return my_subprogram_chair_id;
	}
	
	public void addReview(final Review a_review) {
		my_reviews.add(a_review);
	}
	
	/**
	 * Assigns the given userid as the subprogram chair for the paper.
	 * @param userid The user ID to make subprogram chair for the paper.
	 * @return <code>true</code> if the user was made subprogram chair,
	 * <code>false</code> if they couldn't be made subprogram chair for some
	 * reason (ex: if the given user is the author).
	 */
	public boolean assignSubprogramChair(final String the_user_id) {
		Role r = getRole(the_user_id);
		
		//business rule 9
		if(r != Role.AUTHOR) {
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
	public boolean assignReviewer(final String a_user_id) {
		Role r = getRole(a_user_id);
		
		//business rules 8 and 10 (they're pretty much the same thing...)
		if(r != Role.AUTHOR && r != Role.REVIEWER) {
			my_reviewer_ids.add(a_user_id);
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method will save the Paper (and any changes) to disk.
	 * @return <code>true</code> if successfully saved.
	 */
	public boolean savePaper() {
		// this is where the info.dat gets created and all the stuff gets
		// saved to disk.
	}
}
