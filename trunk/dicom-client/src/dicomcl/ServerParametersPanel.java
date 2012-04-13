package dicomcl;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

public class ServerParametersPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel AELabel;
	private JTextField AETextField;
	private JLabel IPLabel;
	private JTextField IPTextField;
	private JLabel PortLabel;
	private JTextField PortTextField;
	
	public String getAE()
	{
		return AETextField.getText();
	}
	
	public String getIP()
	{
		return IPTextField.getText();
	}
	
	public int getPort() throws NumberFormatException
	{
		try
		{
			int port = Integer.parseInt (PortTextField.getText());
			return port;
		}
		catch (NumberFormatException nfe)
		{
			throw nfe;
		}
	}
	
	public ServerParametersPanel()
	{
		Dimension LabelSize = new Dimension (30,20);
		Dimension TextFieldSize = new Dimension (100,20);
		
		setBorder (BorderFactory.createTitledBorder(new LineBorder(Color.BLACK),"Server parameters"));	
		SpringLayout layout = new SpringLayout();
		setLayout (layout);
		
		AELabel = new JLabel ("AE:");
		AELabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,AELabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,AELabel,4,SpringLayout.NORTH,this);
		add (AELabel);
		
		AETextField = new JTextField ("SERVER");
		AETextField.setPreferredSize (TextFieldSize);
		layout.putConstraint (SpringLayout.WEST,AETextField,4,SpringLayout.EAST,AELabel);
		layout.putConstraint (SpringLayout.NORTH,AETextField,4,SpringLayout.NORTH,this);
		add (AETextField);
		
		IPLabel = new JLabel ("IP:");
		IPLabel.setPreferredSize (LabelSize);
		layout.putConstraint (SpringLayout.WEST,IPLabel,10,SpringLayout.WEST,this);
		layout.putConstraint (SpringLayout.NORTH,IPLabel,4,SpringLayout.SOUTH,AELabel);
		add (IPLabel);
		
		IPTextField = new JTextField ("localhost");
		IPTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint (SpringLayout.WEST,IPTextField,4,SpringLayout.EAST,IPLabel);
		layout.putConstraint (SpringLayout.NORTH,IPTextField,4,SpringLayout.SOUTH,AETextField);
		add (IPTextField);
		
		PortLabel = new JLabel ("Port:");
		PortLabel.setPreferredSize (LabelSize);
		layout.putConstraint (SpringLayout.WEST,PortLabel,10,SpringLayout.WEST,this);
		layout.putConstraint (SpringLayout.NORTH,PortLabel,4,SpringLayout.SOUTH,IPLabel);
		add (PortLabel);
		
		PortTextField = new JTextField ("11111");
		PortTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint (SpringLayout.WEST,PortTextField,4,SpringLayout.EAST,PortLabel);
		layout.putConstraint (SpringLayout.NORTH,PortTextField,4,SpringLayout.SOUTH,IPTextField);
		add (PortTextField);

		setPreferredSize (new Dimension(160,90));
		setMinimumSize (new Dimension(160,90));
		setVisible (true);
	}
}
