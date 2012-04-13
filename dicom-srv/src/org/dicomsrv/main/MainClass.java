package org.dicomsrv.main;

import org.dicomsrv.ui.MainWindow;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
	}

}
