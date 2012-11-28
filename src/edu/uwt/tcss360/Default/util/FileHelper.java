/**
 * FileHelper.java
 * Author: Travis Lewis
 * Date: 8 November 2012
 */

package edu.uwt.tcss360.Default.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * FileHelper contains a set of static methods aimed at aiding the usage
 * of files.
 * 
 * Go ahead and modify this class and add yourself as an author if you've
 * created a method that you've needed.
 * @author Travis Lewis
 * @version 8 November 2012
 */
public class FileHelper {
	
	public static final String DATA_FILE_NAME = "info.cmsd";
	public static final String USERS_DATA_FILE_NAME = "users.cmsd";
	public static final String REVIEW_DIRECTORY_PREFIX = "review_";
	
	// XML element names
	public static final String XML_ELEMENT_FIELDS = "fields";
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_parent
	 * @param the_directory_name
	 * @return
	 */
	public static final File getDirectory(final File the_parent, 
			final String the_directory_name) 
	{
		File dir = new File(the_parent, the_directory_name);
		if (!dir.exists()) {
			dir  = createDirectory(the_parent, the_directory_name);
		}
		return dir;
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @return
	 */
	public static final File getDataDirectory() {
		return getDirectory(new File("."), "data");
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @return
	 */
	public static final File getConferencesDirectory() {
		return getDirectory(getDataDirectory(), "conferences");
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_conference_directory
	 * @return
	 */
	public static final File getPapersDirectory(File the_conference_directory)
	{
		return getDirectory(the_conference_directory, "papers");
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_paper_directory
	 * @return
	 */
	public static final File getRecommendationDirectory(
			File the_paper_directory)
	{
		return getDirectory(the_paper_directory, "recommendation");
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_name
	 * @return
	 */
	public static final String formatFilename(String the_name) 
	{
		final char escapeChar = '%';
		int len = the_name.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
		    char ch = the_name.charAt(i);
		    if (ch < ' ' || ch >= 0x7F || ch == '/' || ch == ':'
		        || (ch == '.' && i == 0)
		        || ch == escapeChar) {
		        sb.append(escapeChar);
		        if (ch < 0x10) {
		            sb.append('0');
		        }
		        sb.append(Integer.toHexString(ch));
		    } else {
		        sb.append(ch);
		    }
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static final Document createXmlDocument() 
			throws ParserConfigurationException 
	{
		DocumentBuilderFactory doc_factory = 
				DocumentBuilderFactory.newInstance();
		DocumentBuilder doc_builder = doc_factory.newDocumentBuilder();
		return doc_builder.newDocument();
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_xml
	 * @param the_data_file
	 * @throws TransformerException 
	 */
	public static final void writeXmlDataFile(Document the_xml,
			File the_parent, String the_data_file_name) 
					throws TransformerException 
	{
		File data_file = new File(the_parent, 
				the_data_file_name);
		if (data_file.exists()) {
			data_file.delete();
		}
		data_file = FileHelper.createFile(the_parent, 
				the_data_file_name);
		StreamResult result = new StreamResult(data_file);

		TransformerFactory transformerFactory = 
				TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(the_xml);
		transformer.transform(source, result);
	}
	
	/**
	 * Gets a BufferedReader for the given file. Don't forget to close the
	 * reader when done using it!
	 * @param the_file The file to get a BufferedReader for
	 * @return <code>null</code> if the_file is a directory or if the_file
	 * does not exist or if there was an exception. Returns a BufferedReader
	 * for the file if it was opened successfully.
	 */
	public static BufferedReader getFileReader(File the_file) {
		if(the_file.isDirectory() || !the_file.exists())
			return null;
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(the_file.getPath()));
		} catch (IOException e) {
			return null;
		}
		return br;
	}
	
	/**
	 * Gets a BufferedReader for the given file in the given directory. Don't
	 * forget to close the reader when done using it!
	 * @param the_directory The directory containing the file.
	 * @param the_file_name The name of the file, do not include a preceding
	 * "/" in the name.
	 * @return <code>null</code> if the_file is a directory or if the_file
	 * does not exist or if there was an exception. Returns a BufferedReader
	 * for the file if it was opened successfully.
	 */
	public static BufferedReader getFileReader(File the_directory, 
			String the_file_name){
		return getFileReader(new File(the_directory.getAbsolutePath() + "/" 
			+ the_file_name));
	}

	/**
	 * Gets an InputSource for the given file for use with a SAXParser.
	 * @param the_file The file to get an InputSource for
	 * @return <code>null</code> if the_file is a directory or if the_file
	 * does not exist or if there was an exception. Returns an InputSource
	 * for the file if it was opened successfully.
	 */
	public static InputSource getInputSource(File the_file) {
		if(the_file.isDirectory() || !the_file.exists())
			return null;
		
		InputSource br = null;
		
		try {
			br = new InputSource(new FileReader(the_file.getPath()));
		} catch (IOException e) {
			return null;
		}
		return br;
	}
	
	/**
	 * Gets an InputSource for the given file in the given directory for 
	 * use with a SAXParser.
	 * @param the_directory The directory containing the file.
	 * @param the_file_name The name of the file, do not include a preceding
	 * "/" in the name.
	 * @return <code>null</code> if the_file is a directory or if the_file
	 * does not exist or if there was an exception. Returns an InputSource
	 * for the file if it was opened successfully.
	 */
	public static InputSource getInputSource(File the_directory, 
			String the_file_name){
		return getInputSource(new File(the_directory.getAbsolutePath() + "/" 
			+ the_file_name));
	}
	
	/**
	 * Gets a BufferedWriter for the given file. Don't forget to close the
	 * writer when you're done using it!
	 * @param the_file The file to get a BufferedWriter for.
	 * @return <code>null</code> if the_file is a directory, the_file doesn't
	 * exist, or there was an exception. Returns a BufferedWriter for the file
	 * if it was created successfully.
	 */
	public static BufferedWriter getFileWriter(File the_file) {
		if(the_file.isDirectory() || !the_file.exists())
			return null;
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(the_file.getPath()));
		} catch (IOException e) {
			return null;
		}
		return bw;
	}
	
	/**
	 * Gets a BufferedWriter for the given file. Don't forget to close the
	 * writer when you're done using it!
	 * @param the_directory The directory containing the file to create a
	 * BufferedReader for.
	 * @param the_file The name of the file to write to, do not include a
	 * preceeding /.
	 * @return <code>null</code> if the_file is a directory, the_file doesn't
	 * exist, or there was an exception. Returns a BufferedWriter for the file
	 * if it was created successfully.
	 */
	public static BufferedWriter getFileWriter(File the_directory, 
			final String the_file_name) {
		return getFileWriter(new File(the_directory.getAbsolutePath() + "/"
			+ the_file_name));
	}
	
	/**
	 * Gets a string representing the leaf of the given File.
	 * @param the_file The file to extract the leaf from.
	 * @return The leaf of the file path (whatever comes after the last slash).
	 */
	public static String getLeafString(File the_file) {
		String path = the_file.getAbsolutePath();
		return path.substring(path.lastIndexOf("/") + 1);
	}
	
	/**
	 * Copies the source file to the destination file.
	 * @param the_source The file to copy.
	 * @param the_dest Where to place the copy, must include path and
	 * file name. Must not already exist.
	 * @return <code>false</code> if the destination file already exists,
	 * the_source doesn't exist, the_source is a directory, or an exception
	 * occured. Returns <code>true</code> if the file was successfully copied.
	 */
	public static boolean copyFile(File the_source, File the_dest)
	{
		if(!the_source.exists() || !the_source.isFile())
			return false;
		
		if(!the_dest.exists()) {
			try {
				the_dest.createNewFile();
			} catch (IOException e) {
				return false;
			}
		} else
			return false;
		
		InputStream instream = null;
		OutputStream outstream = null;
		try {
			instream = new FileInputStream(the_source);
			outstream = new FileOutputStream(the_dest);
			
			byte[] buffer = new byte[1024];
			int length;
			while((length = instream.read(buffer)) > 0)
				outstream.write(buffer, 0, length);
			instream.close();
			outstream.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates a new subdirectory under the given parent directory.
	 * @param the_parent The directory to create the new subdirectory under.
	 * @param the_name The name of the new subdirectory, do not include a
	 * preceding "/", only the folder name.
	 * @return <code>null<code> if the_parent doesn't exist, the_parent isn't 
	 * a directory, the subdirectory already exists, or couldn't be created. 
	 * Returns a File object of the new subdirectory if successful.
	 */
	public static File createDirectory(File the_parent, String the_name)
	{
		// if the parent isn't a directory or if it doesn't exist, you can't
		// make a subdirectory in it...
		if(!the_parent.exists() || !the_parent.isDirectory())
			return null;
		
		File newdir = new File(the_parent.getPath() + "/" + the_name);
		if(newdir.exists())
			return null;
		else {
			try {
				newdir.mkdir();
			} catch (SecurityException e) {
				return null;
			}
		}
		
		if(newdir.exists() || newdir.isDirectory())
			return newdir;
		else
			return null;
	}
	
	/**
	 * 
	 * @author Kurt Hardin
	 * @param the_parent
	 * @return
	 */
	public static List<String> getSubdirectoryNames(File the_parent) 
	{
		// if the parent isn't a directory or if it doesn't exist, you can't
		// make a subdirectory in it...
		if(!the_parent.exists() || !the_parent.isDirectory())
			return null;
		
		String[] dir_names = the_parent.list(
				new FilenameFilter() 
				{
					@Override
					public boolean accept(File dir, String name) 
					{
						return (new File(dir, name).isDirectory());
					}
				});
		return Arrays.asList(dir_names);
	}
	
	/**
	 * Attempts to create a new File in the given directory with the given
	 * name.
	 * @param the_directory The directory to create the file in.
	 * @param the_name The name of the file to be created. Do not include
	 * a preceeding /
	 * @return <code>null</code> if the directory doesn't exist, the directory
	 * is a file, the file already exists, or an exception occured. Returns
	 * a new File object if the file was created successfully.
	 */
	public static File createFile(File the_directory, String the_name) {
		//if the containing directory doesn't exist, you can't make
		//a file in it
		if(!the_directory.exists() || !the_directory.isDirectory())
			return null;
		
		File newfile = new File(the_directory.getPath() + "/" + the_name);
		
		if(newfile.exists())
			return null;
		else {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		
		if(newfile.exists() && newfile.isFile())
			return newfile;
		else
			return null;
	}
}
