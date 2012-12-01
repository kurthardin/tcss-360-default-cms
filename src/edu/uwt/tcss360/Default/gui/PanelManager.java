/**
 * PanelManager.java
 * Scott Sanderson
 * 11/15/2012
 */

package edu.uwt.tcss360.Default.gui;

import javax.swing.JPanel;

/**
 * This interface allows the panels to communicate up to the frame with only
 * the methods defined in this interface.
 * @author Scott Sanderson
 * @version 1.0
 */
public interface PanelManager 
{
	/**
	 * pushes a JPanel onto a stack.
	 * @param the_panel the panel
	 */
	public void pushPanel(AbstractConferencesPanel the_panel);
	
	/**
	 * pops a JPanel off a stack.
	 * @return the top panel of the stack.
	 */
	public JPanel popPanel();
	
	/**
	 * updates the PanelManager class
	 */
	public void updateView();
}
