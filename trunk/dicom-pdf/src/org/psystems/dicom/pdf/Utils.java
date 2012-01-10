package org.psystems.dicom.pdf;

import java.util.ArrayList;

import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;

public class Utils {

    public ArrayList<ConfigTemplate> getPDFTemplates() {
	return Config.getTemplates();
    }
}
