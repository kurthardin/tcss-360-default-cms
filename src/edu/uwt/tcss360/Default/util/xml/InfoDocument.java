/*
 * InfoDocument.java
 * Kurt Hardin
 * 11-30-2012
 */

package edu.uwt.tcss360.Default.util.xml;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import org.w3c.dom.Element;

import edu.uwt.tcss360.Default.model.Conference;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class InfoDocument extends CMSDDocument 
{
	// XML element names
	public static final String XML_ELEMENT_FIELDS = "fields";
	
	public static final int getIntFromAttributeValue(String the_value) 
	{
		return Integer.valueOf(the_value);
	}
	
	public static final Date getDateFromAttributeValue(String the_value) 
	{
		if (the_value == null) {
			return null;
		}
		
		try {
			return Conference.CONFERENCE_DATE_FORMAT.parse(the_value);
		} catch (ParseException e) {
			return null;
		}
	}
	
	protected final Element my_fields_element;
	
	public InfoDocument(File the_output_file) 
	{
		super(the_output_file);
		
		my_fields_element = my_document.createElement(XML_ELEMENT_FIELDS);
		my_document.appendChild(my_fields_element);
	}
	
	public InfoDocument setField(String the_field_name, String the_value) 
	{
		if (the_value != null) {
			my_fields_element.setAttribute(the_field_name, the_value);
		}
		return this;
	}
	
	public InfoDocument setField(String the_field_name, int the_value) 
	{
		my_fields_element.setAttribute(the_field_name, 
				String.valueOf(the_value));
		return this;
	}
	
	public InfoDocument setField(String the_field_name, Date the_value) 
	{
		if (the_value != null) {
			my_fields_element.setAttribute(the_field_name, 
					Conference.CONFERENCE_DATE_FORMAT.format(the_value));
		}
		return this;
	}
	
	public InfoDocument setField(String the_field_name, File the_value) {
		if (the_value != null) {
			my_fields_element.setAttribute(the_field_name, 
					the_value.getName());
		}
		return this;
	}
	
}
