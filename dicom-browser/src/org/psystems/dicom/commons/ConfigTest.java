package org.psystems.dicom.commons;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class ConfigTest {

    @Test
    public void testLoadConfig() throws ParserConfigurationException, SAXException, IOException {
	
	System.setProperty("webdicom.home", ".");
	assertEquals(12, Config.getDevices().size());
	assertEquals(2, Config.getTemplates().size());
	ConfigTemplate template = Config.getTemplates().get(0);
	assertEquals("test_ES1.pdf", template.getName());
	assertEquals("ES", template.getModality());
	assertEquals("тестовый шаблон 1 ES", template.getDescription());
	assertEquals("13", template.getFontsize());
    }

}
