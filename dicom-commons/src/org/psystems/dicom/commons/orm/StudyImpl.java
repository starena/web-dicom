/**
 * 
 */
package org.psystems.dicom.commons.orm;

import java.util.List;

/**
 * @author dima_d
 * 
 */
public class StudyImpl extends Study {

	public static Study getInstance(long id) {
		StudyImpl stub = new StudyImpl();
		stub.setId(id);
		return stub;
	}

	public static List<Study> getStudues(String query) {
		// TODO Auto-generated method stub
		return null;
	}


}
