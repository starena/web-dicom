package dicomcl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class DicomHeaderWindow extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private JTextField [][] textFields;
	private JScrollPane scrollPane;
	private JPanel panel;
	private int arraySize;
	
	private void addTextFields()
	{
		panel = new JPanel();
		panel.setLayout (new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1; gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for (int i=0; i<arraySize; i++)
		{
			if (textFields[1][i].getText()!=null && textFields[1][i].getText().length()>0)
			{
				gbc.gridy = i;
				gbc.gridx = 0;
				panel.add (textFields[0][i],gbc);
				gbc.gridx = 1;
				panel.add (textFields[1][i],gbc);
			}
		}
	}
	
	private void addPanel()
	{
		scrollPane = new JScrollPane (panel);
		scrollPane.setHorizontalScrollBarPolicy (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add (scrollPane);
	}
	
	public DicomHeaderWindow (File file)
	{
		super (file.getName());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		DicomHeader dicomHeader = new DicomHeader (file);
		if (!dicomHeader.loadFileError)
		{
			arraySize = dicomHeader.getTextsNo();
			textFields = new JTextField [2][arraySize];
			String [] textsNames = dicomHeader.getTags();
			String [] textsValues = dicomHeader.getValues();
			for (int i=0; i<arraySize; i++)
			{
				textFields[0][i] = new JTextField (textsNames[i]);
				textFields[0][i].setEditable(false);
				textFields[1][i] = new JTextField (textsValues[i]);
				textFields[1][i].setEditable(false);
			}
			addTextFields();
			addPanel();
			if (getPreferredSize().getHeight()>d.getHeight()-50)
				setPreferredSize (new Dimension ((int)getPreferredSize().getWidth()+20,(int)d.getHeight()-100));
			else
				setPreferredSize (new Dimension ((int)getPreferredSize().getWidth()+20,(int)getPreferredSize().getHeight()));
			setMaximumSize (new Dimension ((int)d.getWidth()-100,(int)d.getHeight()-100));
			pack();
			setLocation ((int)(d.getWidth()-this.getWidth())/2,(int)(d.getHeight()-this.getHeight())/2);
			setVisible(true);
		}
		else JOptionPane.showMessageDialog (this,"Loading file failed.","Error",JOptionPane.ERROR_MESSAGE);
	}

}
