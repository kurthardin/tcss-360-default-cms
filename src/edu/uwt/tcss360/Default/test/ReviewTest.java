/**
 * PaperTest.java
 * Scott Sanderson
 * 11/18/2012
 */

package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import edu.uwt.tcss360.Default.model.Review;

public class ReviewTest 
{

	@Test
	public void testReviewInitialization() 
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Review r = new Review(dir, s, doc, 5);
		assertFalse("Did not initialize", r == null);
		assertEquals("ID doesn't match", s, r.getReviewerID());
		assertEquals("Rating doesn't match", 5, r.getSummaryRating());
	}
	
	
	@Test
	public void testReviewSummaryRating()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Review r = new Review(dir, s, doc, 5);
		r.setSummaryRating(3);
		assertEquals("Rating doesn't match", 3, r.getSummaryRating());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReviewNullDirectory()
	{
		File doc = new File("\\");
		String s = "test@test.com";
		Review r = new Review(null, s, doc, 5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReviewNullDocument()
	{
		File dir = new File("\\");
		String s = "test@test.com";
		Review r = new Review(dir, s, null, 5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReviewNullReviewerID()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		Review r = new Review(dir, null, doc, 5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReviewImproperHighRating()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Review r = new Review(dir, s, doc, 6);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReviewImproperLowRating()
	{
		File dir = new File("\\");
		File doc = new File("\\");
		String s = "test@test.com";
		Review r = new Review(dir, s, doc, 0);
	}

}
