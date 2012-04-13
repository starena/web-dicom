package dicomcl;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class LocalPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private DirTreePanel dirPanel;
	private FilesPanel filesPanel;
	private ShortInfoPanel shortInfoPanel;
	private JButton uploadButton, showImageButton, showHeaderButton;
	private File selectedDir;
	private LocalPanel me;
	private MainWindow parent;
	@SuppressWarnings("unused")
	private DicomImageWindow dicomImageWindow;
	
	public File getSelectedDir()
	{
		return filesPanel.getDir();
	}

	public LocalPanel (MainWindow parent)
	{
		me = this;
		this.parent = parent;
		setBorder (BorderFactory.createTitledBorder(new LineBorder(Color.BLUE),"Local"));
		setLayout (new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets (1,1,1,1);
		
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridheight = 3;
		dirPanel = new DirTreePanel();
		dirPanel.dirTree.addTreeSelectionListener (new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e) 
			{
				DefaultMutableTreeNode node = dirPanel.getTreeNode (e.getPath());
				FileNode fnode = dirPanel.getFileNode (node);
				if (fnode != null) selectedDir = fnode.getFile();
				filesPanel.updateList (selectedDir);
			}
		});
		add (dirPanel,gbc);
		
		gbc.gridx = 1;
		gbc.gridheight = 2;
		filesPanel = new FilesPanel();
		filesPanel.itemsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged (ListSelectionEvent e) 
			{
				File file = filesPanel.getSelectedFile();
				shortInfoPanel.updateInfo (file);
			}	
		});
		add (filesPanel,gbc);
		
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		uploadButton = new JButton ("upload");
		uploadButton.setBorder (BorderFactory.createLineBorder(Color.BLACK));
		uploadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				Runnable runner = new Runnable() 
				{				
					public void run() 
					{
						File fileToUpload = null;
						try
						{
							fileToUpload = filesPanel.getSelectedFile();
							String remoteAE = me.parent.getAE();
							String hostname = me.parent.getIP();
							int port = me.parent.getPort();
							System.out.println (remoteAE+" "+hostname+" "+port);
							if (remoteAE.length()<=0 || hostname.length()<=0 || port<0 || port>65535)
								JOptionPane.showMessageDialog(me,"Incorrect server paramater(s).","Error",JOptionPane.ERROR_MESSAGE);
							else
							{
								new DCMSender().sendFiles(remoteAE, hostname, port, new String[] { fileToUpload.getPath() });
								me.parent.setMessage("\""+fileToUpload.getAbsolutePath()+"\" uploaded.");
							}
						}
						catch (NullPointerException e)
						{
							JOptionPane.showMessageDialog(me,"No file to upload selected.","Error",JOptionPane.ERROR_MESSAGE);
						}
						catch (Exception e) 
						{
							JOptionPane.showMessageDialog(me,fileToUpload.getAbsolutePath()+" upload failed.","Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				};
				Thread thread = new Thread (runner);
				thread.start();
			}
		});
		add (uploadButton,gbc);
		
		gbc.gridx = 2; gbc.gridy = 0;
		gbc.weighty = 1;
		shortInfoPanel = new ShortInfoPanel();
		add (shortInfoPanel,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0;
		showImageButton = new JButton ("show image");
		showImageButton.setBorder (BorderFactory.createLineBorder(Color.BLACK));
		showImageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				Runnable runner = new Runnable() 
				{				
					public void run() 
					{
						if (filesPanel.getSelectedFile()!=null)
						{
							DicomFile dicomFile = new DicomFile (filesPanel.getSelectedFile());
							dicomFile.loadImage();
							if (!dicomFile.loadImageError)
								dicomImageWindow = new DicomImageWindow (dicomFile);
							else 
								JOptionPane.showMessageDialog (me,"Loading image failed.","Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				};
				Thread thread = new Thread (runner);
				thread.start();
			}	
		});
		add (showImageButton,gbc);
		
		gbc.gridy = 2;
		showHeaderButton = new JButton ("show header");
		showHeaderButton.setBorder (BorderFactory.createLineBorder(Color.BLACK));
		showHeaderButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ae) 
			{
				Runnable runner = new Runnable() 
				{				
					public void run() 
					{
						try
						{
							@SuppressWarnings("unused")
							DicomHeaderWindow dicomHeaderWindow = new DicomHeaderWindow (filesPanel.getSelectedFile());
						}
						catch (NullPointerException e) {}
					}
				};
				Thread thread = new Thread (runner);
				thread.start();
			}
		});
		add (showHeaderButton,gbc);
		
		setVisible (true);
	}
}
