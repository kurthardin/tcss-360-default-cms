/**
 * AbstractConferencesPanel.java
 * Scott Sanderson
 * 11/15/2012
 */

package edu.uwt.tcss360.Default.gui;

import javax.swing.JPanel;

import edu.uwt.tcss360.Default.model.CurrentState;

@SuppressWarnings("serial")
public class AbstractConferencesPanel extends JPanel
{
	/*
	 * fields
	 */

	/**
	 * the current state.
	 */
	private final CurrentState my_current_state;
	
	/**
	 * the panel manager that this panel will send signals to.
	 */
	private final PanelManager my_panel_mgr;
	
	/*
	 * methods
	 */
	
	/**
	 * Constructor for AbstractConferencesPanel.
	 * @param the_state the CurrentState passed to it by the frame.
	 * @param the_panel_mgr the panel manager object that is creating this panel.
	 */
	//TODO make this constructor protected so that it can't be initiated
	//unless its children call super().
	public AbstractConferencesPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super();
		my_panel_mgr = the_panel_mgr;
		my_current_state = the_state;
	}
	
	/**
	 * returns the current state object to subclasses.
	 * @return the current state
	 */
	protected CurrentState getCurrentState()
	{
		return my_current_state;
	}
	
	/**
	 * returns the panel manager object to subclasses.
	 * @return the panel manager
	 */
	protected PanelManager getPanelManager()
	{
		return my_panel_mgr;
	}
}
