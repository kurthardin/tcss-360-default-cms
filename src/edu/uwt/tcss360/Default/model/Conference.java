/**
 * Conference.java
 * Kurt Hardin
 * 11/07/2012
 */
package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.uwt.tcss360.Default.model.User.Role;
import edu.uwt.tcss360.Default.util.FileHelper;
import edu.uwt.tcss360.Default.util.xml.ConferenceDocument;
import edu.uwt.tcss360.Default.util.xml.InfoDocument;
import edu.uwt.tcss360.Default.util.xml.parsers.CMSDParser;
import edu.uwt.tcss360.Default.util.xml.parsers.InfoHandler;

/**
 * Class representing a single conference in the 
 * conference management system.  This class Maintains 
 * all data relevant to a given conference. 
 * 
 * @author Kurt Hardin
 * @version 1.0
 */
public final class Conference {

	// XML element names
	public static final String XML_ELEMENT_MY_USERS_ROLES = "my_users_roles";
	public static final String XML_ELEMENT_ROLE = "role";
	
	// XML attribute names
	public static final String XML_ATTR_MY_NAME = "my_name";
	public static final String XML_ATTR_MY_START_DATE = "my_start_date";
	public static final String XML_ATTR_MY_END_DATE = "my_end_date";
	public static final String XML_ATTR_MY_SUBMISSION_DEADLINE = 
			"my_submission_deadline";
	public static final String XML_ATTR_MY_REVIEW_DEADLINE = 
			"my_review_deadline";
	public static final String XML_ATTR_MY_RECOMMENDATION_DEADLINE = 
			"my_recommendation_deadline";
	public static final String XML_ATTR_MY_REVISION_DEADLINE = 
			"my_final_revision_deadline";
	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_NAME = "name";
	
	public static final DateFormat CONFERENCE_DATE_FORMAT = 
			new SimpleDateFormat("MM-dd-yyyy");
	
	/**
	 * The name of this conference.
	 */
	private String my_name;
	
	
	/**
	 * The date on which this conference starts.
	 */
	private Date my_start_date;
	
	/**
	 * The date on which this conference ends.
	 */
	private Date my_end_date;
	
	
	/**
	 * The paper submission deadline for this conference.
	 */
	private Date my_submission_deadline;
	
	/**
	 * The review submission deadline for this conference.
	 */
	private Date my_review_deadline;
	
	/**
	 * The recommendation submission deadline for this conference.
	 */
	private Date my_recommendation_deadline;
	
	/**
	 * The final paper revision submission deadline for this conference.
	 */
	private Date my_final_revision_deadline;
	
	
	/**
	 * The Users and their associated Roles for this conference.
	 */
	private final Map<String, Set<Role>> my_users_roles = 
			new HashMap<String, Set<Role>>();
	
	/**
	 * The Papers submitted to this conference.
	 */
	private final Set<Paper> my_papers = new HashSet<Paper>();
	
	private final File my_directory;
	
	/**
	 * Creates a new Conference initialized with 
	 * data from the specified File.
	 * @param a_data_file the data file to initialize this Conference from.
	 */
	public Conference(final File the_conference_dir) 
	{
		if(the_conference_dir == null)
		    throw new IllegalArgumentException(
		    		"Conference directory cannot be null");
		
		if(!the_conference_dir.exists())
		    throw new IllegalArgumentException(
		    		"Conference directory must exist");
	    
		my_directory = the_conference_dir;
		my_name = "Null";
		my_start_date = null;
		my_end_date = null;
		
		File input_file = new File(my_directory, FileHelper.DATA_FILE_NAME);
		InfoHandler handler = new ConferenceInfoHandler();
		new CMSDParser(input_file, handler).parse();
		
		// Initialize my_papers
		File papers_dir = FileHelper.getPapersDirectory(my_directory);
		if (papers_dir != null) 
		{
			List<String> paper_dir_names = 
					FileHelper.getSubdirectoryNames(papers_dir);
			
			for (String paper_dir_name : paper_dir_names) 
			{
				File paper_dir = new File(papers_dir, paper_dir_name);
				Paper conference = new Paper(paper_dir);
				my_papers.add(conference);
			}
		}
		
		// TODO Write unit tests for Conference(File)
	}
	
