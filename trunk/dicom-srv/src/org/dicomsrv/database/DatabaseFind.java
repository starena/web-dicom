package org.dicomsrv.database;

import java.util.Map;

import org.dcm4che2.data.DicomObject;

public class DatabaseFind {

    private final DatabaseConnection connection = new MySQLConnection();

    private String query;
        
	public String generateQuery(DicomObject keys) {
		boolean isWhereClause = false;
		query = "SELECT * FROM "+DatabaseData.DB_TABLENAME;

		// TODO: na razie same stringi
		for (Map.Entry<Integer, String> searchFieldEntry : DatabaseData.searchFields.entrySet()) {
			if (keys.contains(searchFieldEntry.getKey()) && keys.getString(searchFieldEntry.getKey()) != null) {
				if (!isWhereClause) { query += " WHERE "; isWhereClause = true; }
				query += searchFieldEntry.getValue() + " = '" + keys.getString(searchFieldEntry.getKey()) + "' AND ";
			}
		}
				
		// usuwanie and'a
		if (isWhereClause)
			query = query.substring(0, query.lastIndexOf(" AND "));
		return query;
	}
	
	public void executeFindQuery() { 
		try {
			connection.open(
					DatabaseData.DB_USERNAME,
					DatabaseData.DB_PASSWORD,
					DatabaseData.DB_HOSTNAME,
					DatabaseData.DB_PORT,
					DatabaseData.DB_DATABASENAME
			);
			connection.executeQuery(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasMoreResults() {
		return connection.nextRow();
	}
	
	public int getResultNumber() {
		return connection.getRowNumber();
	}
	
	public void closeConnection() {
		connection.close();
	}
	
	public String getResultPathName() {
		return connection.getString(DatabaseData.DB_FILEPATHCOLUMNNAME);
	}
	
}