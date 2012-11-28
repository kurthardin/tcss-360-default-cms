package edu.uwt.tcss360.Default.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Kurt Hardin
 * @version 1.0
 */
public abstract class InfoHandler extends DefaultHandler {
	
	private boolean fieldsElem = false;
 
	public final void startElement(String uri, String localName, String qName, 
                Attributes attributes) throws SAXException 
    {
 
		System.out.println("Start Element: " + qName);
 
		if (qName.equalsIgnoreCase("fields")) 
		{
			fieldsElem = true;
			handleFieldsAttributes(attributes);
		} 
		else if (fieldsElem) 
		{
			startUnknownFieldsElement(uri, localName, qName, attributes);
		} else {
			System.out.println("Encountered unexpected element: " + qName);
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
		
		if (qName.equalsIgnoreCase("fields")) 
		{
			fieldsElem = false;
		} 
		else if (fieldsElem) 
		{
			endUnknownFieldsElement(uri, localName, qName);
		}
		
		System.out.println("End Element: " + qName);
 
	}
	
	public void endUnknownFieldsElement(String uri, String localName,
			String qName) throws SAXException 
	{
		// Do nothing...
	}

}
