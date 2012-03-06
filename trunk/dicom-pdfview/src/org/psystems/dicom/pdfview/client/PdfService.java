package org.psystems.dicom.pdfview.client;

import java.util.ArrayList;

import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface PdfService extends RemoteService {

	ArrayList<ConfigTemplateDto> getTemplates() throws IllegalArgumentException;

	ArrayList<FormFieldDto> getFormFields(String tmplName)
			throws IllegalArgumentException;

	void makePdf(String tmplName, ArrayList<FormFieldDto> fields)
			throws IllegalArgumentException;

}
