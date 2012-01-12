package org.psystems.dicom.pdfview.server;

import java.util.ArrayList;

import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;
import org.psystems.dicom.pdfview.client.PdfService;
import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PdfServiceImpl extends RemoteServiceServlet implements PdfService {

	@Override
	public ArrayList<ConfigTemplateDto> getTemplates()
			throws IllegalArgumentException {
		
		ArrayList<ConfigTemplateDto> result = new ArrayList<ConfigTemplateDto>();
		for (ConfigTemplate tmpl : Config.getTemplates()) {
			ConfigTemplateDto tmplDto = new ConfigTemplateDto();
			tmplDto.setDescription(tmpl.getDescription());
			tmplDto.setFontsize(tmpl.getFontsize());
			tmplDto.setModality(tmpl.getModality());
			tmplDto.setName(tmpl.getName());
			result.add(tmplDto);
		}
		return result;
	}

}
