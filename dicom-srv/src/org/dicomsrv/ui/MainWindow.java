package org.dicomsrv.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.dicomsrv.services.ServiceManager;

public class MainWindow extends JFrame 
implements ActionListener, WindowListener, Runnable {
	
	private final static String MENUITEM_STARTSERVICE = "Start Services";
	private final static String MENUITEM_STOPSERVICE = "Stop Services";
	private final static String MENUITEM_QUIT = "Quit";
	private final static String MENUITEM_ABOUT = "About";
	private final static String MENUITEM_DATABASESETTINGS = "MySQL Settings";	
	private final static String BUTTON_STARTSERVICE = "Start Services";
	private final static String BUTTON_STOPSERVICE = "Stop Services";
	private final static String BUTTON_SEARCHFORDESTINATION = "...";
	
	private static final long serialVersionUID = 1L;
	private JTextArea console;
	private JMenuBar menuBar;
	private JPanel controlPanel;
	private JTextField portField;
	private JTextField aeField;
	private JTextField hostnameField;
	private JTextField destinationField;
	private JButton chooseDestinationButton;
	private JButton startServiceButton;
	private JButton stopServiceButton;
	private SimpleStatusBar statusBar;
	
	private final ServiceManager serviceManager = new ServiceManager();
		
	private Thread thread;
	
	private boolean serviceStarted = false;
	
	private String getCurrentTime() {
		return new SimpleDateFormat().format(Calendar.getInstance().getTime());
	}
	
	public void addMessageToConsole(String message) {
		console.append(message);
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	private void checkButtons() {
		if (serviceStarted) {
			startServiceButton.setEnabled(false);
			stopServiceButton.setEnabled(true);
		} else {
			startServiceButton.setEnabled(true);
			stopServiceButton.setEnabled(false);			
		}
	}
	
	private void startServices() {
		String aeTitle = aeField.getText();
		int port = Integer.parseInt(portField.getText());
		String hostname = hostnameField.getText();
		String destination = destinationField.getText();
		serviceManager.startServices(aeTitle, port, hostname, destination, this);
		
		serviceStarted = true;
		
		addMessageToConsole(
			"Storage Service is preparing to run..."
			+"\n\tAE Title: "+aeTitle
			+"\n\tPort: "+port
			+"\n\tHostname: "+hostname
			+"\n\tDestination: "+destination+"\n"
		);
		
		statusBar.setMessage("Storage Service is running!");
		checkButtons();
		
		if (thread == null) {
			thread = new Thread(this, "ReceiveDICOMListener"); 
			thread.start();
		}
	}
	
	private void stopServices() {
		serviceManager.stopServices();		
		serviceStarted = false;
		addMessageToConsole("Storage Service has been stopped.\n");
		statusBar.setMessage("Storage Service has been stopped!");
		checkButtons();
	}
	
	private void chooseDestination() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			destinationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
	}
	
	private void initializeComponents() {
		JMenu menu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();

		menu = new JMenu("Program");
		menu.setMnemonic(KeyEvent.VK_P);
		menuItem = new JMenuItem(MENUITEM_STARTSERVICE);
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(MENUITEM_STOPSERVICE);
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem(MENUITEM_QUIT);
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		menu = new JMenu("Settings");
		menu.setMnemonic(KeyEvent.VK_S);
		menuItem = new JMenuItem(MENUITEM_DATABASESETTINGS);
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuItem = new JMenuItem(MENUITEM_ABOUT);
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);

		setLayout(new GridBagLayout());

		JLabel aeLabel = new JLabel("AE Title:");
		JLabel portLabel = new JLabel("Port:");
		JLabel hostnameLabel = new JLabel("Hostname:");
		JLabel destinationLabel = new JLabel("Store destination:");
		JPanel settingsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		console = new JTextArea(12,15);
		JScrollPane consoleScrollPane = new JScrollPane(console);

		controlPanel = new JPanel();
		aeField = new JTextField("SERVER");
		portField = new JTextField("11111");
		hostnameField = new JTextField("localhost");
		destinationField = new JTextField("E:\\");
		chooseDestinationButton = new JButton(BUTTON_SEARCHFORDESTINATION);
		startServiceButton = new JButton(BUTTON_STARTSERVICE);
		stopServiceButton = new JButton(BUTTON_STOPSERVICE);
		statusBar = new SimpleStatusBar("Loaded successfully!");
		
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		console.setEditable(false);
		
		startServiceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startServices();
			}			
		});
		
		stopServiceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopServices();
			}			
		});

		checkButtons();
		
		chooseDestinationButton.setPreferredSize(new Dimension(30,20));
		chooseDestinationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseDestination();
			}
		});
		
		settingsPanel.setLayout(new GridBagLayout());
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 0; c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10);
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(aeLabel, c);

		c.weightx = 0.9; c.weighty = 0;
		c.gridx = 1; c.gridy = 0; c.gridwidth = 2;
		settingsPanel.add(aeField, c);
		
		c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
		c.weightx = 0.1; c.weighty = 0;
		c.insets = new Insets(0,10,10,10);
		settingsPanel.add(portLabel, c);
		c.gridx = 1; c.gridy = 1; c.gridwidth = 2;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(portField, c);

		c.gridx = 0; c.gridy = 2; c.gridwidth = 1;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(hostnameLabel, c);
		c.gridx = 1; c.gridy = 2; c.gridwidth = 2;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(hostnameField, c);

		c.gridx = 0; c.gridy = 3; c.gridwidth = 1;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(destinationLabel, c);
		c.gridx = 1; c.gridy = 3;
		c.weightx = 0.9; c.weighty = 0;
		c.insets = new Insets(0,10,10,0);
		settingsPanel.add(destinationField, c);
		c.gridx = 2; c.weightx = 0;
		c.insets = new Insets(0,10,10,10);
		settingsPanel.add(chooseDestinationButton, c);

		buttonsPanel.add(startServiceButton);
		buttonsPanel.add(stopServiceButton);
		
		controlPanel.add(settingsPanel, BorderLayout.PAGE_START);
		controlPanel.add(buttonsPanel, BorderLayout.PAGE_END);
		
		c = new GridBagConstraints();

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		c.weightx = 0.9; c.weighty = 0.5;
		add(consoleScrollPane, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 1; c.gridy = 0;
		c.insets = new Insets(10,0,10,10);
		c.weightx = 0.1; c.weighty = 0.5;
		add(controlPanel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0,0,0,0);
		c.weightx = 1; c.weighty = 0;
		add(statusBar, c);
	}
	
	public MainWindow() {
		super("Dicom Server - PUT IwM Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(700,300));
		initializeComponents();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(MENUITEM_QUIT))
			processWindowEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING));
		else if (command.equals(MENUITEM_STARTSERVICE))
			startServices();
		else if (command.equals(MENUITEM_STOPSERVICE))
			stopServices();
		else if (command.equals(MENUITEM_ABOUT))
			new AboutWindow().show();
		else if (command.equals(MENUITEM_DATABASESETTINGS)) {
			new SettingsWindow(this); 
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		stopServices();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {		
	}

	@Override
	public void windowIconified(WindowEvent e) {		
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	@Override
	public void run() {
		while (true) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				addMessageToConsole("[ "+getCurrentTime()+" ] "+
						serviceManager.getRetrievedObjectInformation());
				statusBar.setMessage("[ "+getCurrentTime()+
						" ] Retrieved Dicom Object!");
			}
		}
	}

}
