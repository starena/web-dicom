package org.psystems.webdicom2.ws.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.CommandUtils;
import org.dcm4che2.net.Device;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.NetworkConnection;
import org.dcm4che2.net.NewThreadExecutor;
import org.dcm4che2.net.NoPresentationContextException;
import org.dcm4che2.net.TransferCapability;

public class TestGetMwl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestGetMwl();
	}
	
	private static final int[] RETURN_KEYS = {
        Tag.AccessionNumber,
        Tag.ReferringPhysicianName,
        Tag.PatientName,
        Tag.PatientID,
        Tag.PatientBirthDate,
        Tag.PatientSex,
        Tag.PatientWeight,
        Tag.MedicalAlerts,
        Tag.Allergies,
        Tag.PregnancyStatus,
        Tag.StudyInstanceUID,
        Tag.RequestingPhysician,
        Tag.RequestingService,
        Tag.RequestedProcedureDescription,
        Tag.AdmissionID,
        Tag.SpecialNeeds,
        Tag.CurrentPatientLocation,
        Tag.PatientState,
        Tag.RequestedProcedureID,
        Tag.RequestedProcedurePriority,
        Tag.PatientTransportArrangements,
        Tag.PlacerOrderNumberImagingServiceRequest,
        Tag.FillerOrderNumberImagingServiceRequest,
        Tag.ConfidentialityConstraintOnPatientDataDescription,
    };

    private static final int[] SPS_RETURN_KEYS = {
        Tag.Modality,
        Tag.RequestedContrastAgent,
        Tag.ScheduledStationAETitle,
        Tag.ScheduledProcedureStepStartDate,
        Tag.ScheduledProcedureStepStartTime,
        Tag.ScheduledPerformingPhysicianName,
        Tag.ScheduledProcedureStepDescription,
        Tag.ScheduledProcedureStepID,
        Tag.ScheduledStationName,
        Tag.ScheduledProcedureStepLocation,
        Tag.PreMedication,
        Tag.ScheduledProcedureStepStatus
    };

    private static final String[] LE_TS = {
        UID.ExplicitVRLittleEndian, 
        UID.ImplicitVRLittleEndian };
    
    private static final byte[] EXT_NEG_INFO_FUZZY_MATCHING = { 1, 1, 1 };
	
	private Device device;
	private final NetworkApplicationEntity remoteAE = new NetworkApplicationEntity();
	private final NetworkConnection remoteConn = new NetworkConnection();
	 private final NetworkApplicationEntity ae = new NetworkApplicationEntity();
	 private final NetworkConnection conn = new NetworkConnection();
	 private final DicomObject keys = new BasicDicomObject();
	 private final DicomObject spsKeys = new BasicDicomObject();
	 private Association assoc;
	 private int priority = 0;
	 private int cancelAfter = Integer.MAX_VALUE;//Макс количество строк
	 
	 private boolean fuzzySemanticPersonNameMatching;

	public TestGetMwl() {
		String name = "DCMMWL";
		device = new Device(name);
        NewThreadExecutor executor = new NewThreadExecutor(name);
        remoteAE.setInstalled(true);
        remoteAE.setAssociationAcceptor(true);
        remoteAE.setNetworkConnection(new NetworkConnection[] { remoteConn });

        device.setNetworkApplicationEntity(ae);
        device.setNetworkConnection(conn);
        ae.setNetworkConnection(conn);
        ae.setAssociationInitiator(true);
        ae.setAETitle(name);
        for (int i = 0; i < RETURN_KEYS.length; i++) {
            keys.putNull(RETURN_KEYS[i], null);
        }        
        keys.putNestedDicomObject(Tag.RequestedProcedureCodeSequence,
                new BasicDicomObject());
        keys.putNestedDicomObject(Tag.ScheduledProcedureStepSequence, spsKeys);
        for (int i = 0; i < SPS_RETURN_KEYS.length; i++) {
            spsKeys.putNull(SPS_RETURN_KEYS[i], null);
        }
        spsKeys.putNestedDicomObject(Tag.ScheduledProtocolCodeSequence,
                new BasicDicomObject());
        
        /////////
    
        remoteAE.setAETitle("DCM4CHEE");
        remoteConn.setHostname("localhost");
        remoteConn.setPort(11112);
        
        addSpsMatchingKey(Tag.Modality, "CT");
//        addSpsMatchingKey(Tag.ScheduledProcedureStepStartDate,"20131030");
//        addSpsMatchingKey(Tag.ScheduledProcedureStepStartTime,"11111");
        
        setTransferSyntax(LE_TS);
        
        long t1 = System.currentTimeMillis();
        try {
        	assoc = ae.connect(remoteAE, executor);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to establish association:");
            e.printStackTrace(System.err);
            System.exit(2);
        }
        
        long t2 = System.currentTimeMillis();
        System.out.println("Connected to " + remoteAE + " in "
                + ((t2 - t1) / 1000F) + "s");
        
        try {
            List<DicomObject> result = query();
            long t3 = System.currentTimeMillis();
            
            System.out.println("Received " + result.size()
                    + " matching entries in " + ((t3 - t2) / 1000F) + "s");
            
            for(DicomObject dcm : result) {
//            	DicomElement pn = dcm.get(Tag.PatientName);
				System.out.println("!!! PatientName="+dcm.getString(Tag.PatientName));
            }
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
        	assoc.release(true);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Released connection to " + remoteAE);
	}
	
	public void setTransferSyntax(String[] ts) {
        TransferCapability tc = new TransferCapability(
                UID.ModalityWorklistInformationModelFIND, ts,
                TransferCapability.SCU);
        if (fuzzySemanticPersonNameMatching)
            tc.setExtInfo(EXT_NEG_INFO_FUZZY_MATCHING);
        ae.setTransferCapability(new TransferCapability[]{tc});
    }
	
	public void addSpsMatchingKey(int tag, String value) {
        spsKeys.putString(tag, null, value);
    }
	
	public List<DicomObject> query() throws IOException, InterruptedException {
        TransferCapability tc = assoc.getTransferCapabilityAsSCU(
                UID.ModalityWorklistInformationModelFIND);
        if (tc == null) {
            throw new NoPresentationContextException(
                    "Modality Worklist not supported by "
                    + remoteAE.getAETitle());
        }
        System.out.println("Send Query Request:");
        System.out.println(keys.toString());
        DimseRSP rsp = assoc.cfind(UID.ModalityWorklistInformationModelFIND,
                priority, keys, tc.getTransferSyntax()[0], cancelAfter);
        List<DicomObject> result = new ArrayList<DicomObject>();
        while (rsp.next()) {
            DicomObject cmd = rsp.getCommand();
            if (CommandUtils.isPending(cmd)) {
                DicomObject data = rsp.getDataset();
                result.add(data);
                System.out.println("\nReceived Query Response #"
                        + result.size() + ":");
                System.out.println(data.toString());
            }
        }
        return result;
    }
	
	

}
