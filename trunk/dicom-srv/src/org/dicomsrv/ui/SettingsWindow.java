package org.dicomsrv.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.dicomsrv.database.DatabaseConnection;
import org.dicomsrv.database.DatabaseData;
import org.dicomsrv.database.MySQLConnection;

public class SettingsWindow extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JTextField dbHostnameField = new JTextField(DatabaseData.DB_HOSTNAME);
	private final JTextField dbPortField = new JTextField(Integer.toString(DatabaseData.DB_PORT));
	private final JTextField dbUsernameField = new JTextField(DatabaseData.DB_USERNAME);
	private final JPasswordField dbPasswordField = new JPasswordField(DatabaseData.DB_PASSWORD);
	private final JTextField dbDatabaseNameField = new JTextField(DatabaseData.DB_DATABASENAME);
	private final JTextField dbTableNameField = new JTextField(DatabaseData.DB_TABLENAME);
	
	private final JTextArea console = new JTextArea(5,12);
	
	private final JButton okButton = new JButton("OK");
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton testButton = new JButton("Test!");
	private final JButton createStructureButton = new JButton("Create structure");

	private final JPanel settingsPanel = new JPanel();
	private final JPanel buttonsPanel = new JPanel();
	
	public SettingsWindow(JFrame owner) {
		super(owner, "MySQL Settings Window");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(400,260);
		setResizable(false);
		initializeComponents();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeComponents() {
		setLayout(new BorderLayout());
		
		settingsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		settingsPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 0;
		c.insets = new Insets(10,10,0,10);
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Hostname"),c);
		c.gridx = 1; c.gridy = 0;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbHostnameField, c);

		c.gridx = 0; c.gridy = 1;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Port"), c);
		c.gridx = 1; c.gridy = 1;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbPortField, c);

		c.gridx = 0; c.gridy = 2;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Username"), c);
		c.gridx = 1; c.gridy = 2;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbUsernameField, c);
		
		c.gridx = 0; c.gridy = 3;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Password"), c);
		c.gridx = 1; c.gridy = 3;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbPasswordField, c);

		c.gridx = 0; c.gridy = 4;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Database"), c);
		c.gridx = 1; c.gridy = 4;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbDatabaseNameField, c);

		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0; c.gridy = 5;
		c.weightx = 0.1; c.weighty = 0;
		settingsPanel.add(new JLabel("Table"), c);
		c.gridx = 1; c.gridy = 5;
		c.weightx = 0.9; c.weighty = 0;
		settingsPanel.add(dbTableNameField, c);
		
		okButton.addActionListener(this);
		okButton.setMnemonic(KeyEvent.VK_O);
		cancelButton.addActionListener(this);
		cancelButton.setMnemonic(KeyEvent.VK_A);
		testButton.addActionListener(this);
		testButton.setMnemonic(KeyEvent.VK_T);
		createStructureButton.addActionListener(this);
		createStructureButton.setMnemonic(KeyEvent.VK_U);

		buttonsPanel.add(testButton);
		buttonsPanel.add(createStructureButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		
		console.setVisible(false);
		console.setBorder(BorderFactory.createLineBorder(Color.black));
		console.setEditable(false);

		JScrollPane consoleScrollPane = new JScrollPane(console);

		add(settingsPanel, BorderLayout.PAGE_START);
		add(consoleScrollPane, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			try {
				int port = Integer.parseInt(dbPortField.getText());
				if (!(port >= 80 && port <= 65535))
					throw new NumberFormatException();
				DatabaseData.DB_HOSTNAME = dbHostnameField.getText();
				DatabaseData.DB_PORT = port;
				DatabaseData.DB_USERNAME = dbUsernameField.getText();
				DatabaseData.DB_PASSWORD = String.valueOf(dbPasswordField.getPassword());
				DatabaseData.DB_DATABASENAME = dbDatabaseNameField.getText();
				DatabaseData.DB_TABLENAME = dbTableNameField.getText();
				setVisible(false);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(null, 
						"Pole Port musi zawierać liczbę pomiędzy 80-65535",
						"Informacja",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getSource() == cancelButton) {
			setVisible(false);
		} else if (e.getSource() == testButton) {
			console.setVisible(true);
			setSize(400,370);
			testDatabase();
		} else if (e.getSource() == createStructureButton) {
			console.setVisible(true);
			setSize(400,370);
			testDatabase();
			createDatabaseStructure();
		}
	}

	private DatabaseConnection testConnection() {
		DatabaseConnection connection = new MySQLConnection();
		console.setText("");
		try {
			console.append("Checking fields.. ");
			
			int port = Integer.parseInt(dbPortField.getText());
			if (!(port >= 80 && port <= 65535))
				throw new NumberFormatException();
			
			console.append("DONE\n");
			console.append("Connecting to database.. ");
			
			connection.open(
					dbUsernameField.getText(),
					String.valueOf(dbPasswordField.getPassword()),
					dbHostnameField.getText(),
					port,
					dbDatabaseNameField.getText()
			);
			
			console.append("DONE\n");
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(null, 
					"Pole Port musi zawierać liczbę pomiędzy 80-65535",
					"Informacja",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		} catch (Exception e) {
			console.append("ERROR!\n" + e.getMessage() + "\n");
			return null;
		}
		return connection;	
	}
	
	private void testDatabase() {
		DatabaseConnection connection = testConnection();
		if (connection == null) return;
		
		try {			
			console.append("Executing query 'SELECT * FROM " + dbTableNameField.getText() + "'.. ");
			
			if (connection.executeQuery(
					"SELECT * FROM " + dbTableNameField.getText()) != null);
			
			console.append("DONE\n");
			console.append("Closing connection!");
			
			connection.close();
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(null, 
					"Pole Port musi zawierać liczbę pomiędzy 80-65535",
					"Informacja",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			console.append("ERROR!\n" + e.getMessage() + "\n");
		}
	}
	
	private void createDatabaseStructure() {
		DatabaseConnection connection = testConnection();
		if (connection == null) return;
		
		try {
			int port = Integer.parseInt(dbPortField.getText());	
			connection.open(
					dbUsernameField.getText(),
					String.valueOf(dbPasswordField.getPassword()),
					dbHostnameField.getText(),
					port,
					dbDatabaseNameField.getText()
			);
		
			console.append("Creating table: "+dbTableNameField.getText()+".. ");
			String query = "CREATE TABLE "
					+ dbTableNameField.getText()
					+ "(study_id int AUTO_INCREMENT PRIMARY KEY, ";

			// TODO: na razie same stringi
			for (Map.Entry<Integer, String> searchFieldEntry : DatabaseData.searchFields.entrySet()) {
				query += searchFieldEntry.getValue() + " VARCHAR(255), ";
			}
		
			query += DatabaseData.DB_FILEPATHCOLUMNNAME + " VARCHAR(255))";
						
			connection.executeUpdate(query);
			
			console.append("DONE\nClosing connection!\n");
			connection.close();
			
		} catch (Exception e) {
			console.append("ERROR!\n" + e.getMessage() + "\n");
		} 
	}
}
