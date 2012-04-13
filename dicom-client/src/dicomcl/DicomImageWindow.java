package dicomcl;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class DicomImageWindow extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private DCMImagePanel imagePanel;
	private JScrollPane imageScrollPane;
	private DicomFile dicomFile;
	private Dimension screenSize;
	
	private void initializeComponents()
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		
		dicomFile.loadImage();
		imagePanel = new DCMImagePanel (dicomFile.getImage());
		imageScrollPane = new JScrollPane (imagePanel);
		if (dicomFile.getImage().getWidth()>screenSize.getWidth()-10 || dicomFile.getImage().getHeight()>screenSize.getHeight()-20)
			imageScrollPane.setPreferredSize(new Dimension((int)screenSize.getWidth()-10,(int)screenSize.getHeight()-20));
		this.add (imageScrollPane);
	}

	public DicomImageWindow (DicomFile dicomFile)
	{
		super (dicomFile.getFileName());
		this.dicomFile = dicomFile;
		initializeComponents();
		pack();
		setLocation ((int)(screenSize.getWidth()-this.getWidth())/2,(int)(screenSize.getHeight()-this.getHeight())/2);
		setResizable(false);
		setVisible (true);
	}
}
