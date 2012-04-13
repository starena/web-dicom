package org.dicomsrv.database;

public interface DatabaseConnection {
	
	void open(String username, String password, String address, int port, String database) throws Exception;
	Object executeQuery(String query) throws Exception;
	Object executeUpdate(String query) throws Exception;
	String getString(String columnName);
	int getInt(String columnName);
	float getFloat(String columnName);
	String getString(int columnIndex);
	int getInt(int columnIndex);
	float getFloat(int columnIndex);
	boolean nextRow();
	int getRowNumber();
	void close();
	
}
