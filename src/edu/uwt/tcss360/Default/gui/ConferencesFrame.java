/**
 * ConferencesFrame.java
 * Scott Sanderson
 * 11/15/2012
 */

package edu.uwt.tcss360.Default.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.uwt.tcss360.Default.model.ConferencesManager;
import edu.uwt.tcss360.Default.model.CurrentState;

/**
 * The main frame to be used. It will hold all of the panels it needs
 * and display the appropriate panel when its updateView() method is
 * called.
 * @author Scott Sanderson
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConferencesFrame extends JFrame implements PanelManager, Observer
{
	/*
	 * fields
	 */
	
	/**
	 * title of the window
	 */
	private static final String WINDOW_TITLE = "Conference Management System";
	
	/**
	 * width of the window
	 */
	public static final int WIDTH = 750;
	
	/**
	 * height of the main panel
	 */
	public static final int HEIGHT = 500;
	
	/**
	 * height of the top panel
	 */
	public static final int TOP_HEIGHT = 50;
	
	/**
	 * the current state object
	 */
	CurrentState my_current_state;
	
	/**
	 * stack of panels to be used as the main panels.
	 */
	private Stack<AbstractConferencesPanel> my_panels_stack;
	
	/**
	 * panel that holds the top panel of the stack and
	 * the top panel above it. It uses Borderlayout to
	 * organize them.
	 */
	private JPanel my_panel;
	
	/**
	 * the upper panel.
	 */
	private TopPanel my_top_panel;
	
	/*
	 * methods
	 */
	
	/**
	 * default constructor.
	 */
	public ConferencesFrame()
	{
		super(WINDOW_TITLE);
		my_panels_stack = new Stack<AbstractConferencesPanel>();
		my_current_state = new CurrentState(new ConferencesManager());
		my_panel = new JPanel(new BorderLayout());
		my_top_panel = new TopPanel(my_current_state, this);
		my_panels_stack.push(new LoginPanel(my_current_state, this));
		my_panel.add(my_panels_stack.peek(), BorderLayout.CENTER);
	}
	
	/**
	 * starts the application
	 */
	public void start()
	{
		//TODO implement any other startup stuff.
		
		//TODO setup my_panel more.
		my_panel.setPreferredSize(new Dimension(WIDTH, HEIGHT + TOP_HEIGHT));
		my_panel.setBackground(Color.WHITE);
		add(my_panel);
		
		pack();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    //places the frame in the center of the screen.
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
	
	/*
	 * Implemented Methods
	 */
	
	/**
	 * pushes a JPanel onto a stack.
	 * @param the_panel the panel
	 */
	public void pushPanel(AbstractConferencesPanel the_panel)
	{
		my_panels_stack.push(the_panel);
		updateView();
		//TODO add "this" to the panels when they are pushed on the stack
		//casted as a PanelManager object.
	}
	
	/**
	 * pops a JPanel off a stack. Always leaves at least one panel
	 * on the stack (login panel).
	 * @return the top panel of the stack.
	 */
	public JPanel popPanel()
	{
		JPanel panel = null;
		if (my_panels_stack.size() > 1)
		{
			panel = my_panels_stack.pop();
			updateView();
		}
		return panel;
	}
	
	/**
	 * updates the PanelManager class
	 */
	public void updateView()
	{
		my_panel.removeAll();
		if(my_panels_stack.size() > 1)
		{
			my_panel.add(my_top_panel, BorderLayout.NORTH);
		}
		my_panel.add(my_panels_stack.peek(), BorderLayout.CENTER);
	}

	@Override
	public void update(Observable the_o, Object the_arg)
	{
		for(AbstractConferencesPanel panel : my_panels_stack)
		{
			panel.updatePanel();
		}
	}
}
