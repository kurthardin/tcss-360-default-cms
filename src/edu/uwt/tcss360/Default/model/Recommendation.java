/*
 * Recommendation.java
 * Kurt Hardin
 * 12-03-2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;

import edu.uwt.tcss360.Default.util.FileHelper;

/**
 * @author Kurt Hardin
 */
public class Recommendation extends Review {

	//////////////
	// CONSTRUCTORS
	//////////////
	/**
	 * Constructs a Review object using previously saved information located
	 * in the data file in the given directory.
	 * @param the_review_directory The review's directory, which contains the
	 * data file and the review document file.
	 */
	public Recommendation(final File the_review_directory) 
	{
		super(the_review_directory);
	}

	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_paper_directory The review's directory, which will contain
	 * the data file and the review document file. The directory should
	 * already exist before passed to this method.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 */
	public Recommendation(final File the_paper_directory, 
			final String the_reviewer_id, final File the_review_doc) 
	{
		super(the_paper_directory, the_reviewer_id, the_review_doc);
	}

	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_paper_directory The review's directory, which will contain
	 * the data file and the review document file. The directory should
	 * already exist before passed to this method.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 * @param the_summary_rating The 1 to 5 summary rating.
	 */
	public Recommendation(final File the_paper_directory, 
			final String the_reviewer_id, final File the_review_doc, 
			final int the_summary_rating) 
	{
		super(the_paper_directory, the_reviewer_id, 
				the_review_doc, the_summary_rating);
	}


	//////////////
	// METHODS
	//////////////

	public String getDirectoryName() {
		return FileHelper.RECOMMENDATION_DIRECTORY_NAME;
	}

}
