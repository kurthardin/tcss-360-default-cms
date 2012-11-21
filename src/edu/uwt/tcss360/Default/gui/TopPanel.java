package edu.uwt.tcss360.Default.gui;

import java.awt.Color;
import java.awt.Dimension;

import edu.uwt.tcss360.Default.model.CurrentState;

public class TopPanel extends AbstractConferencesPanel
{
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 * @param the_panel_mgr the panel manager object creating this panel.
	 */
	public TopPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		setupPanel();
	}
	
	
	/**
	 * sets up the top panel to be used.
	 */
	private void setupPanel()
	{
		this.setPreferredSize(new Dimension(ConferencesFrame.WIDTH, 
				ConferencesFrame.TOP_HEIGHT));
		this.setBackground(Color.BLACK);
	}
}
