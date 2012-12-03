package edu.uwt.tcss360.Default.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.User;

/**
 * 
 * @author Brett Cate
 * @editor Kurt Hardin
 * @version 1.0
 */
public class ConferencesManagerTest
{
	private File data_dir = null;
	private File data_dir_backup = null;
	
	@Before
	public void setup() {
		data_dir = new File("data");
		if (data_dir.isDirectory()) {
			data_dir_backup = new File("data_bak");
			try {
				FileUtils.moveDirectory(data_dir, data_dir_backup);
			} catch (IOException e) {
				// Do nothing...
			}
		}
	}
	
	@After
	public void tearDown() {
		if (data_dir_backup != null) {
			try {
				FileUtils.deleteDirectory(data_dir);
				FileUtils.moveDirectory(data_dir_backup, data_dir);
			} catch (IOException e) {
				// Do nothing...
			}
		}
	}
	
    @Test
    public void emptySetTest()
    {
        ConferencesManager man = new ConferencesManager();
        Set<Conference> set = man.getAllConferences();
        assertTrue("emptySet", set.isEmpty());
    }
    
    @Test
    public void nonEmptySetTest()
    {
        ConferencesManager man = new ConferencesManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Conference c = new Conference("userid", "some conference",
                cal.getTime());
        man.addConference(c);
        Set<Conference> set = man.getAllConferences();
        assertFalse("nonEmptySet", set.isEmpty());
    }

    @Test
    public void addConferenceTest()
    {
        ConferencesManager man = new ConferencesManager();
        Date date = new Date();
        Conference c = new Conference("userid", "some conference",
                date);

        man.addConference(c);
        Set<Conference> set = man.getAllConferences();
        
        Conference got = set.iterator().next();
        
        assertEquals("addConference: same conference", got.getID(),
                c.getID());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void addNullConferenceTest()
    {
        ConferencesManager man = new ConferencesManager();
        man.addConference(null);
    }
    
    @Test
    public void getConferenceTest()
    {
        ConferencesManager man = new ConferencesManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Conference c = new Conference("userid", "some conference",
                cal.getTime());
        man.addConference(c);
        
        Conference got = man.getConference(c.getID());
        
        assertEquals("getConference: name", got.getname(), c.getname());
        assertEquals("getConference: end date", got.getEndDate(), 
                c.getEndDate());
        assertEquals("getConference: id", got.getID(),
                c.getID());
        
        Conference cc = new Conference("someid", "conferenceblah", 
        		cal.getTime());
        
        assertNull("getConference: doesn't exist", 
                man.getConference(cc.getID()));
    }
    
    @Test 
    public void removeConferenceTest()
    {
        ConferencesManager man = new ConferencesManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Conference c = new Conference("userid", "some conference",
                cal.getTime());
        Conference cc = new Conference("someid", "conferenceblah", 
        		cal.getTime());
        man.addConference(c);
        
        //try to remove a conference that doesn't exist
        assertFalse("removeConference: doesn't exist", 
                man.removeConference(cc.getID()));
        
        assertTrue("removeConference: remove", 
                man.removeConference(c.getID()));
        Set<Conference> set = man.getAllConferences();
        assertFalse("removeConference", set.contains(c));
    }
    
    @Test
    public void getAllConferencesTest()
    {
        ConferencesManager man = new ConferencesManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        
        Conference c1 = new Conference("user1","conference1", cal.getTime());
        Conference c2 = new Conference("user2","conference2", cal.getTime());
        Conference c3 = new Conference("user3","conference3", cal.getTime());
        
        man.addConference(c1);
        man.addConference(c2);
        man.addConference(c3);
        
        Set<Conference> set = man.getAllConferences();
        
        // test to see if the set has something in it
        assertFalse("getAllConferences: non-empty set", set.isEmpty());
        assertEquals("getAllConferences: set size = 3", set.size(), 3);
        
        // see if all the added conferences are in the set
        assertTrue("getAllConferences: contains 1st", set.contains(c1));
        assertTrue("getAllConferences: contains 2nd", set.contains(c2));
        assertTrue("getAllConferences: contains 3rd", set.contains(c3));
        
        // make sure a conference that isn't supposed to be in there isn't.
        Conference c4 = new Conference("user4","conference4", cal.getTime());
        assertFalse("getAllConferences: contains 4th", set.contains(c4));
    }
            
    @Test
    public void addGetUserTest()
    {
        ConferencesManager man = new ConferencesManager();
        User u = new User("Cancer Cowboy", "cjesperson@jpod.ca");
        man.addUser(u);

        assertNull("addGetUser: no user", man.getUser("asdf"));        
        assertNotNull("addGetUser: user exist", man.getUser(u.getID()));
        
        User uu = man.getUser(u.getID());
        
        assertEquals("addGetUser: name", u.getName(), uu.getName());
        assertEquals("addGetUser: id", u.getID(), uu.getID());
        assertEquals("addGetUser: email", u.getEmail(), uu.getEmail());
    }
    
    @Test
    public void addDuplicateUserTest()
    {
        ConferencesManager man = new ConferencesManager();
        User u = new User("Cancer Cowboy", "cjesperson@jpod.ca");
        
        assertTrue("addDuplicateUser: first user", man.addUser(u));
        assertFalse("addDuplicateUser: second user", man.addUser(u));
    }
    
    @Test
    public void removeUserTest()
    {
        ConferencesManager man = new ConferencesManager();
        User u = new User("Cancer Cowboy", "cjesperson@jpod.ca");
        User uu = new User("mountain juniper", "jonhdoe@jpod.ca");
        
        man.addUser(u);
        
        //try to remove a user not in the list
        assertFalse("removeUser: doesn't exist", man.removeUser(uu.getID()));
        
        assertTrue("removeUser: user removed", man.removeUser(u.getID()));
        assertNull("removeUser: can't get", man.getUser(u.getID()));
    }
    
    @Test
    public void getAllUsersTest()
    {
        ConferencesManager man = new ConferencesManager();
        User u1 = new User("Cancer Cowboy", "cjesperson@jpod.ca");
        User u2 = new User("mountain juniper", "jonhdoe@jpod.ca");
        User u3 = new User("Charles Neumann", "c.neumann@gmail.com");
        
        man.addUser(u1);
        man.addUser(u2);
        man.addUser(u3);
        
        User u4 = new User("Mouse Fitzgerald", "ozmomouse@as.com");
        
        Set<User> set = man.getAllUsers();
        assertTrue("getAllUsers: first", set.contains(u1));
        assertTrue("getAllUsers: second", set.contains(u2));
        assertTrue("getAllUsers: third", set.contains(u3));
        assertFalse("getAllUsers: fourth", set.contains(u4));
    }
}
