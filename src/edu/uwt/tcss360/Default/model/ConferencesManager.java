package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.UsersDocument;
import edu.uwt.tcss360.Default.util.xml.parsers.CMSDParser;
import edu.uwt.tcss360.Default.util.xml.parsers.UsersHandler;

/**
 * 
 * @author Brett Cate
 */
public class ConferencesManager 
{

	/** A set of all the conferences. */
	private Set<Conference> my_conferences;
	
	/** A set of all the users. */
	private final Set<User> my_users = new HashSet<User>();
	
	/**
	 * Constructs a ConferencesManager object.
	 * @author Kurt Hardin
	 */
	public ConferencesManager() 
	{	
		initUsers();
		initConfs();
		// TODO Write unit tests for ConferencesManager()
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	private void initUsers() 
	{	
		File input_file = new File(FileHelper.getDataDirectory(), 
				FileHelper.USERS_DATA_FILE_NAME);
		UsersHandler handler = new UsersHandler(this);
		new CMSDParser(input_file, handler).parse();
//		File data_dir = FileHelper.getDataDirectory();
//		if (data_dir != null) {
//			
//			// Create users data file if it doesn't exist
//			FileHelper.createFile(data_dir, FileHelper.USERS_DATA_FILE_NAME);
//			
//			// Get input source for users data file
//			InputSource info = FileHelper.getInputSource(data_dir,
//					FileHelper.USERS_DATA_FILE_NAME);
//			
//			if(info == null) 
//			{
//			    throw new IllegalArgumentException(
//			    		FileHelper.USERS_DATA_FILE_NAME + 
//			    		" could not be found");
//			}
//			else
//			{	
//				try 
//				{
//					SAXParserFactory factory = SAXParserFactory.newInstance();
//					SAXParser saxParser = factory.newSAXParser();
//					saxParser.parse(info, new UsersHandler(this));
//				} 
//				catch (SAXException e) 
//				{
//					e.printStackTrace();
//				} 
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//				catch (ParserConfigurationException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	private void initConfs() 
	{
		File confs_dir = FileHelper.getConferencesDirectory();
		if (confs_dir != null) 
		{
			List<String> conf_dir_names = 
					FileHelper.getSubdirectoryNames(confs_dir);
			my_conferences = new HashSet<Conference>(conf_dir_names.size());

			for (String conf_dir_name : conf_dir_names) 
			{
				File conf_dir = new File(confs_dir, conf_dir_name);
				
				Conference conference = new Conference(conf_dir);
				my_conferences.add(conference);
			}
		}
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	public void writeData() 
	{
		writeUsers();
		writeConferences();
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	private void writeUsers() 
	{
		File output_file = new File(FileHelper.getDataDirectory(), 
				FileHelper.USERS_DATA_FILE_NAME);
		UsersDocument doc = new UsersDocument(output_file);
		for (User user : my_users) 
		{
			doc.addUser(user);	
		}
		doc.write();
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 */
	private void writeConferences() 
	{
		for (Conference conf : my_conferences) 
		{
			conf.writeData();
		}
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
	public boolean removeConference(final String the_conference_id) 
	{
		boolean was_conferenced_removed = false;
		for (Conference a_conference : my_conferences) 
		{
			if (a_conference.getID().equals(the_conference_id))
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
	public Conference getConference(final String the_conference_id)
	{
		Conference sought_conference = null;
		for (Conference a_conference : my_conferences) 
		{
			if (a_conference.getID().equals(the_conference_id))
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
		return Collections.unmodifiableSet(my_conferences);
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
			if (a_user.getID().equals(the_user_id))
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
		User sought_user = null;
		for (User a_user : my_users)
		{
			if(a_user.getID().equals(the_user_id))
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
