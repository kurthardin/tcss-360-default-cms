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
	protected final CurrentState my_current_state;
	
	/**
	 * the panel manager that this panel will send signals to.
	 */
	protected PanelManager my_panel_mgr;
	
	/*
	 * methods
	 */
	
	/**
	 * Constructor for AbstractConferencesPanel.
	 * @param the_state the CurrentState passed to it by the frame.
	 */
	//TODO make this constructor protected so that it can't be initiated
	//unless its children call super().
	public AbstractConferencesPanel(final CurrentState the_state)
	{
		super();
		my_current_state = the_state;
	}
	
	/**
	 * adds a PanelManager object to be used by this class.
	 * @param the_panel_mgr
	 */
	public void addPanelManager(PanelManager the_panel_mgr)
	{
		my_panel_mgr = the_panel_mgr;
	}
}
