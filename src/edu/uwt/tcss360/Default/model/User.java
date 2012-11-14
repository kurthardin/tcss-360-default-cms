package edu.uwt.tcss360.Default.model;

import java.io.File;

public class User 
{
	/////////////
	// FIELDS
	/////////////
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
		
	}
	
	public User(final String the_email) 
	{
		my_email = the_email;
		
		my_id = my_email; //weren't we going to make that the case?
	}
	
	public User(final String the_email, final String the_name) 
	{
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
