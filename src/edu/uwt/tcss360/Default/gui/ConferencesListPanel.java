package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import edu.uwt.tcss360.Default.model.CurrentState;

public class ConferencesListPanel extends AbstractConferencesPanel
{
	
	
	/**
	 * the new conference button.
	 */
	final JButton my_new_conf_button;
	
	/**
	 * constructor
	 * @param the_state the current state object used by the frame.
	 * @param the_panel_mgr the panel manager object creating this panel.
	 */
	public ConferencesListPanel(final CurrentState the_state, 
			final PanelManager the_panel_mgr)
	{
		super(the_state, the_panel_mgr);
		my_new_conf_button = new JButton("New Conference");
		setupPanel();
	}
	
	public void setupPanel()
	{
		setLayout(new BorderLayout());
		Box upper_box = Box.createHorizontalBox();
		JLabel title_label = new JLabel("Conferences:");
		title_label.setFont(new Font(null, Font.BOLD, 20));
		//TODO: add listener to new conf button.
		my_new_conf_button.setVerticalAlignment(SwingConstants.BOTTOM);
		upper_box.add(title_label);
		upper_box.add(Box.createRigidArea(new Dimension(ConferencesFrame.WIDTH / 2,0)));
		upper_box.add(my_new_conf_button);
		upper_box.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));
		JPanel center_panel = new JPanel(new GridLayout(0, 1));
		
		//TODO: add buttons that link to conferences.
		//temporary
		for (int i = 0; i < 20; i++)
			center_panel.add(new JButton("Conference number:" + i));
		//end temporary
		
		JScrollPane scroll_pane = new JScrollPane(center_panel);
		add(upper_box, BorderLayout.NORTH);
		add(scroll_pane, BorderLayout.CENTER);
	}
	

	
	
	//test main
	public static void main(String[]args)
	{
		JFrame frame = new JFrame("Conferences Manager");
		frame.setPreferredSize(new Dimension(ConferencesFrame.WIDTH, 
				ConferencesFrame.HEIGHT));
		frame.add(new ConferencesListPanel(null, null));
		
		
		
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void updatePanel() 
	{
		// TODO Auto-generated method stub
	}
}
