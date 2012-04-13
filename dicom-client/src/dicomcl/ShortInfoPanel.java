package dicomcl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.dcm4che2.data.BasicDicomObject;

public class ShortInfoPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollpane;
	private JTextArea infoTextArea;
	
	public void updateInfo (BasicDicomObject object)
	{
		if (object != null)
		{
			infoTextArea.replaceRange("",0,infoTextArea.getDocument().getLength());
			DicomFile dcmFile = new DicomFile (object);
			infoTextArea.append (dcmFile.getShortInfo());
		}
	}
	
	public void updateInfo (File file)
	{
		infoTextArea.replaceRange("",0,infoTextArea.getDocument().getLength());
		DicomFile dcmFile = new DicomFile (file);
		infoTextArea.append (dcmFile.getShortInfo());
	}
	
	public ShortInfoPanel()
	{
		setBorder (BorderFactory.createTitledBorder (new LineBorder(Color.BLACK),"Info"));
		setLayout (new BorderLayout());
	
		infoTextArea = new JTextArea();
		infoTextArea.setEditable (false);
		
		scrollpane = new JScrollPane();
	    scrollpane.getViewport().add (infoTextArea);
	    scrollpane.setBorder(BorderFactory.createLineBorder (Color.LIGHT_GRAY));
	    add (scrollpane);
	    
		setPreferredSize(new Dimension (120,120));
		setMinimumSize(new Dimension (120,120));
	    setVisible (true);
	}
}
