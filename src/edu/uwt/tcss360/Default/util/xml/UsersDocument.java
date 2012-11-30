/**
 * 
 */
package edu.uwt.tcss360.Default.util.xml;

import java.io.File;

import org.w3c.dom.Element;

import edu.uwt.tcss360.Default.model.User;

/**
 * 
 * @author Kurt Hardin
 * @version 1.0
 */
public class UsersDocument extends CMSDDocument 
{	
	final Element my_users_element;

	public UsersDocument(File the_output_file) 
	{
		super(the_output_file);
		my_users_element = my_document.createElement("users");
		my_document.appendChild(my_users_element);
	}
	
	public void addUser(final User the_user) 
	{
		Element user_element = my_document.createElement(User.XML_ELEMENT_USER);
		user_element.setAttribute(User.XML_ATTR_MY_ID, the_user.getID());
		user_element.setAttribute(User.XML_ATTR_MY_EMAIL, the_user.getEmail());
		user_element.setAttribute(User.XML_ATTR_MY_NAME, the_user.getName());
		my_users_element.appendChild(user_element);
	}
	
}
