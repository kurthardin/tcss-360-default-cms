/**
 * Review.java
 * Author: Travis Lewis
 * Date: 8 November 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class Review {
	/*
	 * the info.dat file for a review will likely contain only three lines:
	 * 
	 * [reviewer id]
	 * [summary rating (an int)]
	 * [name of the review doc, relative to info.dat]
	 */
	
	
	//////////////
	// FIELDS
	//////////////
	/** If the review has not been assigned a summary rating yet. */
	public static final int NO_RATING = -1;
	
	/** 1 to 5 rating. */
	public int my_summary_rating;
	
	
	private static final String DATA_FILE_NAME = "info.dat";
	
	/** The location and name of the review document. (.pdf, .docx, etc). */
	private File my_review_doc;
	
	/** User ID of the reviewer. */
	private String my_reviewer_id;
	
	private File my_directory;
	
	
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
		//TODO: test this constructor
		my_directory = the_review_directory;
		my_reviewer_id = "";
		my_summary_rating = NO_RATING;
		my_review_doc = null;
		
		BufferedReader info = FileHelper.getFileReader(my_directory,
				DATA_FILE_NAME);
		
		if(info != null)
		{
			String str;
			try {
				str = info.readLine();
				my_reviewer_id = str;
				str = info.readLine();
				my_summary_rating = Integer.parseInt(str);
				str = info.readLine();
				my_review_doc = new File(my_directory + "/" + str);
				info.close();
			} catch (IOException e) {
				//auto generated, don't know what to replace it with
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_review_directory The review's directory, which will contain
	 * the info.dat file and the review document file. The directory should
	 * already exist before passed to this method.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 */
	Review(final File the_review_directory, final String the_reviewer_id, 
			final File the_review_doc) {
		my_reviewer_id = the_reviewer_id;
		my_review_doc = null;
		my_summary_rating = NO_RATING;
		my_directory = the_review_directory;
		
		copyReviewDoc(the_review_directory, the_review_doc);
	}
	
	/**
	 * Constructs a new Review object in the location given, with the
	 * information given.
	 * @param the_review_directory The review's directory, which will contain
	 * the info.dat file and the review document file. The directory should
	 * already exist before passed to this method.
	 * @param the_reviewer_id The user ID of the Review owner/creator.
	 * @param the_review_doc The actual document file (.pdf, .docx, etc) for
	 * the review.
	 * @param the_summary_rating The 1 to 5 summary rating.
	 */
	Review(final File the_review_directory, final String the_reviewer_id, 
			final File the_review_doc, final int the_summary_rating) {
		my_reviewer_id = the_reviewer_id;
		my_review_doc = null;
		my_summary_rating = the_summary_rating;
		my_directory = the_review_directory;
		
		copyReviewDoc(my_directory, the_review_doc);
	}
	
	
	//////////////
	// METHODS
	//////////////
	
	public File getDirectory() {
		return new File(my_directory.getAbsolutePath());
	}
	
	public String getReviewerID() {
		return my_reviewer_id;
	}
	
	/**
	 * Gets a File for the review document, the File returned is the one
	 * that was copied to the review directory.
	 * @return The review document if one exists, <code>null</code> if the
	 * Review hasn't been given a review document yet.
	 */
	public File getReviewDoc() {
		if(my_review_doc.exists())
			return new File(my_review_doc.getPath());
		else
			return null;
	}
	
	/**
	 * Deletes the old review document and replaces it with the one given.
	 * @param the_doc The new review document.
	 */
	public void setReviewDoc(File the_doc) {
		//delete old doc
		if(my_review_doc.exists())
			my_review_doc.delete();
		
		//copy new doc over
		copyReviewDoc(my_directory,the_doc);
	}
	
	/**
	 * Saves data about the review by deleting and creating a new info.dat
	 * @return <code>true</code> if the operation succeeded.
	 */
	public boolean saveReview() {
		File info = new File(my_directory.getAbsolutePath() + "/" + 
				DATA_FILE_NAME);
		
		if(info.exists()) {
			info.delete();
		}
		
		try {
			info.createNewFile();
			BufferedWriter bw = FileHelper.getFileWriter(info);
			if(bw == null)
				return false;
			bw.write(my_reviewer_id + '\n');
			bw.write(String.valueOf(my_summary_rating) + '\n');
			bw.write(FileHelper.getLeafString(my_review_doc) + '\n');
			bw.close();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Copies the given document into the given directory.
	 * @param the_review_directory The directory to copy the file into.
	 * @param the_review_doc The document to copy.
	 */
	private void copyReviewDoc(final File the_review_directory, 
			final File the_review_doc) {
		String docname = FileHelper.getLeafString(the_review_doc);
		File copied_file = new File(the_review_directory.getAbsolutePath() 
				+ "/" + docname);
		FileHelper.copyFile(the_review_doc, copied_file);
		my_review_doc = copied_file;
	}
}
