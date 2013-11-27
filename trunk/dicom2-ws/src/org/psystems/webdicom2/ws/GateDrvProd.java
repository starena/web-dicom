package org.psystems.webdicom2.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

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
import org.psystems.webdicom2.ws.dto.DCM;
import org.psystems.webdicom2.ws.dto.DCMTag;
import org.psystems.webdicom2.ws.dto.Direction;
import org.psystems.webdicom2.ws.dto.RISCode;
import org.psystems.webdicom2.ws.dto.StudyResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GateDrvProd implements IGate {
	
	public static final String loggerName = "WebDicom-Ws-prod";
	public static Logger logger = Logger.getLogger(loggerName);

	private String ejbProviderURL;
	private final PatientMatching patientMatching = new PatientMatching("pid,issuer?");
	
	public GateDrvProd(Gate gate) {
		ejbProviderURL = "jnp://"+gate.archiveHost+":1099";
	}
	
	/**
	 * @param msg
	 * @return
	 */
	String getLogMessage(String msg) {
		StringBuffer sb = new StringBuffer();
		return sb.append("[").append(loggerName).append("] ").append(msg).toString();
	}
	
	/**
     * @param dataset
     * @return
     */
    private Dataset add(Dataset ds) throws Exception {
        return getMWLManager().addWorklistItem(ds, patientMatching );
    }
    
    /**
     * @return
     * @throws Exception
     */
    private MWLManager getMWLManager() throws Exception {
	    Context ctx = new InitialContext(makeEnv());
	    MWLManagerHome home = (MWLManagerHome) ctx.lookup(MWLManagerHome.JNDI_NAME);
	    ctx.close();
	    return home.create();
    }
    
    /**
     * @return
     */
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

	@Override
	public Direction sendDirection(Direction drn) throws WsException {
		Properties drnProp = new Properties();

		drnProp.put("barCode", drn.getBarCode());
		drnProp.put("misId", drn.getMisId());
		drnProp.put("dateBirsday", drn.getDateBirsday());
		drnProp.put("dateStudy", drn.getDateStudy());
		drnProp.put("modality", drn.getModality());
		drnProp.put("patientId", drn.getPatientId());
		drnProp.put("patientName", drn.getPatientName());
		drnProp.put("serviceName", drn.getServiceName());
		drnProp.put("sex", drn.getSex());
		
		//TODO выбрать поля в направлении для заполнения этих 3-х полей
		String AdmissionID = "ADMID-" + drn.getBarCode();
		String ScheduledProcedureStepID = "SPSID-" + drn.getServiceName();
		String RequestedProcedureID = "RPID-" + drn.getMisId();

		try {
			// TODO взять из classpath (положить в исходники)
			//File fXmlFile = new File("./test/dicom/mwlitem.xml");
			
			InputStream is = this.getClass().getResourceAsStream("/mwlitem.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
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
					attrNode.getFirstChild().setNodeValue(drn.getPatientId());
					System.out.println("PatientId "
							+ attrNode.getFirstChild().getNodeValue());
				}

				// PatientName
				if (((Element) (attrNode)).getAttribute("tag").equals("00100010")) {
					attrNode.getFirstChild().setNodeValue(drn.getPatientName());
					System.out.println("PatientName "
							+ attrNode.getFirstChild().getNodeValue());
				}

				// PatientBirthDate
				if (((Element) (attrNode)).getAttribute("tag").equals("00100030")) {
					attrNode.getFirstChild().setNodeValue(drn.getDateBirsday());
					System.out.println("PatientBirthDate "
							+ attrNode.getFirstChild().getNodeValue());
				}

				// PatientSex
				if (((Element) (attrNode)).getAttribute("tag").equals("00100040")) {
					attrNode.getFirstChild().setNodeValue(drn.getSex());
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
			
//			System.out.println("!! "+output);
			Dataset ds = DatasetUtils.fromXML(output);
			
			ds = add(ds);
			Dataset spsitem = ds.getItem(Tags.SPSSeq);
	        System.out.println(ds.getString(Tags.RequestedProcedureID)
	                + '/' + spsitem.getString(Tags.SPSID));
			
		} catch (Exception e) {
			throw new WsException(getLogMessage("[sendDirection]"),e);
		} 

		return drn;
	}

	@Override
	public int removeDirection(String misId) throws WsException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DCM[] getDCM(String misId) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DCM[] getDCMbyDate(String date) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DCMTag[] getDCMTags(String dcmId) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getDCMContent(String misId, String contentId)
			throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StudyResult getCompliteStudyResult(String misId) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendPdf(String misId, byte[] content) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendImage(String misId, byte[] content) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendFinalResult(String misId, String resultStr)
			throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendPhysician(String misId, String fio) throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RISCode[] getRISCodes() throws WsException {
		// TODO Auto-generated method stub
		return null;
	}

}
