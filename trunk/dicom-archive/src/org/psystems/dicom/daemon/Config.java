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
	private String outDir;
	private String database;

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

		aet = doc.getElementsByTagName("dicomconnect").item(0).getAttributes()
				.getNamedItem("aet").getNodeValue();

		host = doc.getElementsByTagName("dicomconnect").item(0).getAttributes()
				.getNamedItem("host").getNodeValue();

		port = doc.getElementsByTagName("dicomconnect").item(0).getAttributes()
				.getNamedItem("port").getNodeValue();

		outDir = ""
				+ doc.getElementsByTagName("out").item(0).getFirstChild()
						.getNodeValue();

		database = ""
				+ doc.getElementsByTagName("database").item(0).getFirstChild()
						.getNodeValue();

		System.out.println("dicomconnect=" + aet + "@" + host + ":" + port
				+ " " + outDir + " database=" + database);

		NodeList listOfConnector = doc.getElementsByTagName("dcmconnector");

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

	public String getOutDir() {
		return outDir;
	}

	public String getDatabase() {
		return database;
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

}
