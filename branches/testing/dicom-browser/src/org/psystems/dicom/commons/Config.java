package org.psystems.dicom.commons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

    private static Logger logger = Logger.getLogger(Config.class.getName());

    private static ArrayList<ConfigDevice> devices = new ArrayList<ConfigDevice>();

    private static String aet;
    private static String host;
    private static String port;
    private static String incomingFolder;
    private static String dbDriver;
    private static String dbUrl;
    private static String dbJndi;
    private static String tmpFolder;
    private static String templateFolder;
    private static String configPdf;
    private static String configJpg;
    private static String dbOmitsUrl;
    private static String dbOmitsDriver;
    private static String dbOmitsJndi;

    // Инициализация конфига
    static {
	try {
	    if (System.getenv("WEBDICOM_HOME") != null) {
		loadConfig(System.getenv("WEBDICOM_HOME") + "/conf/conf.xml");
	    } else if (System.getProperty("webdicom.home") != null) {
		loadConfig(System.getProperty("webdicom.home") + "/conf/conf.xml");
	    } else {
		loadConfig("conf/conf.xml");
	    }
	} catch (Exception e) {
	    logger.fatal("Error load config file! ", e);
	    e.printStackTrace();
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

	try {
	    new Config();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public static void loadConfig(String file) throws ParserConfigurationException, SAXException, IOException {

	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	Document doc = docBuilder.parse(new File(file));

	// normalize text representation
	doc.getDocumentElement().normalize();

	// System.out.println("Root element of the doc is "
	// + doc.getDocumentElement().getNodeName());

	aet = doc.getElementsByTagName("dicom-connection").item(0).getAttributes().getNamedItem("aet").getNodeValue();

	host = doc.getElementsByTagName("dicom-connection").item(0).getAttributes().getNamedItem("host").getNodeValue();

	port = doc.getElementsByTagName("dicom-connection").item(0).getAttributes().getNamedItem("port").getNodeValue();

	incomingFolder = "" + doc.getElementsByTagName("study-incoming-folder").item(0).getFirstChild().getNodeValue();

	tmpFolder = "" + doc.getElementsByTagName("tmp-folder").item(0).getFirstChild().getNodeValue();

	templateFolder = "" + doc.getElementsByTagName("template-folder").item(0).getFirstChild().getNodeValue();

	configPdf = "" + doc.getElementsByTagName("conf-pdf").item(0).getFirstChild().getNodeValue();

	configJpg = "" + doc.getElementsByTagName("conf-jpg").item(0).getFirstChild().getNodeValue();

	dbDriver = doc.getElementsByTagName("db-connection").item(0).getAttributes().getNamedItem("driver")
		.getNodeValue();

	dbUrl = doc.getElementsByTagName("db-connection").item(0).getAttributes().getNamedItem("url").getNodeValue();

	if (doc.getElementsByTagName("db-connection").item(0).getAttributes().getNamedItem("jndi") != null) {
	    dbJndi = doc.getElementsByTagName("db-connection").item(0).getAttributes().getNamedItem("jndi")
		    .getNodeValue();
	}

	dbOmitsUrl = doc.getElementsByTagName("omits-connection").item(0).getAttributes().getNamedItem("url")
		.getNodeValue();

	dbOmitsDriver = doc.getElementsByTagName("omits-connection").item(0).getAttributes().getNamedItem("driver")
		.getNodeValue();

	if (doc.getElementsByTagName("omits-connection").item(0).getAttributes().getNamedItem("jndi") != null) {
	    dbOmitsJndi = doc.getElementsByTagName("omits-connection").item(0).getAttributes().getNamedItem("jndi")
		    .getNodeValue();
	}

	System.out.println("dicomconnect=" + aet + "@" + host + ":" + port + "; incomingFolder=" + incomingFolder
		+ "; tmpFolder=" + tmpFolder + "; templateFolder=" + templateFolder + "; configPdf=" + configPdf
		+ "; configJpg=" + configJpg + "; db=(" + dbDriver + ")" + dbUrl + " jndi:" + dbJndi + "; omits="
		+ dbOmitsUrl + " jndi:" + dbOmitsJndi);

	NodeList listOfConnector = doc.getElementsByTagName("dicom-driver");

	System.out.println("Total dcmconnector's : " + listOfConnector.getLength());

	for (int s = 0; s < listOfConnector.getLength(); s++) {

	    Node connector = listOfConnector.item(s);

	    String aet = connector.getAttributes().getNamedItem("aet").getNodeValue();
	    String name = connector.getAttributes().getNamedItem("name").getNodeValue();
	    String driver = connector.getAttributes().getNamedItem("driver").getNodeValue();
	    System.out.println(" driver name=" + name + "; aet=" + aet + "; driver=" + driver);

	    // DcmConnector conn = new DcmConnector(aet, name, driver);
	    // connectors.add(conn);

	}

	// Ищем секцию <device>
	NodeList listOfDevices = doc.getElementsByTagName("device");

	System.out.println("Device's count : " + listOfDevices.getLength());

	for (int s = 0; s < listOfDevices.getLength(); s++) {

	    Node deviceNode = listOfDevices.item(s);

	    String modality = deviceNode.getAttributes().getNamedItem("modality").getNodeValue();
	    String name = deviceNode.getAttributes().getNamedItem("name").getNodeValue();
	    String description = deviceNode.getAttributes().getNamedItem("description").getNodeValue();

	    System.out.println("[" + s + "]\t modality:[" + modality + "] name:[" + name + "] description:["
		    + description + "]");

	    ConfigDevice device = new ConfigDevice(modality, name, description);

	    // Ищем секцию <driver>

	    for (int i = 0; i < deviceNode.getChildNodes().getLength(); i++) {
		Node driverNode = deviceNode.getChildNodes().item(i);
		if (driverNode.getNodeName().equals("driver")) {

		    String aet = null;
		    String javaclass = null;

		    if (driverNode.getAttributes().getNamedItem("aet") != null) {
			aet = driverNode.getAttributes().getNamedItem("aet").getNodeValue();
			System.out.println("\t driver aet=[" + aet + "]");
		    }
		    if (driverNode.getAttributes().getNamedItem("class") != null) {
			javaclass = driverNode.getAttributes().getNamedItem("class").getNodeValue();
			System.out.println("\t driver class=[" + javaclass + "]");
		    }

		    ConfigDeviceDriver driver = new ConfigDeviceDriver(aet, javaclass);
		    device.setDriver(driver);

		    // Ищем секцию <condition> - условия для связки (через теги)
		    for (int j = 0; j < driverNode.getChildNodes().getLength(); j++) {
			Node conditionNode = driverNode.getChildNodes().item(j);

			String tag = null;
			String type = null;
			String value = null;

			if (conditionNode.getNodeName().equals("condition")) {
			    tag = conditionNode.getAttributes().getNamedItem("tag").getNodeValue();
			    type = conditionNode.getAttributes().getNamedItem("type").getNodeValue();
			    value = conditionNode.getAttributes().getNamedItem("value").getNodeValue();
			    
			    ConfigDeviceDriverCondition condition = new ConfigDeviceDriverCondition(tag, type, value);
			    driver.addCondition(condition);
			    
			    System.out.println("\t\t tag tag[" + tag + "]");
			    System.out.println("\t\t tag type[" + type + "]");
			    System.out.println("\t\t tag value[" + value + "]");
			}
		    }

		}
	    }

	    System.out.println("====================================================================");
	    
	    devices.add(device);
	}
    }

    public static ArrayList<ConfigDevice> getDevices() {
	return devices;
    }

    public static String getAet() {
	return aet;
    }

    public static String getHost() {
	return host;
    }

    public static String getPort() {
	return port;
    }

    public static String getIncomingFolder() {
	return incomingFolder;
    }

    public static String getDbDriver() {
	return dbDriver;
    }

    public static String getDbUrl() {
	return dbUrl;
    }

    public static String getDbJndi() {
	return dbJndi;
    }

    public static String getTmpFolder() {
	return tmpFolder;
    }

    public static String getTemplateFolder() {
	return templateFolder;
    }

    public static String getConfigPdf() {
	return configPdf;
    }

    public static String getConfigJpg() {
	return configJpg;
    }

    public static String getOmits() {
	return dbOmitsUrl;
    }

    public static String getDbOmitsDriver() {
	return dbOmitsDriver;
    }

    public static String getDbOmitsUrl() {
	return dbOmitsUrl;
    }

    public static String getDbOmitsJndi() {
	return dbOmitsJndi;
    }

}
