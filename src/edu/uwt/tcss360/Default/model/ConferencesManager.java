import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ConferencesManager 
{

	/** A set of all the conferences. */
	private Set<Conference> my_conferences;
	
	/** A set of all the users. */
	private Set<User> my_users;
	
	/**
	 * Constructs a ConferencesManager object.
	 */
	public ConferencesManager() 
	{
		my_conferences = new HashSet<Conference>();
		my_users = new HashSet<User>();
	}
	
	/**
	 * Adds the given conference to the collection of conferences.
	 * 
	 * @param the_conference The conference to be added.
	 */
	public void addConference(final Conference the_conference) 
	{
		if (the_conference == null) 
		{
			throw new IllegalArgumentException();
		}
		my_conferences.add(the_conference);
	}
	
	/**
	 * Removes the conference with the associated conference ID and returns true if 
	 * the conference exists in this ConferencesManager, false otherwise.
	 * 
	 * @param the_conference_id The ID of the conference to be removed.
	 * 
	 * @return True if the conference was removed, false otherwise.
	 */
	public boolean removeConference(final int the_conference_id) 
	{
		boolean was_conferenced_removed = false;
		for (Conference a_conference : my_conferences) 
		{
			if (a_conference.getConferenceID == the_conference_id)
			{
				my_conferences.remove(a_conference);
				was_conferenced_removed = true;
				break;
			}
		}
		return was_conferenced_removed;
	}
	
	/**
	 * Returns the Conference with the given ID, or null if it is not found.
	 * 
	 * @param the_conference_id The ID of the conference sought.
	 * 
	 * @return The conference with the given ID, or null if it was not found.
	 */
	public Conference getConference(final int the_conference_id)
	{
		Conference sought_conference;
		for (Conference a_conference : my_conferences) 
		{
			if (a_conference.getConferenceID == the_conference_id)
			{
				sought_conference = a_conference;
				break;
			}
		}
		return sought_conference;
	}
	
	/**
	 * Returns an unmodifiable Set of the conferences in this ConferencesManager.
	 * 
	 * @return An unmodifiable Set of the conferences in this ConferencesManager.
	 */
	public Set<Conference> getAllConferences() 
	{
		Collections.unmodifiableSet(my_conferences);
	}
	
	/**
	 * Adds the given User to this ConferencesManager.
	 * 
	 * @param the_user The user to be added.
	 * 
	 * @return True if the user was added, false otherwise.
	 */
	public boolean addUser(final User the_user)
	{
		return my_users.add(the_user);
	}
	
	/**
	 * Removes the user with the corresponding user ID given and returns true. If the User is not
	 * found, returns false instead.
	 * 
	 * @param the_user_id The ID of the user to be removed.
	 * 
	 * @return True if the User was found and removed, false otherwise.
	 */
	public boolean removeUser(final String the_user_id)
	{
		boolean was_user_removed = false;
		for (User a_user : my_users)
		{
			if (a_user.getId().equals(the_user_id))
			{
				was_user_removed = my_users.remove(a_user);
				break;
			}
		}
		return was_user_removed;
	}
	
	/**
	 * Returns the User with the corresponding User ID given, or null if the user was not found.
	 * 
	 * @param the_user_id The User ID for the sough User.
	 * 
	 * @return The User sought, or null if the User was not found.
	 */
	public User getUser(final String the_user_id)
	{
		User sought_user;
		for (User a_user : my_users)
		{
			if(a_user.getId.equals(the_user_id))
			{
				sought_user = a_user;
			}
		}
		return sought_user;
	}
	
	/**
	 * Returns an unmodifiable Set of the Users in this ConferencesManager.
	 * 
	 * @return An unmodifiable Set of the Users in this ConferencesManager.
	 */
	public Set<User> getAllUsers()
	{
		
		return Collections.unmodifiableSet(my_users);
	}
}
