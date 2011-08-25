package org.psystems.dicom.daemon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

	private ArrayList<DcmConnector> connectors = new ArrayList<DcmConnector>();

	private String aet;
	private String host;
	private String port;
	private String incomingFolder;
	private String db;

	private String tmpFolder;

	private String templateFolder;

	private String configPdf;

	private String configJpg;

	private String omits;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			new Config("distrib/dcmarcive-conf.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param file
	 * @throws Exception
	 */
	public Config(String file) throws Exception {
		loadConfig(file);
	}

	public void loadConfig(String file) throws ParserConfigurationException,
			SAXException, IOException {

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

		db = doc.getElementsByTagName("db-connection").item(0).getFirstChild()
				.getNodeValue();

		omits = doc.getElementsByTagName("omits-connection").item(0)
				.getFirstChild().getNodeValue();

		System.out.println("dicomconnect=" + aet + "@" + host + ":" + port
				+ "; incomingFolder=" + incomingFolder + "; tmpFolder="
				+ tmpFolder + "; templateFolder=" + templateFolder
				+ "; configPdf=" + configPdf + "; configJpg=" + configJpg
				+ "; db=" + db + "; omits=" + omits);

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

			DcmConnector conn = new DcmConnector(aet, name, driver);
			connectors.add(conn);

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

	public ArrayList<DcmConnector> getConnectors() {
		return connectors;
	}

	public String getAet() {
		return aet;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getIncomingFolder() {
		return incomingFolder;
	}

	public String getDb() {
		return db;
	}

	public String getTmpFolder() {
		return tmpFolder;
	}

	public String getTemplateFolder() {
		return templateFolder;
	}

	public String getConfigPdf() {
		return configPdf;
	}

	public String getConfigJpg() {
		return configJpg;
	}

	public String getOmits() {
		return omits;
	}

}