	/**
	 * Creates a new Conference initialized with the 
	 * specified program chair, name and date.
	 * @param the_pc_user_id the program chair for this conference.
	 * @param the_name the name of this conference.
	 * @param the_date the date on which this conference will occur.
	 */
	public Conference(final String the_pc_user_id, 
			final String the_name, final Date the_date) 
	{
		this(the_pc_user_id, the_name, the_date, the_date);
	}
	
	/**
	 * Creates a new Conference initialized with the specified
	 * program chair, name, starting date, and ending date.
	 * @param the_pc_user_id the program chair for this conference.
	 * @param the_name the name of this conference. 
	 * @param the_start the starting date for this conference.
	 * @param the_end the ending date for this conference.
	 */
	public Conference(final String the_pc_user_id, final String the_name, 
			final Date the_start, final Date the_end) 
	{
		if (the_name == null) 
		{
			throw new IllegalArgumentException(
					"The conference name cannot be null");
		}
		
		if (the_start == null) 
		{
			throw new IllegalArgumentException(
					"The conference start date cannot be null");
		}
		
		if (the_end == null) 
		{
			throw new IllegalArgumentException(
					"The conference end date cannot be null");
		}
		
		if (the_start.before(new Date())) {
			throw new IllegalArgumentException(
					"The conference start date must be in the future");
		}
		
		if (the_end.before(the_start)) {
			throw new IllegalArgumentException(
					"The conference start date cannot be after the end date");
		}
		
		my_name = the_name;
		my_start_date = the_start;
		my_end_date = the_end;
		authorizeUser(the_pc_user_id, Role.PROGRAM_CHAIR);
		
		File confs_dir = FileHelper.getConferencesDirectory();
		String conf_dir_name = FileHelper.formatFilename(getID());
		my_directory = FileHelper.getDirectory(confs_dir, conf_dir_name);
		writeData();
	}
	
	/**
	 * 
	 */
	public void writeData() 
	{
			// Build the XML document
			File output_file = new File(my_directory, 
					FileHelper.DATA_FILE_NAME);
			new ConferenceDocument(output_file)
			.addMyUsersRolesField(my_users_roles)
			.setField(XML_ATTR_MY_NAME, my_name)
			.setField(XML_ATTR_MY_START_DATE, my_start_date)
			.setField(XML_ATTR_MY_END_DATE, my_end_date)
			.setField(XML_ATTR_MY_SUBMISSION_DEADLINE, 
					my_submission_deadline)
			.setField(XML_ATTR_MY_REVIEW_DEADLINE, my_review_deadline)
			.setField(XML_ATTR_MY_RECOMMENDATION_DEADLINE, 
					my_recommendation_deadline)
			.setField(XML_ATTR_MY_REVISION_DEADLINE, 
					my_final_revision_deadline)
			.write();
		
		for (Paper paper : my_papers) {
			paper.writeData();
		}
	}
	
	/**
	 * Gets the name of this Conference.
	 * @return the name of this conference.
	 */
	public String getname() 
	{
		return my_name;
	}
	
	/**
	 * Gets the starting date for this Conference.
	 * @return the starting date.
	 */
	public Date getStartDate() 
	{
		return my_start_date == null ? null : 
			(Date) my_start_date.clone();
	}
	
	/**
	 * Gets the ending date for this Conference.
	 * @return the ending date.
	 */
	public Date getEndDate() 
	{
		return my_end_date == null ? null : 
			(Date) my_end_date.clone();
	}
	
	/**
	 * Gets the paper submission deadline for this Conference.
	 * @return the paper submission deadline.
	 */
	public Date getSubmissionDeadline() 
	{
		return my_submission_deadline == null ? null : 
			(Date) my_submission_deadline.clone();
	}
	
