/*
 * CurrentStateTest.java
 * Kurt Hardin
 * 11-16-2012
 */

package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;

/**
 * Unit tests for CurrentState methods.
 * @author Kurt Hardin
 */
public class CurrentStateTest 
{
	
	private final ConferencesManager my_confs_mgr = new ConferencesManager();
	private CurrentState my_curr_state = new CurrentState(my_confs_mgr);
	private Conference my_test_conf;
	private User my_test_user;
	private Paper my_test_paper;
	
	/**
	 * @editor Scott Sanderson
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		// Add User for testing
		String testUserId = "test_user@testing.com";
		my_test_user = new User(testUserId, "Test User");
		my_confs_mgr.addUser(my_test_user);
		
		// Add Conference for testing
		my_test_conf = new Conference(my_test_user.getID(), 
				"Test Conference", new Date());
		my_confs_mgr.addConference(my_test_conf);
		
		// Add Paper for testing
		File testPaperDirectory = new File("./testPaperDir");
		if (!testPaperDirectory.exists()) {
			testPaperDirectory = FileHelper.createDirectory(
					new File("."), "testPaperDir");
		}
		File testPaperFile = new File("./testPaperDir/testPaper.docx");
		if (!testPaperFile.exists()) {
			testPaperFile = FileHelper.createFile(
					testPaperDirectory, "testPaper.docx");
		}
		my_test_paper = new Paper(my_test_user.getID(), "testName", 
				testPaperFile, testPaperDirectory);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(new File("./testPaperDir"));
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#CurrentState(edu.uwt.tcss360.Default.model.ConferencesManager)}.
	 */
	@Test
	public final void testCurrentStateForValidConferencesManager() 
	{
		my_curr_state = new CurrentState(my_confs_mgr);
		assertEquals("ConferencesManager not set correctly", 
				my_confs_mgr, my_curr_state.getConferencesManager());
		assertNull("Current Conference not null", 
				my_curr_state.getCurrentConference());
		assertNull("Current Paper not null", 
				my_curr_state.getCurrentPaper());
		assertNull("Current Role not null", 
				my_curr_state.getCurrentRole());
		assertNull("Current User not null", 
				my_curr_state.getCurrentUser());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#CurrentState(edu.uwt.tcss360.Default.model.ConferencesManager)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testCurrentStateForNullConferencesManager() 
	{
		new CurrentState(null);
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#getConferencesManager()}.
	 */
	@Test
	public final void testGetConferencesManager() 
	{
		// Check that the same ConferencesManager that my_curr_state
		// was created with is returned.
		assertEquals("Returned ConferencesManager invalid", 
				my_confs_mgr, my_curr_state.getConferencesManager());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#getCurrentConference()}.
	 * @editor Scott Sanderson
	 */
	@Test
	public final void testGetCurrentConference() 
	{
		// Check that null is returned when there is no current Conference
		assertNull("getCurrentConference() return value not null",
				my_curr_state.getCurrentConference());
		
		// Set current conference ID and check that the same ID is returned
		my_curr_state.setCurrentConference(my_test_conf.getID());
		assertEquals("getCurrentConference() returned invalid id", 
				my_test_conf.getID(), 
				my_curr_state.getCurrentConference().getID());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#getCurrentUser()}.
	 */
	@Test
	public final void testGetCurrentUser() 
	{
		// Check that null is returned when there is no current User
		assertNull("getCurrentUser() return value not null",
				my_curr_state.getCurrentUser());
		
		// Set current user ID and check that the same ID is returned
		my_curr_state.setCurrentUser(my_test_user.getID());
		assertEquals("getCurrentUser() returned invalid id", 
				my_test_user, my_curr_state.getCurrentUser());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#getCurrentRole()}.
	 */
	@Test
	public final void testGetCurrentRole() 
	{
		// Check that null is returned when there is no current Role
		assertNull("getCurrentRole() return value not null",
				my_curr_state.getCurrentRole());
		
		// Set current Role and check that the same Role is returned
		Role dummyRole = Role.USER;
		my_curr_state.setCurrentRole(dummyRole);
		assertEquals("getCurrentRole() returned invalid Role", 
				dummyRole, my_curr_state.getCurrentRole());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#getCurrentPaper()}.
	 */
	@Test
	public final void testGetCurrentPaper() 
	{
		// Check that null is returned when there is no current Paper
		assertNull("getCurrentPaper() return value not null",
				my_curr_state.getCurrentPaper());
		
		// Set current Paper and check that the same Paper is returned
		my_curr_state.setCurrentPaper(my_test_paper);
		assertEquals("getCurrentPaper() returned invalid Paper", 
				my_test_paper, my_curr_state.getCurrentPaper());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#setCurrentConference(java.lang.String)}.
	 * @editor Scott Sanderson
	 */
	@Test
	public final void testSetCurrentConference() 
	{
		// Set current Conference and check that the same Conference is returned
		my_curr_state.setCurrentConference(my_test_conf.getID());
		assertEquals("invalid Conference returned after setCurrentConference()", 
				my_test_conf.getID(), my_curr_state.getCurrentConference().getID());
		
		// Set current Conference with invalid ID and check for null return
		my_curr_state.setCurrentConference("invalid_conf_id");
		assertNull("null should be returned after setCurrentConference()", 
				my_curr_state.getCurrentConference());
		
		// Set current Conference to a valid Conference, then reset with 
		// null ID and check that null is returned.
		my_curr_state.setCurrentConference(my_test_conf.getID());
		my_curr_state.setCurrentConference(null);
		assertNull("null should be returned after setCurrentConference()", 
				my_curr_state.getCurrentConference());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#setCurrentUser(java.lang.String)}.
	 */
	@Test
	public final void testSetCurrentUser() 
	{
		// Set current User and check that the same User is returned
		my_curr_state.setCurrentUser(my_test_user.getID());
		assertEquals("invalid User returned after setCurrentUser()", 
				my_test_user, my_curr_state.getCurrentUser());
		
		// Set current User with invalid ID and check for null return
		my_curr_state.setCurrentUser("invalid_user_id");
		assertNull("null should be returned after setCurrentUser()", 
				my_curr_state.getCurrentUser());
		
		// Set current User to a valid User, then reset with 
		// null ID and check that null is returned.
		my_curr_state.setCurrentUser(my_test_user.getID());
		my_curr_state.setCurrentUser(null);
		assertNull("null should be returned after setCurrentUser()", 
				my_curr_state.getCurrentUser());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#setCurrentRole(edu.uwt.tcss360.Default.model.User.Role)}.
	 */
	@Test
	public final void testSetCurrentRole() 
	{
		// Set current Role and check that the same Role is returned
		my_curr_state.setCurrentRole(Role.AUTHOR);
		assertEquals("Invalid Role returned after setCurrentRole()", 
				Role.AUTHOR, my_curr_state.getCurrentRole());
		
		// Set current Role to null and check that the return is null
		my_curr_state.setCurrentRole(null);
		assertNull("Invalid Paper returned after setCurrentRole()", 
				my_curr_state.getCurrentRole());
	}

	/**
	 * Test method for {@link edu.uwt.tcss360.Default.model.CurrentState#setCurrentPaper(edu.uwt.tcss360.Default.model.Paper)}.
	 */
	@Test
	public final void testSetCurrentPaper() 
	{
		// Set current Paper and check that the same Paper is returned
		my_curr_state.setCurrentPaper(my_test_paper);
		assertEquals("Invalid Paper returned after getCurrentPaper()", 
				my_test_paper, my_curr_state.getCurrentPaper());
		
		// Set current Paper to null and check that the return is null
		my_curr_state.setCurrentPaper(null);
		assertNull("Invalid Paper returned after getCurrentPaper()", 
				my_curr_state.getCurrentPaper());
	}

}
