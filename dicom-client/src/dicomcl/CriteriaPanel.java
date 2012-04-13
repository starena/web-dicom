package dicomcl;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

public class CriteriaPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private JLabel PatientNameLabel;
	private JLabel PatientBirthDateLabel;
	private JLabel PatientSexLabel;
	private JLabel StudyDescriptionLabel;
	private JLabel ModalityLabel;
	private JLabel StudyDateLabel;
	private JTextField PatientNameTextField;
	private JTextField PatientBirthDateTextField;
	private JTextField PatientSexTextField;
	private JTextField StudyDescriptionTextField;
	private JTextField ModalityTextField;
	private JTextField StudyDateTextField;
	public JButton SearchButton;
	
	public String getPatientName()
	{
		return PatientNameTextField.getText();
	}
	
	public String getPatientBirthDate()
	{
		return PatientBirthDateTextField.getText().replace("-","");
	}
	
	public String getPatientSex()
	{
		return PatientSexTextField.getText();
	}
	
	public String getStudyDescription()
	{
		return StudyDescriptionTextField.getText();
	}
	
	public String getModality()
	{
		return ModalityTextField.getText();
	}
	
	public String getStudyDate()
	{
		return StudyDateTextField.getText();
	}
	
	
	public CriteriaPanel()
	{
		setBorder (BorderFactory.createTitledBorder (new LineBorder(Color.BLACK),"Criteria"));	
		SpringLayout layout = new SpringLayout();
		setLayout (layout);
		
		Dimension LabelSize = new Dimension (70,20);
		Dimension TextFieldSize = new Dimension (120,20);

		PatientNameLabel = new JLabel ("Pat. name: ");
		PatientNameLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,PatientNameLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,PatientNameLabel,4,SpringLayout.NORTH,this);
		add (PatientNameLabel);
		
		PatientNameTextField = new JTextField();
		PatientNameTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint(SpringLayout.WEST,PatientNameTextField,4,SpringLayout.EAST,PatientNameLabel);
		layout.putConstraint(SpringLayout.NORTH,PatientNameTextField,4,SpringLayout.NORTH,this);
		add (PatientNameTextField);
		
		PatientBirthDateLabel = new JLabel ("Pat. birth: ");
		PatientBirthDateLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,PatientBirthDateLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,PatientBirthDateLabel,4,SpringLayout.SOUTH,PatientNameLabel);
		add (PatientBirthDateLabel);
		
		PatientBirthDateTextField = new JTextField();
		PatientBirthDateTextField.setToolTipText("YYYY-MM-DD");
		PatientBirthDateTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint(SpringLayout.WEST,PatientBirthDateTextField,4,SpringLayout.EAST,PatientBirthDateLabel);
		layout.putConstraint(SpringLayout.NORTH,PatientBirthDateTextField,4,SpringLayout.SOUTH,PatientNameTextField);
		add (PatientBirthDateTextField);
		
		PatientSexLabel = new JLabel ("Pat. sex: ");
		PatientSexLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,PatientSexLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,PatientSexLabel,4,SpringLayout.SOUTH,PatientBirthDateLabel);
		add (PatientSexLabel);
		
		PatientSexTextField = new JTextField();
		PatientSexTextField.setToolTipText ("F/M");
		PatientSexTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint(SpringLayout.WEST,PatientSexTextField,4,SpringLayout.EAST,PatientSexLabel);
		layout.putConstraint(SpringLayout.NORTH,PatientSexTextField,4,SpringLayout.SOUTH,PatientBirthDateTextField);
		add (PatientSexTextField);
		
		StudyDescriptionLabel = new JLabel ("Study: ");
		StudyDescriptionLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,StudyDescriptionLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,StudyDescriptionLabel,4,SpringLayout.SOUTH,PatientSexLabel);
		add (StudyDescriptionLabel);
		
		StudyDescriptionTextField = new JTextField();
		StudyDescriptionTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint(SpringLayout.WEST,StudyDescriptionTextField,4,SpringLayout.EAST,StudyDescriptionLabel);
		layout.putConstraint(SpringLayout.NORTH,StudyDescriptionTextField,4,SpringLayout.SOUTH,PatientSexTextField);
		add (StudyDescriptionTextField);
		
		ModalityLabel = new JLabel ("Modality: ");
		ModalityLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,ModalityLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,ModalityLabel,4,SpringLayout.SOUTH,StudyDescriptionLabel);
		add (ModalityLabel);
		
		ModalityTextField = new JTextField();
		ModalityTextField.setPreferredSize (TextFieldSize);
		layout.putConstraint(SpringLayout.WEST,ModalityTextField,4,SpringLayout.EAST,ModalityLabel);
		layout.putConstraint(SpringLayout.NORTH,ModalityTextField,4,SpringLayout.SOUTH,StudyDescriptionTextField);
		add (ModalityTextField);
		
		StudyDateLabel = new JLabel ("Study date: ");
		StudyDateLabel.setPreferredSize (LabelSize);
		layout.putConstraint(SpringLayout.WEST,StudyDateLabel,10,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,StudyDateLabel,4,SpringLayout.SOUTH,ModalityLabel);
		add (StudyDateLabel);
		
		StudyDateTextField = new JTextField();
		StudyDateTextField.setPreferredSize (TextFieldSize);
		StudyDateTextField.setToolTipText("YYYY-MM-DD");
		layout.putConstraint(SpringLayout.WEST,StudyDateTextField,4,SpringLayout.EAST,StudyDateLabel);
		layout.putConstraint(SpringLayout.NORTH,StudyDateTextField,4,SpringLayout.SOUTH,ModalityTextField);
		add (StudyDateTextField);
		
		setPreferredSize (new Dimension(220,180));
		setMinimumSize (new Dimension(220,180));
		setVisible (true);
		
	}

}
