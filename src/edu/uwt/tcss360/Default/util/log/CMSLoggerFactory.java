/**
 * 
 */
package edu.uwt.tcss360.Default.util.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kurt Hardin
 * @version 1.0
 */
public final class CMSLoggerFactory 
{
	
	private static Level LOG_LEVEL = Level.SEVERE;
	
	public static final void setLevel(Level the_level)
	{
		LOG_LEVEL = the_level;
	}
	
	public static final Logger getLogger(Class<?> the_class) 
	{
		Logger logger = Logger.getLogger(the_class.getName());
		logger.setLevel(LOG_LEVEL);
		
		ConsoleHandler console_handler = new ConsoleHandler();
		logger.addHandler(console_handler);
		
//		try {
//			FileHandler log_file_handler = new FileHandler("log.txt", true);
//			logger.addHandler(log_file_handler);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return logger;
	}
	
	private CMSLoggerFactory() {
		// Avoids instantiation
	}
}
