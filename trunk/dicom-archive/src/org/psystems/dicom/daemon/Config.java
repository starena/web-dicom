package org.psystems.dicom.daemon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.derby.tools.sysinfo;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

	private static Logger logger = Logger.getLogger(Config.class.getName());

	private static ArrayList<DcmConnector> connectors = new ArrayList<DcmConnector>();

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
				loadConfig(System.getProperty("webdicom.home")
						+ "/conf/conf.xml");
			} else {
				loadConfig("conf/conf.xml");
			}
		} catch (Exception e) {
			logger.fatal("Error load config file! ", e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	

//	public Config(String confPath) {
//		try {
//			loadConfig(confPath);
//		} catch (Exception e) {
//			logger.fatal("Error load config fail! ", e);
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
//
//	public Config() {
//	}

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

	public static void loadConfig(String file)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(file));

		// normalize text representation
		doc.getDocumentElement().normalize();

		// System.out.println("Root element of the doc is "
		// + doc.getDocumentElement().getNodeName());

		aet = doc.getElementsByTagName("dicom-connection").item(0)
				.getAttributes().getNamedItem("aet").getNodeValue();

		host = doc.getElementsByTagName("dicom-connection").item(0)
				.getAttributes().getNamedItem("host").getNodeValue();

		port = doc.getElementsByTagName("dicom-connection").item(0)
				.getAttributes().getNamedItem("port").getNodeValue();

		incomingFolder = ""
				+ doc.getElementsByTagName("study-incoming-folder").item(0)
						.getFirstChild().getNodeValue();

		tmpFolder = ""
				+ doc.getElementsByTagName("tmp-folder").item(0)
						.getFirstChild().getNodeValue();

		templateFolder = ""
				+ doc.getElementsByTagName("template-folder").item(0)
						.getFirstChild().getNodeValue();

		configPdf = ""
				+ doc.getElementsByTagName("conf-pdf").item(0).getFirstChild()
						.getNodeValue();

		configJpg = ""
				+ doc.getElementsByTagName("conf-jpg").item(0).getFirstChild()
						.getNodeValue();

		dbDriver = doc.getElementsByTagName("db-connection").item(0)
				.getAttributes().getNamedItem("driver").getNodeValue();

		dbUrl = doc.getElementsByTagName("db-connection").item(0)
				.getAttributes().getNamedItem("url").getNodeValue();

		if (doc.getElementsByTagName("db-connection").item(0).getAttributes()
				.getNamedItem("jndi") != null)
			dbJndi = doc.getElementsByTagName("db-connection").item(0)
					.getAttributes().getNamedItem("jndi").getNodeValue();

		dbOmitsUrl = doc.getElementsByTagName("omits-connection").item(0)
				.getAttributes().getNamedItem("url").getNodeValue();

		dbOmitsDriver = doc.getElementsByTagName("omits-connection").item(0)
				.getAttributes().getNamedItem("driver").getNodeValue();

		if (doc.getElementsByTagName("omits-connection").item(0)
				.getAttributes().getNamedItem("jndi") != null)
			dbOmitsJndi = doc.getElementsByTagName("omits-connection").item(0)
					.getAttributes().getNamedItem("jndi").getNodeValue();

		System.out.println("dicomconnect=" + aet + "@" + host + ":" + port
				+ "; incomingFolder=" + incomingFolder + "; tmpFolder="
				+ tmpFolder + "; templateFolder=" + templateFolder
				+ "; configPdf=" + configPdf + "; configJpg=" + configJpg
				+ "; db=(" + dbDriver + ")" + dbUrl + "; omits=" + dbOmitsUrl);

		NodeList listOfConnector = doc.getElementsByTagName("dicom-driver");

		System.out.println("Total dcmconnector's : "
				+ listOfConnector.getLength());

		for (int s = 0; s < listOfConnector.getLength(); s++) {

			Node connector = listOfConnector.item(s);

			String aet = connector.getAttributes().getNamedItem("aet")
					.getNodeValue();
			String name = connector.getAttributes().getNamedItem("name")
					.getNodeValue();
			String driver = connector.getAttributes().getNamedItem("driver")
					.getNodeValue();
			System.out.println(" driver name=" + name + "; aet=" + aet
					+ "; driver=" + driver);

			// DcmConnector conn = new DcmConnector(aet, name, driver);
			// connectors.add(conn);

		}
	}

	/**
	 * @author dima_d
	 * 
	 */
	public class DcmConnector {

		private String aet;
		private String name;
		private String driver;

		public DcmConnector(String aet, String name, String driver) {
			super();
			this.aet = aet;
			this.name = name;
			this.driver = driver;
		}

		public String getAet() {
			return aet;
		}

		public String getName() {
			return name;
		}

		public String getDriver() {
			return driver;
		}

	}

	public static ArrayList<DcmConnector> getConnectors() {
		return connectors;
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

}
