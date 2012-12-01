/*
 * InfoHandler.java
 * 11-30-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.util.xml.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uwt.tcss360.Default.util.xml.InfoDocument;

/**
 * 
 * @author Kurt Hardin
 * @version 1.0
 */
public abstract class InfoHandler extends DefaultHandler 
{
	private Locator my_sax_locator;
	private boolean my_fields_elem = false;
	
	@Override
	public void setDocumentLocator(Locator locator) 
	{
		my_sax_locator = locator;
	}
 
	public final void startElement(String uri, String localName, String qName, 
                Attributes attributes) throws SAXException 
    {
		if (qName.equalsIgnoreCase(InfoDocument.XML_ELEMENT_FIELDS)) 
		{
			my_fields_elem = true;
			handleFieldsAttributes(attributes);
		} 
		else if (my_fields_elem) 
		{
			startUnknownFieldsElement(uri, localName, qName, attributes);
		} 
		else 
		{
			throw new SAXParseException(
					"Encountered unexpected element: " + qName, 
					my_sax_locator);
		}
 
	}
	
	public abstract void handleFieldsAttributes(Attributes attr);
	
	public void startUnknownFieldsElement(String uri, String localName,
			String qName, Attributes attr) 
	{
		// Do nothing...
	}
 
	public final void endElement(String uri, String localName,
		String qName) throws SAXException 
	{
		
		if (qName.equalsIgnoreCase(InfoDocument.XML_ELEMENT_FIELDS)) 
		{
			my_fields_elem = false;
		} 
		else if (my_fields_elem) 
		{
			endUnknownFieldsElement(uri, localName, qName);
		}
	}
	
	public void endUnknownFieldsElement(String uri, String localName,
			String qName) throws SAXException 
	{
		// Do nothing...
	}

}
