/*
 * UserTest.java
 * 11-16-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.User;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class UserTest 
{

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
	 * @editor Travis Lewis
	 */
	@SuppressWarnings("unused")
    @Test
	public final void testUserStringStringForNullEmail() {
		String testName = "Test User";
		User testUser = null;
		boolean result = false;
		try {
		    testUser = new User(null, testName);
		} catch (IllegalArgumentException e) {
		    result = true;
		}
		assertTrue("null email exception", result);
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 * @editor Travis Lewis
	 */
	@SuppressWarnings("unused")
    @Test
	public final void testUserStringStringForNullName() {
		String testEmail = "test_user@testing.com";
		boolean result = false;
		User testUser = null;
		try{
		    testUser = new User(testEmail, null);
		} catch(IllegalArgumentException e) {
		    result = true;
		}
		assertTrue("null name exception", result);
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.User#User(java.lang.String, java.lang.String)}.
	 * @editor Travis Lewis
	 */
	@Test
	public final void testUserStringStringForNullEmailAndName() {
	    boolean result = false;
	    try {
	        new User(null, null);
	    } catch (IllegalArgumentException e) {
	        result = true;
	    }
	    assertTrue("null email and name", result);
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
