package org.dicomsrv.database;

import java.util.Map;

import org.dcm4che2.data.DicomObject;

public class DatabaseStore {

	private final DatabaseConnection connection = new MySQLConnection();

	private String query;
	
	private String generateQuery(DicomObject dicomObject, String path) {
		query = "INSERT INTO " + DatabaseData.DB_TABLENAME 
			+ "(";
		
		for (String column : DatabaseData.searchFields.values())
			query += column + ", ";
			
		query += DatabaseData.DB_FILEPATHCOLUMNNAME + ")";
		
		query += " VALUES (";
		
		// TODO: na razie same stringi
		for (Map.Entry<Integer, String> searchFieldEntry : DatabaseData.searchFields.entrySet()) {
			if (dicomObject.contains(searchFieldEntry.getKey()) && dicomObject.getString(searchFieldEntry.getKey()) != null)
				query += "'" + dicomObject.getString(searchFieldEntry.getKey()) + "', ";
			else 
				query += "'', ";
		}
		
		path = path.replaceAll("\\\\","\\\\\\\\");
		query += "'" + path + "');"; 

		return query;
	}
	
	public DatabaseStore(DicomObject dicomObject, String path) {
		try {
			connection.open(
					DatabaseData.DB_USERNAME,
					DatabaseData.DB_PASSWORD,
					DatabaseData.DB_HOSTNAME,
					DatabaseData.DB_PORT,
					DatabaseData.DB_DATABASENAME
			);

			connection.executeUpdate(generateQuery(dicomObject, path));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	
	}	
}
