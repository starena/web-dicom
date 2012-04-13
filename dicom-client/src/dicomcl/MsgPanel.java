package dicomcl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MsgPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private JLabel message;
	
	public MsgPanel()
	{
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.insets = new Insets (0,0,0,10);
		gbc.fill = GridBagConstraints.VERTICAL;
		
		message = new JLabel ("Welcome!");
		this.add (message,gbc);
		
		this.setVisible (true);
	}
	
	public void setMessage (String msg)
	{
		message.setText (msg);
		//repaint();
	}

}
