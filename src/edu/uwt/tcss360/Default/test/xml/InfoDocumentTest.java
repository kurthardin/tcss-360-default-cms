/*
 * InfoDocumentTest.java
 * 12-01-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.test.xml;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class InfoDocumentTest extends CMSDDocumentTest 
{
	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.InfoDocument#setField(java.lang.String, int)}.
	 */
	@Test
	public final void testInfoDocument() 
	{
		// Confirm that the InfoDocument() constructor writes
		// an XML document with the single root element "fields".
		new InfoDocument(getOutputFile())
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields/>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.InfoDocument#setField(java.lang.String, int)}.
	 */
	@Test
	public final void testSetFieldStringInt() 
	{
		// Test with positive int
		new InfoDocument(getOutputFile())
		.setField("int_val", 5)
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields int_val=\"5\"/>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test with negative int
		new InfoDocument(getOutputFile())
		.setField("int_val", -5)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields int_val=\"-5\"/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test with maximum int value
		new InfoDocument(getOutputFile())
		.setField("int_val", Integer.MAX_VALUE)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields int_val=\"2147483647\"/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test with minimum int value
		new InfoDocument(getOutputFile())
		.setField("int_val", Integer.MIN_VALUE)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields int_val=\"-2147483648\"/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.InfoDocument#setField(java.lang.String, java.util.Date)}.
	 */
	@Test
	public final void testSetFieldStringDate() 
	{
		// TODO Test with valid Date
		Date date = new Date();
		new InfoDocument(getOutputFile())
		.setField("date", date)
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields date=\"" +
				Conference.CONFERENCE_DATE_FORMAT.format(date) + "\"/>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO Test with null Date
		date = null;
		new InfoDocument(getOutputFile())
		.setField("date", date)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.InfoDocument#setField(java.lang.String, java.io.File)}.
	 */
	@Test
	public final void testSetFieldStringFile() {
		// Test with valid File
		String filename = "test.xml";
		File file = new File(filename);
		new InfoDocument(getOutputFile())
		.setField("file", file)
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields file=\"" + filename + "\"/>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test with null File
		file = null;
		new InfoDocument(getOutputFile())
		.setField("file", file)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
