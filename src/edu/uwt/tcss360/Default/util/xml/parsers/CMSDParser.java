/*
 * CMSDParser.java
 * 11-30-2012
 * Kurt Hardin 
 */

package edu.uwt.tcss360.Default.util.xml.parsers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uwt.tcss360.Default.util.FileHelper;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class CMSDParser {
	
	private final File my_input_file;
	private final InputSource my_input_source;
	private final DefaultHandler my_handler;
	private final SAXParser my_sax_parser;
	
	public CMSDParser(final File the_input_file, 
			final DefaultHandler the_handler) 
	{
		my_input_file = the_input_file;
		
		// Get input source for users data file
		my_input_source = FileHelper.getInputSource(the_input_file);
		
		if(my_input_source == null) 
		{
		    throw new IllegalArgumentException(
		    		the_input_file.getName() + 
		    		" could not be found in " + the_input_file.getParent());
		}
		else
		{
			my_handler = the_handler;
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try {
				my_sax_parser = factory.newSAXParser();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException("Unable to create SAX parser", e);
			} catch (SAXException e)
			{
			    throw new RuntimeException("Unable to create SAX parser", e);
			}
		}
	}
	
	public void parse() {
		try {
			my_sax_parser.parse(my_input_source, my_handler);
		} catch (SAXException e) {
			System.out.println("Parsing failed for " + my_input_file.getName());
		} catch (IOException e)
		{
		    System.out.println("Parsing failed for " + my_input_file.getName());
		}
	}
	
}
