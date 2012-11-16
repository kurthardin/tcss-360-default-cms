package edu.uwt.tcss360.Default.model;

import java.io.File;

public class User 
{
	/////////////
	// FIELDS
	/////////////
    
    //how is this going to be formatted?
	private String my_name;
	
	private String my_id;
	private String my_email;
	
	public enum Role
	{
		PROGRAM_CHAIR, SUBPROGRAM_CHAIR, REVIEWER, AUTHOR, USER
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
