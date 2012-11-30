/**
 * PaperTest.java
 * Scott Sanderson
 * 11/18/2012
 */

package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.Review;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * 
 * @author Scott Sanderson
 * @version 1.0
 */
public class PaperTest 
{

	@Test
	public void testPaperInitialization() 
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		assertFalse("Did not initialize", p == null);
		assertEquals("Author ID doesn't match", s, p.getAuthorID());
	}
	
	@Test
	public void testPaperEmptyReviews()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		assertEquals("Review list not Empty", 0, p.getReviews().size());
	}
	
	@Test
	public void testPaperNotEmptyReviews()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		p.addReview(new Review(dir, "test2@test.com", doc, 3));
		assertEquals("Review list size incorrect", 1, p.getReviews().size());
		p.addReview(new Review(dir, "test3@test.com", doc, 2));
		assertEquals("Review list size incorrect", 2, p.getReviews().size());
		p.addReview(new Review(dir, "test4@test.com", doc, 5));
		assertEquals("Review list size incorrect", 3, p.getReviews().size());
	}
	
	@Test
	public void testPaperRoles()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		p.assignReviewer("testReviewer@test.com");
		p.assignSubprogramChair("testSubprogramChair@test.com");
		
		assertEquals("Author doesnt match ID", 
				Role.AUTHOR, p.getRole("test@test.com"));
		assertEquals("Reviewer doesnt match ID", 
				Role.REVIEWER, p.getRole("testReviewer@test.com"));
		assertEquals("Subprogram Chair doesnt match ID", 
				Role.SUBPROGRAM_CHAIR, p.getRole("testSubprogramChair@test.com"));
		assertEquals("Not User Role", 
				Role.USER, p.getRole("testUser@test.com"));
	}
	
	@Test
	public void testPaperAuthor()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		
		assertEquals("Author doesnt match ID", 
				"test@test.com", p.getAuthorID());
	}
	
	@Test
	public void testPaperSubprogram()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		p.assignSubprogramChair("testSubprogramChair@test.com");
		assertEquals("Author doesnt match ID", 
				"testSubprogramChair@test.com", p.getSubprogramChairID());
	}
	
	@Test
	public void testPaperReviewers()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		p.assignReviewer("test2@test.com");
		p.assignReviewer("test3@test.com");
		p.assignReviewer("test4@test.com");
		List<String> list = p.getUserIDs(Role.REVIEWER);
		assertTrue("Reviewer doesn't match ID", list.contains("test2@test.com"));
		assertTrue("Reviewer doesn't match ID", list.contains("test3@test.com"));
		assertTrue("Reviewer doesn't match ID", list.contains("test4@test.com"));
	}
	
	@Test
	public void testPaperAcceptanceStatus()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		//there was no ACCEPT or DECLINE status in paper so I used 0 and 1
		p.setAcceptanceStatus(0);
		assertEquals("Incorrect Paper Status", 0, p.getAcceptanceStatus());
		p.setAcceptanceStatus(1);
		assertEquals("Incorrect Paper Status", 1, p.getAcceptanceStatus());
	}
	
	@Test
	public void testPaperTitle()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Paper p = new Paper(s, "testtitle", doc, dir);
		
		p.setTitle("testTitle");
		assertEquals("Title incorrectly set", "testTitle", p.getTitle());
	}
	
	
}
