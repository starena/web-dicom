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

	private ArrayList<Connector> connectors = new ArrayList<Connector>();

	String aet;
	String host;
	String port;

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
		
		outDir = ""+doc.getElementsByTagName("out").item(0).getFirstChild().getNodeValue();

		database = ""+doc.getElementsByTagName("database").item(0).getFirstChild().getNodeValue();
		
		System.out.println("dicomconnect=" + aet + "@" + host + ":" + port + " "+outDir + " database="+database);

		NodeList listOfConnector = doc.getElementsByTagName("dcmconnector");

		System.out.println("Total dcmconnector's : "
				+ listOfConnector.getLength());

		for (int s = 0; s < listOfConnector.getLength(); s++) {

			Node firstPersonNode = listOfConnector.item(s);
			if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

				Element firstconenctorElement = (Element) firstPersonNode;

				// -------

				NodeList AETList = firstconenctorElement
						.getElementsByTagName("aet");
				Element firstNameElement = (Element) AETList.item(0);

				NodeList textFNList = firstNameElement.getChildNodes();
				System.out.println("AET : "
						+ ((Node) textFNList.item(0)).getNodeValue().trim());

				String aET = ((Node) textFNList.item(0)).getNodeValue().trim();

				// -------

				NodeList nameList = firstconenctorElement
						.getElementsByTagName("name");
				Element lastNameElement = (Element) nameList.item(0);

				NodeList textLNList = lastNameElement.getChildNodes();
				System.out.println("Name : "
						+ ((Node) textLNList.item(0)).getNodeValue().trim());

				String name = ((Node) textLNList.item(0)).getNodeValue().trim();

				// -------

				NodeList ageList = firstconenctorElement
						.getElementsByTagName("driver");
				Element ageElement = (Element) ageList.item(0);

				NodeList textAgeList = ageElement.getChildNodes();
				System.out.println("driver : "
						+ ((Node) textAgeList.item(0)).getNodeValue().trim());

				String driver = ((Node) textAgeList.item(0)).getNodeValue()
						.trim();
				Connector conn = new Connector(aET, name, driver);

				connectors.add(conn);

			}
		}
	}

	/**
	 * @author dima_d
	 * 
	 */
	public class Connector {

		private String AET;
		private String name;
		private String driver;

		public Connector(String aET, String name, String driver) {
			super();
			AET = aET;
			this.name = name;
			this.driver = driver;
		}

		public String getAET() {
			return AET;
		}

		public String getName() {
			return name;
		}

		public String getDriver() {
			return driver;
		}

	}

}
