package org.dicomsrv.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class SimpleStatusBar extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel label = new JLabel();
	
	private void initialize() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED)); 
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1; 
		add(label, c);
	}
	
	public SimpleStatusBar() {
		super();
		initialize();
	}

	public SimpleStatusBar(String message) {
		super();
		initialize();
		label.setText(message);
	}

	public void setMessage(String message) {
		label.setText(message);		
	}
}
