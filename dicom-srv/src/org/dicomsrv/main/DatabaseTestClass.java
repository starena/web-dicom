package org.dicomsrv.main;

import org.dicomsrv.database.DatabaseConnection;
import org.dicomsrv.database.MySQLConnection;

public class DatabaseTestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseConnection c = new MySQLConnection();
		try {
			c.open("dicomsrv", "qwerty", "localhost", 3306, "dicomsrv");
			c.executeQuery("SELECT * FROM test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (c.nextRow())
			System.out.println(c.getString("username") +" "+ c.getString(3));
		c.close();
	}

}