	/**
	 * Sets the paper submission deadline for this Conference.
	 * @param the_deadline the new paper submission deadline.
	 */
	public void setSubmissionDeadline(final Date the_deadline) 
	{
		if (the_deadline == null) 
		{
			throw new IllegalArgumentException(
					"The submission deadline cannot be null");
		}
		my_submission_deadline = (Date) the_deadline.clone();
	}
	
	/**
	 * Gets the review deadline for this Conference.
	 * @return the review deadline.
	 */
	public Date getReviewDeadline() 
	{
		return my_review_deadline == null ? null : 
			(Date) my_review_deadline.clone();
	}
	
	/**
	 * Sets the review deadline for this Conference.
	 * @param the_deadline the new review deadline.
	 */
	public void setReviewDeadline(final Date the_deadline) 
	{
		if (the_deadline == null) 
		{
			throw new IllegalArgumentException(
					"The review deadline cannot be null");
		}
		my_review_deadline = (Date) the_deadline.clone();
	}
	
	/**
	 * Gets the recommendation deadline for this Conference.
	 * @return the recommendation deadline.
	 */
	public Date getRecommendationDeadline() 
	{
		return my_recommendation_deadline == null ? null : 
			(Date) my_recommendation_deadline.clone();
	}
	
	/**
	 * Sets the recommendation deadline for this Conference.
	 * @param the_deadline the new recommendation deadline.
	 */
	public void setRecommendationDeadline(final Date the_deadline) 
	{
		if (the_deadline == null) 
		{
			throw new IllegalArgumentException(
					"The recommendation deadline cannot be null");
		}
		my_recommendation_deadline = (Date) the_deadline.clone();
	}
	
	/**
	 * Gets the final paper revision submission deadline for this Conference.
	 * @return the final paper revision submission deadline.
	 */
	public Date getFinalRevisionDeadline() 
	{
		return my_final_revision_deadline == null ? null : 
			(Date) my_final_revision_deadline.clone();
	}
	
	/**
	 * Sets the final paper revision submission deadline for this Conference.
	 * @param the_deadline the new final paper revision submission deadline.
	 */
	public void setFinalRevisionDeadline(final Date the_deadline) 
	{
		if (the_deadline == null) 
		{
			throw new IllegalArgumentException(
					"The final revision deadline cannot be null");
		}
		my_final_revision_deadline = (Date) the_deadline.clone();
	}
	
	public File getDirectory() 
	{
		return my_directory.getAbsoluteFile();
	}
	
	public File getPapersDirectory() 
	{
		File papers_dir = new File(my_directory, "papers");
		if (!papers_dir.exists()) {
			papers_dir = FileHelper.createDirectory(my_directory, 
					"papers");
		}
		return papers_dir.getAbsoluteFile();
	}
	
	/**
	 * Authorizes the specified User to perform the specified Role 
	 * for this Conference.  This method returns true if the role is 
	 * assigned to the user, or false if the User is already 
	 * authorized to perform the specified Role.  
	 * @param the_user_id the User to authorize.
	 * @param the_role the Role to assign.
	 * @return true if the User could be assigned the specified Role, 
	 * 			otherwise false. 
	 */
	public boolean authorizeUser(
			final String the_user_id, 
			final Role the_role) 
	{
		
		if (the_user_id == null) 
		{
			throw new IllegalArgumentException(
					"The user to authorize cannot be null");
		}
		
		if (the_role == null) 
		{
			throw new IllegalArgumentException(
					"The role to authorize cannot be null");
		}
		
		Set<Role> user_roles = my_users_roles.get(the_user_id);
		if (user_roles == null) 
		{
			user_roles = new HashSet<Role>();
			my_users_roles.put(the_user_id, user_roles);
		}
		
		return user_roles.add(the_role);
	}
	
