package org.psystems.webdicom2.ws.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.datatype.XPN;
import ca.uhn.hl7v2.model.v231.group.ORM_O01_PIDPD1NTEPV1PV2IN1IN2IN3GT1AL1;
import ca.uhn.hl7v2.model.v231.message.ACK;
import ca.uhn.hl7v2.model.v231.message.ORM_O01;
import ca.uhn.hl7v2.model.v231.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

public class TestHl7SendMWL {

	public static void main(String[] args) throws HL7Exception, LLPException, IOException {

		String patientId = "ID124";
		String patientName = "Ivanov3^Ivan^Ivanovich";
		String patientBirthDate = "19740301";
		String patientSex = "M";
		String accessionNumber = "AN124";
		
		String Modality = "MG";
		String RequestedProcedureID = "RP124";
		String ScheduledProcedureStepID = "ST124";
		String ScheduledProcedureStepDescription = "SP124";
		String RequestedProcedureDescription = "Mammografia 123";
		String ReferringPhysicianName = "NELL^F REDERICK^P^^DR";
		String RequestingPhysician = "ES TRADA^JAIME^P^DR";
		
		
		String msg = "" +
		"MSH|^~\\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_M ANAGER|||ORM^O01|100112|P|2.3.1|||||| ||\n"+
		"PID|||"+patientId+"^^^ADT1||" + patientName+ "||"+patientBirthDate+"|"+patientSex+"||WH|82 0 JORIE BLVD^^CHICAGO^IL^60523|||||||20-98-4000|||||||||||||||||||||\n"+
		"PV1||E|ED||||1234^WEAVER^TIMOTHY^P^^DR|5101^"+ReferringPhysicianName+"|||||||||||V100^^^ADT1|||||||||||||| |||||||||||200008201100|||||||V|\n"+
		"ORC|NW|A100Z^MESA_ORDPLC|B100Z^MESA_ORDFIL||SC||1^ once^^^^S||200008161510|^ROSEWOOD^RANDOLPH||7101^E STRADA^JAIME^P^^DR||(314)555-1212|200008161510||922229-10^IHE-RAD^IHE-CODE-231||\n"+
		"OBR|1|A100Z^MESA_ORDPLC|B100Z^MESA_ORDFIL|P1^Proce dure 1^ERL_MESA^X1_A1^"+ScheduledProcedureStepDescription+"^DSS_MESA|||||||||xxx||Radiology^^^^R|7101^"+RequestingPhysician+"||"+accessionNumber+"|"+RequestedProcedureID +"|"+ScheduledProcedureStepID+"||||"+Modality+"|||1^once^^^^S|||WALK|||||||||||A|||RP_X1^"+RequestedProcedureDescription+"^DSS_MESA\n"+
		"ZDS|1.2.1^100^Application^DICOM\n";
		
	
		FileReader reader = new FileReader("/home/ddv/workspace/dicom2-ws/test/hl7/new_mwl_item.hl7");

		// Create an iterator to iterate over all the messages
//		Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(
//				reader);

		// Create a HapiContext
		HapiContext context = new DefaultHapiContext();

		Parser p = context.getGenericParser();
		
		Connection conn = null;
//		while (iter.hasNext()) {

			/*
			 * If we don't already have a connection, create one. Note that
			 * unless something goes wrong, it's very common to keep reusing the
			 * same connection until we are done sending messages. Many systems
			 * keep a connection open even if a long period will pass between
			 * messages being sent. This is good practice, as it is much faster
			 * than creating a new connection each time.
			 */
			if (conn == null) {
				boolean useTls = false;
				int port = 2575;
				conn = context.newClient("localhost", port, useTls);
			}

			try {
				
				//Message next = iter.next();
				
				Message hapiMsg = p.parse(msg);				 
				ORM_O01 orm = (ORM_O01)hapiMsg;
				
//				for(String name : orm.getNames()) {
//					System.out.println("!!!! "+name);
//				}
//				
//				ORM_O01_PIDPD1NTEPV1PV2IN1IN2IN3GT1AL1 pid = orm.getPIDPD1NTEPV1PV2IN1IN2IN3GT1AL1();
//				System.out.println("!!!!!!! "+pid.getPID().getPatientName().toString());
//				for(String name : pid.getPID().getNames()) {
//					System.out.println(">>>>>>>>>>> "+name+"=");
//				}
//				
				
				//XPN[] pname = pid.getPatientName();
				
				
				
				
				
//				jfor (XPN xpn : pname) {
//					System.out.println("!!!! xpn ["+xpn.getFamilyName()+"] - ["+xpn.getGivenName()+"]"+"["+
//				xpn.getMiddleInitialOrName()+"]");
//				}
//				
//				System.out.println(" !!!! next: " + next.getClass().getName());
//				System.out.println(" !!!! orm: " + pname);
				
				Message response = conn.getInitiator().sendAndReceive(hapiMsg);
				System.out.println("[>>>] Responce: "
						+ response.encode());
				
				ACK ack =   (ACK) response.getMessage();
				System.out.println(" TEXT MESSAGE >>> " + ack.getMSA().getTextMessage());
				
				//Если есть ошибка
				//TODO Разобраться с кодами ошибок
				if(ack.getMSA().getTextMessage()!=null) {
					//throw new IOException(ack.getMSA().getTextMessage().encode());
				}
				
//				System.out.println(" TEXT MESSAGE ERR >>> " + ack.getERR().getMessage());
//				for(String name : ack.getMSA().getNames()) {
//					System.out.println(">>> " + name+" = ");
//				}
			} finally {

				// Since we failed, close the connection
				conn.close();
				conn = null;
				
			}
			
			System.exit(0);

//		}
		
		

	}

}