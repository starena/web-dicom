package org.psystems.webdicom2.ws.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;
import org.dcm4chex.archive.common.DatasetUtils;
import org.dcm4chex.archive.common.PatientMatching;
import org.dcm4chex.archive.ejb.interfaces.MWLManager;
import org.dcm4chex.archive.ejb.interfaces.MWLManagerHome;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestSendMwlItem {

	
	private String ejbProviderURL = "jnp://localhost:1099";
	private final PatientMatching patientMatching = new PatientMatching("pid,issuer?");
	
	/**
	 * @param args
	 * @throws Exception private Dataset remove(String rqpid, String spsid) throws Exception {
        return getMWLManager().removeWorklistItem(rqpid, spsid);
    }
	 * @throws FinderException 
	 * @throws CreateException 
	 */
	public static void main(String[] args) throws CreateException, FinderException, Exception {
		new TestSendMwlItem();

	}

	public TestSendMwlItem() throws CreateException, FinderException, Exception {
		sendMwlItem();
	}

	private void sendMwlItem() throws CreateException, FinderException, Exception {

		String id = "123";

		String PatientId = "PATID-" + id;
		String PatientName = "Ivanov-" + id + "^I^I";
		String PatientBirthDate = "19740301";
		String PatientSex = "M";

		String AdmissionID = "ADMID-" + id;
		String ScheduledProcedureStepID = "SPSID-" + id;
		String RequestedProcedureID = "RPID-" + id;

		// TODO Auto-generated method stub
		File fXmlFile = new File("./test/dicom/mwlitem.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :"
				+ doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("attr");

		for (int i = 0; i < nList.getLength(); i++) {
			Node attrNode = nList.item(i);

			// PatientId
			if (((Element) (attrNode)).getAttribute("tag").equals("00100020")) {
				attrNode.getFirstChild().setNodeValue(PatientId);
				System.out.println("PatientId "
						+ attrNode.getFirstChild().getNodeValue());
			}

			// PatientName
			if (((Element) (attrNode)).getAttribute("tag").equals("00100010")) {
				attrNode.getFirstChild().setNodeValue(PatientName);
				System.out.println("PatientName "
						+ attrNode.getFirstChild().getNodeValue());
			}

			// PatientBirthDate
			if (((Element) (attrNode)).getAttribute("tag").equals("00100030")) {
				attrNode.getFirstChild().setNodeValue(PatientBirthDate);
				System.out.println("PatientBirthDate "
						+ attrNode.getFirstChild().getNodeValue());
			}

			// PatientSex
			if (((Element) (attrNode)).getAttribute("tag").equals("00100040")) {
				attrNode.getFirstChild().setNodeValue(PatientSex);
				System.out.println("PatientSex "
						+ attrNode.getFirstChild().getNodeValue());
			}

			// AdmissionID
			if (((Element) (attrNode)).getAttribute("tag").equals("00380010")) {
				attrNode.getFirstChild().setNodeValue(AdmissionID);
				System.out.println("AdmissionID "
						+ attrNode.getFirstChild().getNodeValue());
			}

			// Scheduled Procedure Step Sequence
			if (((Element) (attrNode)).getAttribute("tag").equals("00400100")) {

				NodeList itemList = attrNode.getChildNodes();

				for (int j = 0; j < itemList.getLength(); j++) {
					Node itemNode = itemList.item(j);

					if (itemNode.getNodeType() == Node.ELEMENT_NODE) {

						NodeList tagList = ((Element) itemNode)
								.getElementsByTagName("attr");
						for (int k = 0; k < tagList.getLength(); k++) {

							Node itemNode2 = tagList.item(k);
							// ScheduledProcedureStepID
							if (((Element) (itemNode2)).getAttribute("tag")
									.equals("00400009")) {
								itemNode2.getFirstChild().setNodeValue(
										ScheduledProcedureStepID);
								System.out.println("ScheduledProcedureStepID "
										+ itemNode2.getFirstChild()
												.getNodeValue());
							}
						}
					}
				}
			}

			// RequestedProcedureID
			if (((Element) (attrNode)).getAttribute("tag").equals("00401001")) {
				attrNode.getFirstChild().setNodeValue(RequestedProcedureID);
				System.out.println("RequestedProcedureID "
						+ attrNode.getFirstChild().getNodeValue());
			}
			
			
			

		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		
//		System.out.println("!! "+output);
		Dataset ds = DatasetUtils.fromXML(output);
		
		ds = add(ds);
		Dataset spsitem = ds.getItem(Tags.SPSSeq);
        System.out.println(ds.getString(Tags.RequestedProcedureID)
                + '/' + spsitem.getString(Tags.SPSID));
        
        
	}
	
	/**
     * @param dataset
     * @return
     */
    private Dataset add(Dataset ds) throws Exception {
        return getMWLManager().addWorklistItem(ds, patientMatching );
    }

    /**
     * @param arg
     */
    private Dataset remove(String rqpid, String spsid) throws Exception {
        return getMWLManager().removeWorklistItem(rqpid, spsid);
    }
    
    private Dataset find(String rqpid, String spsid) throws Exception {
    	return null;
    }
	
	private MWLManager getMWLManager() throws Exception {
	    Context ctx = new InitialContext(makeEnv());
	    MWLManagerHome home = (MWLManagerHome) ctx.lookup(MWLManagerHome.JNDI_NAME);
	    ctx.close();
	    return home.create();
    }
	
	private Hashtable makeEnv() {
        Hashtable env = new Hashtable();
        env.put(
                "java.naming.factory.initial",
        "org.jnp.interfaces.NamingContextFactory");
        env.put(
                "java.naming.factory.url.pkgs",
        "org.jboss.naming:org.jnp.interfaces");
        env.put("java.naming.provider.url", ejbProviderURL);
        return env;
    }

}
