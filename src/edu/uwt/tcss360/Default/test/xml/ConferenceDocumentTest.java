/*
 * ConferenceDocumentTest.java
 * 12-01-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.test.xml;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.ConferenceDocument;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class ConferenceDocumentTest extends CMSDDocumentTest 
{
	/**
	 * Test method for {@link edu.uwt.tcss360.Default.util.xml.ConferenceDocument#addMyUsersRolesField(java.util.Map)}.
	 */
	@Test
	public final void testAddMyUsersRolesField() 
	{
		
		// Test null map.
		Map<String, Set<Role>> users_roles = null;
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?><fields/>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
		
		
		// Test empty map.
		users_roles = new HashMap<String, Set<Role>>();
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?>" +
				"<fields>" +
					"<my_users_roles/>" +
				"</fields>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
		
		
		// Test single user in map with empty roles set
		Set<Role> user1_roles = new TreeSet<Role>();
		users_roles.put("user1", user1_roles);
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?>" +
				"<fields>" +
					"<my_users_roles>" +
						"<user id=\"user1\"/>" +
					"</my_users_roles>" +
				"</fields>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
		
		
		// Test single user in map with single role in set
		user1_roles.add(Role.USER);
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?>" +
				"<fields>" +
					"<my_users_roles>" +
						"<user id=\"user1\">" +
							"<role name=\"USER\"/>" +
						"</user>" +
					"</my_users_roles>" +
				"</fields>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
		
		
		// Test single user in map with multiple roles in set
		user1_roles.add(Role.AUTHOR);
		user1_roles.add(Role.REVIEWER);
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?>" +
				"<fields>" +
					"<my_users_roles>" +
						"<user id=\"user1\">" +
							"<role name=\"REVIEWER\"/>" +
							"<role name=\"AUTHOR\"/>" +
							"<role name=\"USER\"/>" +
						"</user>" +
					"</my_users_roles>" +
				"</fields>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
		
		
		// Test multiple users in map, one with empty roles set
		Set<Role> user2_roles = new TreeSet<Role>();
		Set<Role> user3_roles = new TreeSet<Role>();
		user3_roles.add(Role.PROGRAM_CHAIR);
		user3_roles.add(Role.SUBPROGRAM_CHAIR);
		users_roles.put("user2", user2_roles);
		users_roles.put("user3", user3_roles);
		new ConferenceDocument(getOutputFile())
		.addMyUsersRolesField(users_roles)
		.write();
		
		expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"" +
				" standalone=\"no\"?>" +
				"<fields>" +
					"<my_users_roles>" +
						"<user id=\"user2\"/>" +
						"<user id=\"user1\">" +
							"<role name=\"REVIEWER\"/>" +
							"<role name=\"AUTHOR\"/>" +
							"<role name=\"USER\"/>" +
						"</user>" +
						"<user id=\"user3\">" +
							"<role name=\"PROGRAM_CHAIR\"/>" +
							"<role name=\"SUBPROGRAM_CHAIR\"/>" +
						"</user>" + 
					"</my_users_roles>" +
				"</fields>";
		assertEquals("Incorrect data in XML test file", expected, readXml());
	}
	
	private String readXml() {
		BufferedReader test_reader = FileHelper.getFileReader(getOutputFile());
		try {
			return test_reader.readLine();
		} catch (IOException e) {
			return null;
		}
	}
}
