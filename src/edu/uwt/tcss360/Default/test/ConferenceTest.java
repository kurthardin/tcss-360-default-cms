/*
 * ConferenceTest.java
 * Brett Cate
 * 11-21-2012
 */

package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * @author Brett Cate
 */
public class ConferenceTest {
	
	public static Date INITIAL_DATE;
	
	public static Date FINAL_DATE;
	
	public static Date EARLIER_DATE;
	
	public Conference TEST_CONFERENCE;

	
	public ConferenceTest()
	{
		try
		{
		INITIAL_DATE = Conference.CONFERENCE_DATE_FORMAT.parse("12-10-2012");
		FINAL_DATE = Conference.CONFERENCE_DATE_FORMAT.parse("12-10-2012");
		EARLIER_DATE = Conference.CONFERENCE_DATE_FORMAT.parse("12-9-2012");
		}
		catch(ParseException p)
		{
			// do nothing
		}
		
		TEST_CONFERENCE = new Conference("program_chair22", 
				"PNW Risk Forum", INITIAL_DATE, FINAL_DATE);
	}
	

	@Test
	public void testConstructor() 
	{
		assertEquals("The names are not equal.", TEST_CONFERENCE.getname(), 
				"PNW Risk Forum");
		assertEquals("The start dates are not equal.", 
				TEST_CONFERENCE.getStartDate(), INITIAL_DATE);
		assertEquals("The end dates are not equal.", 
				TEST_CONFERENCE.getEndDate(), FINAL_DATE);
		
	}
	
	@Test (expected  = IllegalArgumentException.class)
	public void testConstructorWithStartDatAfterEndDate() {
		new Conference("PC name", "Conf Name", INITIAL_DATE, EARLIER_DATE);
	}

	@Test
	public void testGetName() 
	{
		assertEquals("The names are not equal.", TEST_CONFERENCE.getname(), 
				"PNW Risk Forum");
	}
	
	@Test
	public void testGetStartDate() 
	{
		assertEquals("The start dates are not equal.", 
				TEST_CONFERENCE.getStartDate(), INITIAL_DATE);
	}
	
	@Test
	public void testGetEndDate() 
	{
		assertEquals("The end dates are not equal.", 
				TEST_CONFERENCE.getEndDate(), FINAL_DATE);
	}
	
	@Test
	public void testSubmissionDeadline() 
	{
		TEST_CONFERENCE.setSubmissionDeadline(FINAL_DATE);
		assertEquals("The submission deadlines are not equal.", 
				TEST_CONFERENCE.getSubmissionDeadline(), FINAL_DATE);
	}
	
	@Test
	public void testReviewDeadline()
	{
		TEST_CONFERENCE.setReviewDeadline(FINAL_DATE);
		assertEquals("The review deadlines are not equal.", 
				TEST_CONFERENCE.getReviewDeadline(), FINAL_DATE);
	}
	
	@Test
	public void testRecommendationDeadline()
	{
		TEST_CONFERENCE.setRecommendationDeadline(FINAL_DATE);
		assertEquals("The recommendation deadlines are not equal.", 
				TEST_CONFERENCE.getRecommendationDeadline(), FINAL_DATE);
	}
	
	@Test
	public void testFinalReviewDeadline()
	{
		TEST_CONFERENCE.setFinalRevisionDeadline(FINAL_DATE);
		assertEquals("The final review deadlines are not equal.", 
				TEST_CONFERENCE.getFinalRevisionDeadline(), FINAL_DATE);
	}
	
	@Test
	public void testAuthorizeUser()
	{
		User bob = new User("Bob");
		assertTrue("The user was not properly authorized " +
				"as a Subprogram Chair.", 
				TEST_CONFERENCE.authorizeUser(bob.getID(), 
						Role.SUBPROGRAM_CHAIR));
		assertFalse("The authorizeUser method did not return false when " +
				"a user was authorized multiple times for the same role.",
				TEST_CONFERENCE.authorizeUser(bob.getID(), 
						Role.SUBPROGRAM_CHAIR));
		assertTrue("The user was not properly authorized as a Program Chair.", 
				TEST_CONFERENCE.authorizeUser(bob.getID(), Role.PROGRAM_CHAIR));
		assertTrue("The user was not properly authorized as an Author.", 
				TEST_CONFERENCE.authorizeUser(bob.getID(), Role.AUTHOR));
		assertTrue("The user was not properly authorized as a Reviewer.", 
				TEST_CONFERENCE.authorizeUser(bob.getID(), Role.REVIEWER));
		assertTrue("The user was not properly authorized as an User.", 
				TEST_CONFERENCE.authorizeUser(bob.getID(), Role.USER));
		
	}
	
	@Test
	public void testDeauthorizeUser()
	{
		User bob = new User("Bob");
		TEST_CONFERENCE.authorizeUser(bob.getID(), Role.SUBPROGRAM_CHAIR);
		TEST_CONFERENCE.authorizeUser(bob.getID(), Role.AUTHOR);
		TEST_CONFERENCE.authorizeUser(bob.getID(), Role.PROGRAM_CHAIR);
		TEST_CONFERENCE.authorizeUser(bob.getID(), Role.REVIEWER);
		assertTrue("The user was not properly deauthorized " +
				"as a Subprogram Chair.",
				TEST_CONFERENCE.deauthorizeUser(bob.getID(), 
						Role.SUBPROGRAM_CHAIR));
		assertFalse("The deauthorizeUser method did not return false when " +
				"a user was deauthorized multiple times for the same role.",
				TEST_CONFERENCE.deauthorizeUser(bob.getID(), 
						Role.SUBPROGRAM_CHAIR));
		assertTrue("The user was not properly deauthorized as an Author.",
				TEST_CONFERENCE.deauthorizeUser(bob.getID(), Role.AUTHOR));
		assertTrue("The user was not properly deauthorized as a Reviewer.",
				TEST_CONFERENCE.deauthorizeUser(bob.getID(), Role.REVIEWER));
	}
	
	@Test
	public void testGetUserIds()
	{
		TEST_CONFERENCE.authorizeUser("Joe", Role.AUTHOR);
		assertTrue("The User List returned does not contain the correct users.", 
				TEST_CONFERENCE.getUserIds(Role.AUTHOR).contains("Joe"));
		TEST_CONFERENCE.authorizeUser("Joe", Role.PROGRAM_CHAIR);
		assertTrue("The User List returned does not contain the correct user.", 
				TEST_CONFERENCE.getUserIds(Role.PROGRAM_CHAIR).contains("Joe"));
	}
	
	@Test
	public void testPapers() 
	{
		User daniel = new User("Daniel");
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper test_paper = new Paper(s, "testtitle", doc, dir);
		TEST_CONFERENCE.authorizeUser(daniel.getID(), Role.AUTHOR);
		TEST_CONFERENCE.addPaper(test_paper.getAuthorID(), test_paper);
		assertTrue("The Paper List returned does not contain the correct paper.",
				TEST_CONFERENCE.getPapers(test_paper.getAuthorID(),
				Role.AUTHOR).contains(test_paper));
	}
	
	@Test
	public void testGetConferenceID()
	{
		System.out.println(TEST_CONFERENCE.getID());
		assertEquals("The Conference IDs should be equal.", 
				TEST_CONFERENCE.getID(), 
				"PNW Risk Forum12-10-201212-10-2012");
	}
	
}
