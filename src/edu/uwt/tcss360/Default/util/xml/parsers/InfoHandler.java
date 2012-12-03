/*
 * InfoHandler.java
 * Kurt Hardin
 * 11-30-2012
 */

package edu.uwt.tcss360.Default.util.xml.parsers;

import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uwt.tcss360.Default.util.log.CMSLoggerFactory;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public abstract class InfoHandler extends DefaultHandler 
{
	private static final Logger LOG = CMSLoggerFactory.getLogger(
			InfoHandler.class);
	
//	private Locator my_sax_locator;
	private boolean my_fields_elem = false;
	
//	@Override
//	public void setDocumentLocator(Locator locator) 
//	{
//		my_sax_locator = locator;
//	}
 
	public final void startElement(String uri, String localName, String qName, 
                Attributes attributes) throws SAXException 
    {
		boolean unexpected_element = false;
		
		if (qName.equalsIgnoreCase(InfoDocument.XML_ELEMENT_FIELDS)) 
		{
			my_fields_elem = true;
			handleFieldsAttributes(attributes);
		} 
		else if (my_fields_elem) 
		{
			unexpected_element = !startUnknownFieldsElement(
					uri, localName, qName, attributes);
		} 
		else 
		{
			unexpected_element = true;
		}
		
		if (unexpected_element) {
			LOG.warning("Encountered unexpected element: " + qName);
		}
 
	}
	
	public abstract void handleFieldsAttributes(Attributes attr);
	
	public boolean startUnknownFieldsElement(String uri, String localName,
			String qName, Attributes attr) 
	{
		// Do nothing...
		return false;
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
