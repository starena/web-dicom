package dicomcl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.Tag;

public class FilesPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel listModel;
	public JList itemsList;
	private JScrollPane scrollpane;
	private File [] f_items;
	private BasicDicomObject [] d_items;
	private File dir;
	
	public File getDir()
	{
		return dir;
	}
	
	public FilesPanel ()
	{		
		setBorder (BorderFactory.createTitledBorder (new LineBorder(Color.BLACK),"Files"));
		setLayout (new BorderLayout());
		
		listModel = new DefaultListModel();
		itemsList = new JList (listModel);
		
		scrollpane = new JScrollPane();
	    scrollpane.getViewport().add (itemsList);
	    scrollpane.setBorder(BorderFactory.createLineBorder (Color.LIGHT_GRAY));
	    add (scrollpane);
		
	    setPreferredSize(new Dimension (120,120));
		setMinimumSize(new Dimension (120,120));
		setVisible (true);
	}
	
	public File getSelectedFile()
	{
		int index = itemsList.getSelectedIndex();
		if (index>=0) return f_items[index];
		else return null;
	}
	
	public BasicDicomObject getSelectedDicomObject()
	{
		int index = itemsList.getSelectedIndex();
		if (index>=0) return d_items[index];
		else return null;
	}
	
	public void updateList (BasicDicomObject [] files)
	{
		d_items = null;
		d_items = files.clone();
		listModel.clear();
		try
		{
			for (int i=0; i<d_items.length; i++)
				listModel.add (i,d_items[i].getString(Tag.StudyInstanceUID));
		}
		catch (NullPointerException e) {}
	}
	
	public void updateList (File dir)
	{
		f_items = null;
		this.dir = dir;
		listModel.clear();
		try
		{
			f_items = dir.listFiles (new DCMFileFilter());
			for (int i=0; i<f_items.length; i++) 
				listModel.add (i,f_items[i].getName());
		}
		catch (NullPointerException e) {}	
	}	
}
