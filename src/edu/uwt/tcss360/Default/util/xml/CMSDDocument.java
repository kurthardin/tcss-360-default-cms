/*
 * CMSDDocument.java
 * 11-30-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.util.xml;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import edu.uwt.tcss360.Default.util.log.CMSLoggerFactory;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public abstract class CMSDDocument 
{
	private static final Logger LOG = CMSLoggerFactory.getLogger(
			CMSDDocument.class);
	
	private File my_output_file;
	protected Document my_document;
	
	public CMSDDocument(File the_output_file) 
	{
		if (the_output_file == null) {
			throw new IllegalArgumentException(
					"The output file cannot be null");
		}
		
		if (the_output_file.isDirectory()) {
			throw new IllegalArgumentException(
					"The output file cannot be an existing directory");
		}
		
		my_output_file = the_output_file;
		try {
			// Create the XML document
			DocumentBuilderFactory doc_factory = 
					DocumentBuilderFactory.newInstance();
			DocumentBuilder doc_builder = doc_factory.newDocumentBuilder();
			my_document = doc_builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not create CMSDDocument", e);
		}
	}
	
	/**
	 * 
	 * @param the_xml
	 * @param the_data_file
	 * @throws TransformerException 
	 */
	public final void write() 
	{
		StreamResult result = new StreamResult(my_output_file);

		TransformerFactory transformerFactory = 
				TransformerFactory.newInstance();
		try 
		{
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(my_document);
			transformer.transform(source, result);
		} 
		catch (TransformerConfigurationException e) 
		{
			LOG.severe("Failed to write xml to file: " + 
					my_output_file.getName());
		} 
		catch (TransformerException e) 
		{
			LOG.severe("Failed to write xml to file: " + 
					my_output_file.getName());
		}
	}
	
}
