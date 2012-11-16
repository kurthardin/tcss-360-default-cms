/**
 * 
 */
package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.User;

/**
 * @author churlbong
 *
 */
public class UserTests {

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.io.File)}.
	 */
	@Test
	public final void testUserFile() {
		fail("Not yet implemented"); // TODO Add tests for User(File)
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String)}.
	 */
	@Test
	public final void testUserString() {
		String testUserEmail = "test_user@testing.com";
		User testUser = new User(testUserEmail);
		assertEquals("User email not correctly initialized",
				testUserEmail, testUser.getEmail());
		assertEquals("User ID not correctly initialized",
				testUserEmail, testUser.getID());
		assertNull("User name should be null", testUser.getName());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testUserStringStringForValidEmailAndName() {
		String testEmail = "test_user@testing.com";
		String testName = "Test User";
		User testUser = new User(testEmail, testName);
		assertEquals("User email not correctly initialized",
				testEmail, testUser.getEmail());
		assertEquals("User ID not correctly initialized",
				testEmail, testUser.getID());
		assertEquals("User name not correctly initialized", 
				testName, testUser.getName());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testUserStringStringForNullEmail() {
		String testName = "Test User";
		new User(null, testName);
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testUserStringStringForNullName() {
		String testEmail = "test_user@testing.com";
		User testUser = new User(testEmail, null);
		assertEquals("User email not correctly initialized",
				testEmail, testUser.getEmail());
		assertEquals("User ID not correctly initialized",
				testEmail, testUser.getID());
		assertNull("User name should be null", 
				testUser.getName());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testUserStringStringForNullEmailAndName() {
		new User(null, null);
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#getID()}.
	 */
	@Test
	public final void testGetIDForValidId() {
		String testUserEmail = "test_user@testing.com";
		User testUser = new User(testUserEmail);
		assertEquals("getID() returned invalid ID",
				testUserEmail, testUser.getID());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#getEmail()}.
	 */
	@Test
	public final void testGetEmailForValidEmail() {
		String testUserEmail = "test_user@testing.com";
		User testUser = new User(testUserEmail);
		assertEquals("getEmail() returned invalid email",
				testUserEmail, testUser.getEmail());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#getName()}.
	 */
	@Test
	public final void testGetNameForValidName() {
		String testEmail = "test_user@testing.com";
		String testName = "Test User";
		User testUser = new User(testEmail, testName);
		assertEquals("getName() returned invalid name", 
				testName, testUser.getName());
	}

}
