package org.dicomsrv.database;

import java.util.HashMap;
import java.util.Map;

import org.dcm4che2.data.Tag;

public class DatabaseData {

	public static String DB_USERNAME = "dicomsrv";
	
	public static String DB_PASSWORD = "dicomsrv";

	public static String DB_HOSTNAME = "localhost";
	
	public static int DB_PORT = 3306;
	
	public static String DB_DATABASENAME = "dicomsrv";
	
	public static String DB_TABLENAME = "studies";
	
	public static String DB_FILEPATHCOLUMNNAME = "Path";
	
	@SuppressWarnings("serial")
	public static Map<Integer, String> searchFields = new HashMap<Integer, String>() {
		{
			put(Tag.PatientName, "PatientName");
			put(Tag.PatientBirthDate, "PatientBirthDate");
			put(Tag.PatientSex, "PatientSex");
			put(Tag.StudyDate, "StudyDate");
			put(Tag.Modality, "Modality");
			put(Tag.StudyDescription, "StudyDescription");
		}
	};
	
}
