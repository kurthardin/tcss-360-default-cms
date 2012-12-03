/*
 * UsersDocumentTest.java
 * Kurt Hardin
 * 12-01-2012
 */

package edu.uwt.tcss360.Default.test.xml;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.UsersDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class UsersDocumentTest extends CMSDDocumentTest 
{
	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.UsersDocument#UsersDocument(java.io.File)}.
	 */
	@Test
	public final void testUsersDocument() 
	{
		// Confirm that the UsersDocument() constructor writes
		// an XML document with the single root element "users".
		new UsersDocument(getOutputFile())
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><users/>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.UsersDocument#addUser(edu.uwt.tcss360.Default.model.User)}.
	 */
	@Test
	public final void testAddUser() 
	{
		// Test adding a single valid user.
		new UsersDocument(getOutputFile())
		.addUser(new User("user1"))
		.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><users><user my_email=\"user1\"" +
				" my_id=\"user1\" my_name=\"\"/></users>";
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test adding a null user.
		User null_user = null;
		new UsersDocument(getOutputFile())
		.addUser(null_user)
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><users/>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test adding multiple valid users.
		new UsersDocument(getOutputFile())
		.addUser(new User("user1"))
		.addUser(new User("user2"))
		.addUser(new User("user3"))
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><users><user my_email=\"user1\"" +
				" my_id=\"user1\" my_name=\"\"/><user my_email=\"user2\"" +
				" my_id=\"user2\" my_name=\"\"/><user my_email=\"user3\"" +
				" my_id=\"user3\" my_name=\"\"/></users>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Test adding multiple valid users and one null user.
		new UsersDocument(getOutputFile())
		.addUser(new User("user1"))
		.addUser(null_user)
		.addUser(new User("user3"))
		.write();
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><users><user my_email=\"user1\"" +
				" my_id=\"user1\" my_name=\"\"/><user my_email=\"user3\"" +
				" my_id=\"user3\" my_name=\"\"/></users>";
		test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			assertEquals("Incorrect data in XML test file", 
					expected, test_reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
