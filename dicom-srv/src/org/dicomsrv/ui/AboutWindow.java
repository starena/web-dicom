package org.dicomsrv.ui;

import javax.swing.JOptionPane;

public class AboutWindow {
	
	public void show() {
		JOptionPane.showMessageDialog(
				null, 
				"DICOM-Server\n\n"
				+"Authors:\n"
				+"bart [barthand@gmail.com]\n"
				+"vilandra [martadudek.md@gmail.com]\n\n"
				+"2009 - PUT IwM Project", 
				"DICOM Server", 
				JOptionPane.INFORMATION_MESSAGE);
	}
}
