package org.dicomsrv.database;

import java.sql.*;

public class MySQLConnection implements DatabaseConnection {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	@Override
	public void open(String username, String password, String address,
			int port, String database) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + address
				+ ":" + port + "/"+database, username, password);
		statement = connection.createStatement();
	}

	@Override
	public void close() {
		try {
			if (connection != null && statement != null) {
				statement.close();
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet executeQuery(String query) throws SQLException {
		resultSet = statement.executeQuery(query);
		return resultSet;
	}

	@Override
	public Object executeUpdate(String query) throws SQLException {
		int result = 0;
		result = statement.executeUpdate(query);
		return result;
	}

	@Override
	public float getFloat(int columnIndex) {
		try {
			return resultSet.getFloat(columnIndex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public float getFloat(String columnName) {
		try {
			return resultSet.getFloat(columnName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getInt(int columnIndex) {
		try {
			return resultSet.getInt(columnIndex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getInt(String columnName) {
		try {
			return resultSet.getInt(columnName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getString(int columnIndex) {
		try {
			return resultSet.getString(columnIndex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getString(String columnName) {
		try {
			return resultSet.getString(columnName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean nextRow() {
		try {
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getRowNumber() {
		try {
			return resultSet.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
