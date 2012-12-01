package edu.uwt.tcss360.Default.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class SubmitPaperListener implements ActionListener {
	
	final JPanel my_parent_panel;
	
	public SubmitPaperListener(final JPanel the_parent_panel)
	{
		my_parent_panel = the_parent_panel;
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(my_parent_panel);
	}

}
