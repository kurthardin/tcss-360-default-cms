/*
 * PaperDocument.java
 * 12-01-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.test.xml;

import java.io.File;
import java.util.Set;

import org.w3c.dom.Element;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class PaperDocument extends InfoDocument 
{	
	public PaperDocument(File the_output_file) 
	{
		super(the_output_file);
	}

	public PaperDocument addMyReviewerIds(Set<String> the_value) 
	{	
		if (the_value != null) 
		{
			// Create root "my_reviewer_ids" XML element
			Element reviewer_ids_element = my_document.createElement(
					Paper.XML_ELEMENT_MY_REVIEWER_IDS);
			
			// Create a "user" sub-element in 
			// "my_reviewer_ids" for each reviewer
			for (String user_id : the_value) 
			{
				Element user_elem = my_document.createElement(
						User.XML_ELEMENT_USER);
				user_elem.setAttribute(Conference.XML_ATTR_ID, user_id);
				reviewer_ids_element.appendChild(user_elem);
			}
			
			my_fields_element.appendChild(reviewer_ids_element);
		}
		return this;
	}
}
