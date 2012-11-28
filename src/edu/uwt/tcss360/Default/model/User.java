package edu.uwt.tcss360.Default.model;

import java.io.File;

public class User 
{

	/////////////
	// CONSTANTS
	/////////////
	
	// XML element names
	public static final String XML_ELEMENT_USER = "user";
	public static final String XML_ELEMENT_USERS = "users";
	
	// XML attribute names
	public static final String XML_ATTR_MY_NAME = "my_name";
	public static final String XML_ATTR_MY_ID = "my_id";
	public static final String XML_ATTR_MY_EMAIL = "my_email";
	
	/////////////
	// FIELDS
	/////////////
    
    //how is this going to be formatted?
	private String my_name;
	
	private String my_id;
	private String my_email;
	
	public enum Role
	{
		PROGRAM_CHAIR, SUBPROGRAM_CHAIR, REVIEWER, AUTHOR, USER;

		/**
		 * 
		 * @author Kurt Hardin
		 */
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			String [] words = name().split("\\_");
			for (int i = 0; i < words.length; i++) 
			{
				if (i > 0) 
				{
					result.append(" ");
				}
				String word = words[i].toLowerCase();
				result.append(Character.toUpperCase(word.charAt(0)))
		        .append(word.substring(1));
			}
			return result.toString();
		}
	}
	
	/////////////
	// CONSTRUCTORS
	/////////////
	
	//TODO: we probably won't use this, since user information is going to
	//be stored all in one file...
	public User(File the_user_file) 
	{
		if(the_user_file == null)
		    throw new IllegalArgumentException("User File cannot be null");
	}
	
	public User(final String the_email) 
	{
	    if(the_email == null)
	        throw new IllegalArgumentException("Email cannot be null");
	        
		my_email = the_email;
		
		my_id = my_email; //weren't we going to make that the case?
	}
	
	public User(final String the_email, final String the_name) 
	{
	    if(the_email == null)
	        throw new IllegalArgumentException("Email cannot be null");
	    if(the_name == null)
	        throw new IllegalArgumentException("Name cannot be null");
	        
		my_email = the_email;
		my_name = the_name;
		
		my_id = my_email; //weren't we going to make that the case?
	}
	
	/////////////
	// METHODS
	/////////////
	public String getID() 
	{
		return my_id;
	}
	
	public String getEmail() 
	{ 
		return my_email; 
	}
	
	public String getName() 
	{ 
		return my_name; 
	}
}
