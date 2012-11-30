/*
 * UsersHandler.java
 * 11-30-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.util.xml.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.User;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class UsersHandler extends DefaultHandler {

	private final ConferencesManager my_confs_mgr;
	private Locator my_sax_locator;
	private boolean my_users_elem = false;
	
	public UsersHandler(final ConferencesManager the_confs_mgr) {
		super();
		my_confs_mgr = the_confs_mgr;
	}
	
	@Override
	public void setDocumentLocator(Locator locator) 
	{
		my_sax_locator = locator;
	}
	
	/**
	 * Handles the start of a new XML element
	 */
	@Override
	public final void startElement(String uri, String localName, 
			String qName, Attributes attr) throws SAXException 
    {
		if (qName.equals(User.XML_ELEMENT_USERS)) 
		{
			my_users_elem = true;
		}
		else if (qName.equalsIgnoreCase(User.XML_ELEMENT_USER) &&
				my_users_elem) 
		{
			my_confs_mgr.addUser(
					new User(attr.getValue(User.XML_ATTR_MY_ID),
							attr.getValue(User.XML_ATTR_MY_NAME)));
		} 
		else
		{
			throw new SAXParseException(
					"Encountered unexpected element: " + qName, 
					my_sax_locator);
		}
	}
	
	/**
	 * Handles the end of a new SML element
	 */
	@Override
	public final void endElement(String uri, String localName,
		String qName) throws SAXException 
	{
		
		if (qName.equalsIgnoreCase(User.XML_ELEMENT_USERS)) 
		{
			my_users_elem = false;
		}
	}
}
