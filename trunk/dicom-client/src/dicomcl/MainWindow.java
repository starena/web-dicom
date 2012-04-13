package dicomcl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import dicomcl.DicomStorageService;
import dicomcl.DicomServicesCore;

public class MainWindow extends JFrame 
{
	private final DicomStorageService storageService = new DicomStorageService();
	
	private static final long serialVersionUID = 1L;
	private final static String MENUITEM_QUIT = "Quit";
	private final static String MENUITEM_ABOUT = "About";
	
	private JMenuBar menuBar;
	private ServerPanel serverPanel;
	private LocalPanel localPanel;
	private MsgPanel msgPanel;
	private MainWindow me;
	private JSplitPane splitPane;
	
	public File getSelectedDir()
	{
		return localPanel.getSelectedDir();
	}
	
	public void setMessage (String msg)
	{
		msgPanel.setMessage (msg);
	}
	
	public String getAE()
	{
		return serverPanel.getAE();
	}
	
	public String getIP()
	{
		return serverPanel.getIP();
	}
	
	public int getPort()
	{
		return serverPanel.getPort();
	}
	
	private void initializeComponents()
	{
		JMenu menu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();

		menu = new JMenu ("File");
		menuItem = new JMenuItem (MENUITEM_QUIT);
		menuItem.addActionListener (new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				processWindowEvent (new WindowEvent (me,WindowEvent.WINDOW_CLOSING));
			}
		});
		menu.add (menuItem);
		menuBar.add (menu);

		menu = new JMenu ("Help");
		menuItem = new JMenuItem(MENUITEM_ABOUT);
		menuItem.addActionListener (new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog (me, 
						"DICOM-Client\n\n"
						+"Authors:\n"
						+"bart [barthand@gmail.com]\n"
						+"vilandra [martadudek.md@gmail.com]\n\n"
						+"2009 - PUT IwM Project", 
						"DICOM Client", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		setLayout (new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1; gbc.weighty = 1;
		
		serverPanel = new ServerPanel (this);
		localPanel = new LocalPanel (this);
		splitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT,true,serverPanel,localPanel);
		splitPane.setDividerSize(10);
		
		gbc.gridx = 0; gbc.gridy = 0;
		add (splitPane,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0;
		msgPanel = new MsgPanel();
		add (msgPanel,gbc);
	}
	
	public MainWindow()
	{
		super ("Dicom Client - PUT IwM Project");
		me = this;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setMinimumSize (new Dimension (800,600));
		setLocation ((int)(d.getWidth()-this.getWidth())/2,(int)(d.getHeight()-this.getHeight())/2);
		initializeComponents();
		pack();
		setVisible (true);
		DicomServicesCore.prepare();
		DicomServicesCore.setPort(11119);
		DicomServicesCore.setHostname("localhost");
		DicomServicesCore.setAEtitle("DCMMWL");
		DicomServicesCore.setPackPDV(true);
		DicomServicesCore.setTcpNoDelay(true);
		DicomServicesCore.initTransferCapability();
		storageService.setDestination("C:\\");
        try {
			DicomServicesCore.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
