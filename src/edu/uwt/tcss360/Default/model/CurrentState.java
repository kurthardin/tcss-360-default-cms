/**
 * Conference.java
 * Scott Sanderson
 * 11/09/2012
 */

package edu.uwt.tcss360.Default.model;

import edu.uwt.tcss360.Default.model.User.Role;

/**
 * Class representing the current state for the Conference
 * Management System. It stores all of the current values
 * along with the ConferencesManager object.
 * @author Scott Sanderson
 * @version 1.0
 */
public class CurrentState 
{
	/*
	 * Fields
	 */
	
	/**
	 * The ConefencesManager object being used.
	 */
	private ConferencesManager my_conf_mgr;
	
	/**
	 * the current conference.
	 */
	private Conference my_current_conference;
	
	/**
	 * the current user.
	 */
	private User my_current_user;
	
	/**
	 * the current role of the current user.
	 */
	private Role my_current_role;
	
	/**
	 * the current paper.
	 */
	private Paper my_current_paper;
	
	
	/*
	 * Methods
	 */
	
	/**
	 * Constructor for the CurrentState class. It is passed in a
	 * ConferencesManager object to use.
	 * @param the_conf_mgr
	 */
	public CurrentState(ConferencesManager the_conf_mgr)
	{
		my_conf_mgr = the_conf_mgr;
	}
	
	/**
	 * Returns the ConferencesManager object
	 * @return the conferences manager
	 */
	public ConferencesManager getConferencesManager()
	{
		return my_conf_mgr;
	}
	
	/**
	 * Returns the current conference object.
	 * @return the current conference
	 */
	public Conference getCurrentConference()
	{
		return my_current_conference;
	}
	
	/**
	 * Returns the current user object.
	 * @return the current user
	 */
	public User getCurrentUser()
	{
		return my_current_user;
	}
	
	/**
	 * Returns the current role of the current user.
	 * @return the current role
	 */
	public Role getCurrentRole()
	{
		return my_current_role;
	}
	
	/**
	 * Returns the current paper
	 * @return the current paper
	 */
	public Paper getCurrentPaper()
	{
		return my_current_paper;
	}
	
	/**
	 * Sets the current conference to the_conference
	 * @param the_conference the if of the conference sent in
	 */
	public void setCurrentConference(int the_conf_id)
	{
		my_current_conference = my_conf_mgr.getConference(the_conf_id);
	}
	
	/**
	 * sets the current user to the user specified by the
	 * string identifier passed in.
	 * @param the_user_id the login ID of the user.
	 */
	public void setCurrentUser(String the_user_id)
	{
		my_current_user = my_conf_mgr.getUser(the_user_id);
	}
	
	/**
	 * sets the current role for the current user.
	 * @param the_role the role being passed in
	 */
	public void setCurrentRole(Role the_role)
	{
		my_current_role = the_role;
	}
	
	/**
	 * sets the current paper to the passed in paper.
	 * @param the_paper the paper passed in.
	 */
	public void setCurrentPaper(Paper the_paper)
	{
		my_current_paper = the_paper;
	}
}