	/**
	 * Deauthorizes the specified User from performing the specified
	 * Role for this Conference.  This method returns true if the 
	 * Role is unassigned from the User, or false if the Role cannot be 
	 * unassigned.  A user cannot be unassigned the Roles of 
	 * {@link Role#PROGRAM_CHAIR} or {@link Role#USER}.  A User also 
	 * cannot be unassigned from any Role for which they still have 
	 * assigned {@link Paper}s for this Conference.  
	 * @param the_user_id
	 * @param the_role
	 * @return true if the Role is unassigned from the User, or false 
	 * 			if the Role cannot be unassigned.
	 */
	public boolean deauthorizeUser(
			final String the_user_id, 
			final Role the_role) 
	{
		
		if (the_user_id == null) 
		{
			throw new IllegalArgumentException(
					"The user to deauthorize cannot be null");
		}
		
		if (the_role == null) 
		{
			throw new IllegalArgumentException(
					"The role to deauthorize cannot be null");
		}
		
		boolean canDeauthorize;
		
		switch (the_role) 
		{
		case AUTHOR:
		case REVIEWER:
		case SUBPROGRAM_CHAIR:
			canDeauthorize = getPapers(the_user_id, the_role).isEmpty();
			break;
		default:
			canDeauthorize = false;
			break;
		}
		
		if (canDeauthorize) 
		{
			Set<Role> user_roles = my_users_roles.get(the_user_id);
			if (user_roles != null) 
			{
				user_roles.remove(the_role);
			}
		}
		
		return canDeauthorize;
	}
	
	/**
	 * Gets the {@link User} IDs of all {@link User}s assigned the 
	 * specified Role for this Conference.
	 * @param a_role the Role for which to retrieve {@link User} IDs.
	 * @return the list of {@link User} IDs assigned the specified Role.
	 */
	public List<String> getUserIds(final Role a_role) 
	{
		if (a_role == null) 
		{
			throw new IllegalArgumentException(
					"The role of users to retrieve cannot be null");
		}
		
		final Set<String> all_user_ids = my_users_roles.keySet();
		final List<String> user_ids_with_role = 
				new ArrayList<String>(all_user_ids.size());
		
		for (String user_id : all_user_ids) 
		{
			Set<Role> user_roles = my_users_roles.get(user_id);
			if (user_roles.contains(a_role)) 
			{
				user_ids_with_role.add(user_id);
			}
		}
		
		return user_ids_with_role;
	}
	
	/**
	 * Gets the roles for the specified user id passed in.
	 * @param the_user_id the user id.
	 * @return a set of roles associated with the user id.
	 * @author Scott Sanderson
	 */
	@SuppressWarnings("unchecked")
	public Set<Role> getRoles(final String the_user_id)
	{
		Set<Role> roles = my_users_roles.get(the_user_id);
		return roles == null ? Collections.EMPTY_SET : roles;
	}
	
	/**
	 * Gets all the papers in the conference.
	 * @return All the papers in the conference
	 * @author Travis Lewis
	 */
	public List<Paper> getPapers()
	{
		return new ArrayList<Paper>(my_papers);
	}
	
	/**
	 * Gets all Papers assigned to the specified User with the specified Role
	 * for this Conference.
	 * @param the_user_id the User for which to retrieve Papers.
	 * @param a_role the Role of the User for which to retrieve Papers.
	 * @return the list of Papers assigned to the User with the specified Role.
	 */
	public List<Paper> getPapers(final String the_user_id, final Role a_role) 
	{
		if (the_user_id == null) 
		{
			throw new IllegalArgumentException(
					"The user id for which to retrieve papers cannot be null");
		}
		
		if (a_role == null) 
		{
			throw new IllegalArgumentException(
					"The role for which to retrieve papers cannot be null");
		}
		
		List<Paper> users_papers = new ArrayList<Paper>(my_papers.size());
		for (Paper current_paper : my_papers) 
		{
			if (a_role.equals(current_paper.getRole(the_user_id))) 
			{
				users_papers.add(current_paper);
			}
		}
		return users_papers;
	}
	
