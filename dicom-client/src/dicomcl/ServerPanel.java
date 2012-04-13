package dicomcl;

import java.awt.Color;
import java.awt.Dimension;
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

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.Tag;

public class ServerPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private ServerParametersPanel serverParametersPanel;
	private CriteriaPanel criteriaPanel;
	private FilesPanel filesPanel;
	private ShortInfoPanel infoPanel;
	private JButton searchButton, downloadButton;
	private MainWindow parent;
	private ServerPanel me;
	
	public String getAE()
	{
		return serverParametersPanel.getAE();
	}
	
	public String getIP()
	{
		return serverParametersPanel.getIP();
	}
	
	public int getPort()
	{
		return serverParametersPanel.getPort();
	}
	
	public ServerPanel (MainWindow parent)
	{
		this.parent = parent;
		me = this;
		Dimension minSize = new Dimension (200,100);
		setBorder (BorderFactory.createTitledBorder (new LineBorder(Color.BLUE),"Server"));
		setLayout (new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.25; gbc.weighty = 1;
		gbc.insets = new Insets (1,1,1,1);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 0;
		serverParametersPanel = new ServerParametersPanel();
		serverParametersPanel.setMinimumSize (minSize);
		this.add (serverParametersPanel,gbc);
		
		gbc.gridx = 1;
		gbc.gridheight = 1;
		criteriaPanel = new CriteriaPanel();
		criteriaPanel.setMinimumSize (minSize);
		this.add (criteriaPanel,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0;
		searchButton = new JButton ("search");
		searchButton.setBorder (BorderFactory.createLineBorder(Color.BLACK));
		searchButton.addActionListener (new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				Runnable runner = new Runnable() 
				{				
					public void run() 
					{
						try
						{
							DCMQuery query = new DCMQuery();
							query.addMatchingKey (Tag.PatientName,criteriaPanel.getPatientName());
							query.addMatchingKey (Tag.PatientBirthDate,criteriaPanel.getPatientBirthDate());
							query.addMatchingKey (Tag.PatientSex,criteriaPanel.getPatientSex());
							query.addMatchingKey (Tag.StudyDate,criteriaPanel.getStudyDate());
							query.addMatchingKey (Tag.StudyDescription,criteriaPanel.getStudyDescription());
							query.addMatchingKey (Tag.Modality,criteriaPanel.getModality());
							query.query (getAE(),getIP(),getPort());
							filesPanel.updateList (query.getResult().toArray(new BasicDicomObject[0]));
						}
						catch (Exception e)
						{
							
						}
					}
				};
				Thread thread = new Thread (runner);
				thread.start();
			}
		});
		add (searchButton,gbc);
		
		gbc.gridx = 2; gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.weightx = 1;
		filesPanel = new FilesPanel();
		filesPanel.setMinimumSize (minSize);
		filesPanel.itemsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged (ListSelectionEvent e) 
			{
				BasicDicomObject object = filesPanel.getSelectedDicomObject();
				infoPanel.updateInfo (object);
			}	
		});
		this.add (filesPanel,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0;
		downloadButton = new JButton ("download");
		downloadButton.setBorder (BorderFactory.createLineBorder(Color.BLACK));
		downloadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae) 
			{
				Runnable runner = new Runnable() 
				{				
					public void run() 
					{
						@SuppressWarnings("unused")
						BasicDicomObject fileToDownload = filesPanel.getSelectedDicomObject();
						File dirToDownloadTo = me.parent.getSelectedDir();
						try
						{
							//TODO download
							DCMQuery query = new DCMQuery();
							query.addMatchingKey (Tag.PatientName,fileToDownload.getString(Tag.PatientName));
							query.addMatchingKey (Tag.PatientBirthDate,fileToDownload.getString(Tag.PatientBirthDate));
							query.addMatchingKey (Tag.PatientSex,fileToDownload.getString(Tag.PatientSex));
							query.addMatchingKey (Tag.StudyDate,fileToDownload.getString(Tag.StudyDate));
							query.addMatchingKey (Tag.StudyDescription,fileToDownload.getString(Tag.StudyDescription));
							query.addMatchingKey (Tag.Modality,fileToDownload.getString(Tag.Modality));
							query.addMatchingKey (Tag.AccessoryCode, "5000-download");
							query.query (getAE(),getIP(),getPort());
							me.parent.setMessage("File succesfully downloaded to \""+dirToDownloadTo.getAbsolutePath()+"\".");
						}
						catch (NullPointerException e)
						{
							JOptionPane.showMessageDialog(me,"No directory or file to download to selected.","Error",JOptionPane.ERROR_MESSAGE);
						}
						catch (Exception e)
						{
							JOptionPane.showMessageDialog(me,"Download failed.","Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				};
				Thread thread = new Thread (runner);
				thread.start();
			}
		});
		add (downloadButton,gbc);
		
		gbc.gridx = 3; gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.gridheight = 2;
		infoPanel = new ShortInfoPanel();
		infoPanel.setMinimumSize (minSize);
		add (infoPanel,gbc);
		
		setMinimumSize (new Dimension(700,230));
		setVisible (true);
	}
}
