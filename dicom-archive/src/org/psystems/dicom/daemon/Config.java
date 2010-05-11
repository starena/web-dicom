package org.psystems.dicom.daemon;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Config(); 

	}
	
	

	public Config() {
		super();
		try {
			loadConfig();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	public void loadConfig() throws ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File("distrib/dcmarcive-conf.xml"));

		// normalize text representation
		doc.getDocumentElement().normalize();

		System.out.println("Root element of the doc is "
				+ doc.getDocumentElement().getNodeName());

		NodeList listOfConnector = doc.getElementsByTagName("dcmconnector");

		System.out.println("Total dcmconnector's : "
				+ listOfConnector.getLength());

		for (int s = 0; s < listOfConnector.getLength(); s++) {

			Node firstPersonNode = listOfConnector.item(s);
			if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

				Element firstconenctorElement = (Element) firstPersonNode;

				// -------

				NodeList AETList = firstconenctorElement
						.getElementsByTagName("AET");
				Element firstNameElement = (Element) AETList.item(0);

				NodeList textFNList = firstNameElement.getChildNodes();
				System.out.println("AET : "
						+ ((Node) textFNList.item(0)).getNodeValue().trim());

				// -------

				NodeList nameList = firstconenctorElement
						.getElementsByTagName("name");
				Element lastNameElement = (Element) nameList.item(0);

				NodeList textLNList = lastNameElement.getChildNodes();
				System.out.println("Name : "
						+ ((Node) textLNList.item(0)).getNodeValue().trim());

				// -------
				
				NodeList ageList = firstconenctorElement
						.getElementsByTagName("driver");
				Element ageElement = (Element) ageList.item(0);

				NodeList textAgeList = ageElement.getChildNodes();
				System.out.println("driver : "
						+ ((Node) textAgeList.item(0)).getNodeValue().trim());

			}
		}
	}

}
