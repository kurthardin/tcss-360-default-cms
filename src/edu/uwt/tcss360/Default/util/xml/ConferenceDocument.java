/*
 * ConferenceDocument.java
 * 11-30-2012
 * Kurt Hardin
 */

package edu.uwt.tcss360.Default.util.xml;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import edu.uwt.tcss360.Default.model.Conference;
import edu.uwt.tcss360.Default.model.User;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * @author Kurt Hardin
 * @version 1.0
 */
public class ConferenceDocument extends InfoDocument 
{
	public ConferenceDocument(File the_output_file) 
	{
		super(the_output_file);
	}
	
	public ConferenceDocument addMyUsersRolesField(Map<String, Set<Role>> the_value) 
	{	
		if (the_value != null) 
		{
			// Create root "my_users_roles" XML element
			Element users_roles_elem = my_document.createElement(
					Conference.XML_ELEMENT_MY_USERS_ROLES);
			
			// Create a sub-element in "my_users_roles" for each user
			for (String user_id : the_value.keySet()) 
			{
				Element user_elem = my_document.createElement(
						User.XML_ELEMENT_USER);
				user_elem.setAttribute(Conference.XML_ATTR_ID, user_id);
				
				// Create a sub-element in "user" for each Role
				for (Role role : the_value.get(user_id)) 
				{
					Element role_elem = my_document.createElement(
							Conference.XML_ELEMENT_ROLE);
					role_elem.setAttribute(Conference.XML_ATTR_NAME, role.name());
					
					user_elem.appendChild(role_elem);
				}
				
				users_roles_elem.appendChild(user_elem);
			}
			
			my_fields_element.appendChild(users_roles_elem);
		}
		return this;
	}
	
}
