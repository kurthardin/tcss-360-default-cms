/**
 * Review.java
 * Author: Travis Lewis
 * Date: 8 November 2012
 */

package edu.uwt.tcss360.Default.model;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.InfoHandler;

public class Review 
{	
	//////////////
	// CONSTANTS
	//////////////
	
	// XML attribute names
	public static final String XML_ATTR_MY_REVIEWER_ID = "my_reviewer_id";
	public static final String XML_ATTR_MY_SUMMARY_RATING = "my_summary_rating";
	public static final String XML_ATTR_MY_REVIEW_DOC = "my_review_doc";
	
	/** If the review has not been assigned a summary rating yet. */
	public static final int NO_RATING = -1;
	
	
	//////////////
	// FIELDS
	//////////////
	/** User ID of the reviewer. */
	private String my_reviewer_id;
	
	/** 1 to 5 rating. */
	public int my_summary_rating;
	
	private final File my_directory;
	
	// TODO Remove my_review_doc or include in XML data file
	/** The location and name of the review document. (.pdf, .docx, etc). */
	private File my_review_doc;
	
	
	//////////////
	// CONSTRUCTORS
	//////////////
	/**
	 * Constructs a Review object using previously saved information located
	 * in the data file in the given directory.
	 * @author Kurt Hardin
	 * @param the_review_directory The review's directory, which contains the
	 * data file and the review document file.
	 */
	public Review(final File the_review_directory) 
	{
		if(the_review_directory == null)
		    throw new IllegalArgumentException("Review directory cannot" +
		    		"be null");
		
		if(!the_review_directory.exists())
		    throw new IllegalArgumentException("Review directory must exist");
	    
		my_directory = the_review_directory;
		my_reviewer_id = "";
		my_summary_rating = NO_RATING;
		my_review_doc = null;
		
		InputSource info = FileHelper.getInputSource(my_directory,
				FileHelper.DATA_FILE_NAME);
		
		if(info == null)
		{
		    throw new IllegalArgumentException(FileHelper.DATA_FILE_NAME +
		    		" could not be found");
		}
		else
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
						my_reviewer_id = attr.getValue(XML_ATTR_MY_REVIEWER_ID);
						
						String rating_str = attr.getValue(
								XML_ATTR_MY_SUMMARY_RATING);
						my_summary_rating = (rating_str == null) ? 
								NO_RATING : Integer.valueOf(rating_str);
						
						String review_doc_str = attr.getValue(
								XML_ATTR_MY_REVIEW_DOC);
						my_review_doc = new File(my_directory, review_doc_str);
					}
				};

				saxParser.parse(info, handler);

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			// TODO Write unit tests for Review(File)
			
		}
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
	public Review(final File the_paper_directory, final String the_reviewer_id, 
			final File the_review_doc) 
	{
		this(the_paper_directory, the_reviewer_id, the_review_doc, 
		        NO_RATING);
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
	public Review(final File the_paper_directory, final String the_reviewer_id, 
			final File the_review_doc, final int the_summary_rating) 
	{
	    if(the_paper_directory == null)
	        throw new IllegalArgumentException("Paper directory cannot " +
	        		"be null");
	    
	    if(!the_paper_directory.exists())
	        throw new IllegalArgumentException("Paper directory must exist");
	    
	    if(the_reviewer_id == null)
	        throw new IllegalArgumentException("Reviewer ID cannot be null");
	    
	    if(the_review_doc == null)
	        throw new IllegalArgumentException("Review doc cannot be null");
	    
	    if(!the_review_doc.exists())
	        throw new IllegalArgumentException("Review doc must exist");
	    
		my_reviewer_id = the_reviewer_id;
		my_review_doc = null;
		my_summary_rating = the_summary_rating;
		
		String directory_name = FileHelper.formatFilename(
				FileHelper.REVIEW_DIRECTORY_PREFIX + my_reviewer_id);
		my_directory = FileHelper.getDirectory(the_paper_directory, 
				directory_name);
		
		copyReviewDoc(my_directory, the_review_doc);
	}
	
	
	//////////////
	// METHODS
	//////////////
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	public void writeData() 
	{
		try 
		{
			// Build the XML document
			Document doc = FileHelper.createXmlDocument();
			
			Element fields_element = doc.createElement(
					FileHelper.XML_ELEMENT_FIELDS);
			
			fields_element.setAttribute("my_reviewer_id", 
					my_reviewer_id);

			fields_element.setAttribute("my_summary_rating", 
					String.valueOf(my_summary_rating));
			
			fields_element.setAttribute("my_review_doc", 
					my_review_doc.getName());
			
			doc.appendChild(fields_element);

			FileHelper.writeXmlDataFile(doc, my_directory, 
					FileHelper.DATA_FILE_NAME);

			System.out.println("Review data file saved: " + 
					my_directory.getName());
		} 
		catch (ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		} 
		catch (TransformerException tfe) 
		{
			tfe.printStackTrace();
		}
		
		// TODO Write unit tests for Review.writeData()
	}
	
	public File getDirectory() 
	{
		return my_directory.getAbsoluteFile();
	}
	
	public String getReviewerID() 
	{
		return my_reviewer_id;
	}
	
	/**
	 * Gets a File for the review document, the File returned is the one
	 * that was copied to the review directory.
	 * @return The review document if one exists, <code>null</code> if the
	 * Review hasn't been given a review document yet.
	 */
	public File getReviewDoc() 
	{
		if(my_review_doc.exists())
			return my_review_doc.getAbsoluteFile();
		else
			return null;
	}
	
	/**
	 * returns the summary rating for the review.
	 * @return summary rating.
	 * @author Scott Sanderson
	 */
	public int getSummaryRating()
	{
		return my_summary_rating;
	}
	
	/**
	 * sets the rating to the appropriate value.
	 * @param the_rating
	 * @author Scott Sanderson
	 */
	public void setSummaryRating(int the_rating)
	{
		if (the_rating < 1 || the_rating > 5 || the_rating == 0)
			throw new IllegalArgumentException();
		my_summary_rating = the_rating;
	}
	
	/**
	 * Deletes the old review document and replaces it with the one given.
	 * @param the_doc The new review document.
	 */
	public void setReviewDoc(File the_doc) 
	{
	    if(the_doc == null)
	        throw new IllegalArgumentException("Review doc cannot be null");
	    
	    if(!the_doc.exists())
	        throw new IllegalArgumentException("Review doc must exist");
	    
	    if(the_doc.isDirectory())
	        throw new IllegalArgumentException("Review doc cannot be " +
	        		"a directory");
	    
		//delete old doc
		if(my_review_doc.exists())
			my_review_doc.delete();
		
		//copy new doc over
		copyReviewDoc(my_directory,the_doc);
	}
	
	/**
	 * Saves data about the review by deleting and creating a new data file
	 * @return <code>true</code> if the operation succeeded.
	 */
	public boolean saveReview() 
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
//			bw.write(my_reviewer_id + '\n');
//			bw.write(String.valueOf(my_summary_rating) + '\n');
//			bw.write(FileHelper.getLeafString(my_review_doc) + '\n');
//			bw.close();
//		} 
//		catch (IOException e) 
//		{
//			// Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
	}
	
	/**
	 * Copies the given document into the given directory.
	 * @param the_review_directory The directory to copy the file into.
	 * @param the_review_doc The document to copy.
	 */
	private void copyReviewDoc(final File the_review_directory, 
			final File the_review_doc) 
	{
		String docname = FileHelper.getLeafString(the_review_doc);
		File copied_file = new File(the_review_directory, docname);
		
		FileHelper.copyFile(the_review_doc, copied_file);
		my_review_doc = copied_file;
	}
}
