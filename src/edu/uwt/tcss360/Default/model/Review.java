/**
 * Review.java
 * Author: Travis Lewis
 * Date: 8 November 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;

public class Review {
	//////////////
	// FIELDS
	//////////////
	/** If the review has not been assigned a summary rating yet. */
	public static int NO_RATING = -1;
	
	/** The location and name of the review document. (.pdf, .docx, etc). */
	public File my_review_doc;
	
	/** 1 to 5 rating. */
	public int my_summary_rating;
	
	/** User ID of the reviewer. */
	private String my_reviewer_id;
	
	
	//////////////
	// CONSTRUCTORS
	//////////////
	/**
	 * Constructs a Review object using previously saved information located
	 * in the info.dat file in the given directory.
	 * @param the_review_directory The review's directory, which contains the
	 * info.dat file and the review document file.
	 */
	Review(final File the_review_directory) {
		//TODO: load/parse the info.dat file in the directory
		
	}
	
	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_review_directory The review's directory, which contains the
	 * info.dat file and the review document file.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 */
	Review(final File the_review_directory, final String the_reviewer_id, 
			final File the_review_doc) {
		my_reviewer_id = the_reviewer_id;
		my_review_doc = the_review_doc;
		my_summary_rating = NO_RATING;
		
		copyReviewDoc(the_review_directory, the_review_doc);
	}
	
	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_review_directory The review's directory, which contains the
	 * info.dat file and the review document file.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 * @param the_summary_rating The 1 to 5 summary rating.
	 */
	Review(final File the_review_directory, final String the_reviewer_id, 
			final File the_review_doc, final int the_summary_rating) {
		my_reviewer_id = the_reviewer_id;
		my_review_doc = the_review_doc;
		my_summary_rating = the_summary_rating;
		
		copyReviewDoc(the_review_directory, the_review_doc);
	}
	
	
	//////////////
	// METHODS
	//////////////
	public String getReviewerID() {
		return my_reviewer_id;
	}
	
	
	
	private void copyReviewDoc(final File the_review_directory, 
			final File the_review_doc) {
		String docname = FileHelper.getLeafString(the_review_doc);
		File copied_file = new File(the_review_directory.getAbsolutePath() 
				+ "/" + docname);
		FileHelper.copyFile(the_review_doc, copied_file);
		my_review_doc = copied_file;
	}
}
