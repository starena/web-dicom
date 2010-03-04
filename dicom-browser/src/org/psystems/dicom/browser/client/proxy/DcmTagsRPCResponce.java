package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author dima_d Запрос на получение тегов
 * 
 */
public class DcmTagsRPCResponce implements RPCResponce, Serializable {

	private static final long serialVersionUID = -540089094742711175L;
	private ArrayList<DcmTagProxy> tagList; // Список тегов

	public ArrayList<DcmTagProxy> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<DcmTagProxy> tagList) {
		this.tagList = tagList;
	}

}