	/**
	 * Adds a Paper to this Conference.
	 * @param the_author_id the {@link User} ID of the {@link Role#AUTHOR}
	 * 						of this Paper.
	 * @param a_paper the Paper to add.
	 */
	public void addPaper(String the_author_id, Paper a_paper) 
	{
		if (the_author_id == null) 
		{
			throw new IllegalArgumentException(
					"The author of the paper cannot be null");
		}
		
		if (a_paper == null) 
		{
			throw new IllegalArgumentException(
					"The paper to add cannot be null");
		}
		
		if (my_papers.add(a_paper)) 
		{
			authorizeUser(the_author_id, Role.AUTHOR);
		}
	}
	
	/**
	 * Removes a Paper from this Conference.  If this is the last Paper
	 * submitted to this Conference by the {@link User} who is the 
	 * {@link Role#AUTHOR} of the Paper to be removed, that {@link User} will
	 * be automatically deauthorized as an {@link Role#AUTHOR} 
	 * for this Conference.
	 * @param a_paper the Paper to remove.
	 */
	public void removePaper(Paper a_paper) 
	{
		if (a_paper == null) 
		{
			throw new IllegalArgumentException(
					"The paper to remove cannot be null");
		}
		
		if (my_papers.remove(a_paper)) 
		{
			deauthorizeUser(a_paper.getAuthorID(), Role.AUTHOR);
		}
	}
	
	/**
	 * Returns the unique ID associated with the conference.
	 * @return the unique ID for this conference.
	 */
	public String getID()
	{
		StringBuilder sb = new StringBuilder(my_name);
		sb.append(CONFERENCE_DATE_FORMAT.format(my_start_date));
		sb.append(CONFERENCE_DATE_FORMAT.format(my_end_date));
		return sb.toString();
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @version 1.0
	 */
	private class ConferenceInfoHandler extends InfoHandler 
	{	
		private boolean my_inside_users_roles;
		private String my_current_user_id;
		
		@Override
		public void handleFieldsAttributes(Attributes attr) 
		{
			my_name = attr.getValue(XML_ATTR_MY_NAME);
			my_start_date = InfoDocument.getDateFromAttributeValue(
					attr.getValue(XML_ATTR_MY_START_DATE));
			my_end_date = InfoDocument.getDateFromAttributeValue(
						attr.getValue(XML_ATTR_MY_END_DATE));
			my_submission_deadline = InfoDocument.getDateFromAttributeValue(
					attr.getValue(XML_ATTR_MY_SUBMISSION_DEADLINE));
		}
		
		@Override
		public void startUnknownFieldsElement(String uri, 
				String localName, String qName, Attributes attr) 
		{
			if (qName.equalsIgnoreCase(XML_ELEMENT_MY_USERS_ROLES)) 
			{
				//Started "my_users_roles" element
				my_inside_users_roles = true;
			}
			else if (my_inside_users_roles) 
			{
				if (qName.equalsIgnoreCase(User.XML_ELEMENT_USER)) 
				{
					// Started new "user" element within "my_users_roles"
					my_current_user_id = attr.getValue(XML_ATTR_ID);
				} 
				else if (my_current_user_id != null && 
						qName.equalsIgnoreCase(XML_ELEMENT_ROLE)) 
				{
					// Get the roles for the current "user" element
					Set<Role> user_roles = 
							my_users_roles.get(my_current_user_id);
					if (user_roles == null) {
						// Create Role Set if it doesn't exist
						user_roles = new HashSet<Role>(5);
						my_users_roles.put(my_current_user_id, user_roles);
					}

					// Add Role within current "user" element
					try 
					{
						Role role = Role.valueOf(attr.getValue(XML_ATTR_NAME));
						user_roles.add(role);
					} catch (final IllegalArgumentException e) {
						System.out.println("Encountered invalid Role name: '" +
								attr.getValue(XML_ATTR_NAME) + "'");
					}
				}
			}
		}
		
		@Override
		public final void endUnknownFieldsElement(String uri, String localName,
				String qName) throws SAXException 
		{
			if (qName.equalsIgnoreCase(XML_ELEMENT_MY_USERS_ROLES)) 
			{
				my_inside_users_roles = false;
			} 
			else if (qName.equalsIgnoreCase(User.XML_ELEMENT_USER)) 
			{
				my_current_user_id = null;
			}
		}
	}
	
}
