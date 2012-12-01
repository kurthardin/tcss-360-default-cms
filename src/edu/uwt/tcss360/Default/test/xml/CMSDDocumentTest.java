/**
 * 
 */
package edu.uwt.tcss360.Default.test.xml;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class CMSDDocumentTest 
{
	private File my_output_file = new File("test.xml");
	
	public File getOutputFile() {
		return my_output_file;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		my_output_file.delete();
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.CMSDDocument#write()}.
	 */
	@Test
	public final void testWrite() 
	{
		// Test writing to a file that doesn't exist
		my_output_file.delete();
		new InfoDocument(my_output_file)
		.setField("name", "value")
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields name=\"value\"/>";
		BufferedReader test_reader = FileHelper.getFileReader(my_output_file);
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Test overwriting an existing file
		new InfoDocument(my_output_file)
		.setField("name", "new_value")
		.setField("another_name", "another_value")
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields another_name=\"another_value\"" +
				" name=\"new_value\"/>";
		test_reader = FileHelper.getFileReader(my_output_file);
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
