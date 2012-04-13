package org.dicomsrv.services;

import java.io.File;
import java.io.IOException;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.Status;
import org.dicomsrv.database.DatabaseFind;

public class DicomMultiFindRSP implements DimseRSP {
	
    private DicomObject rsp;
    private DicomObject keys;
    private DicomObject mwl;
    private final DatabaseFind databaseFind = new DatabaseFind();
    private boolean responsesSended = false;
    
    public DicomMultiFindRSP(DicomObject keys, DicomObject rsp) {
        this.keys = keys;
        // always return Specific Character Set
        if (!keys.contains(Tag.SpecificCharacterSet))
            keys.putNull(Tag.SpecificCharacterSet, VR.CS);
        this.rsp = rsp;

        databaseFind.generateQuery(keys);
        databaseFind.executeFindQuery();
    }
    
    DicomObject load(File f) throws Exception {
        return loadDICOM(f);
    }

    private DicomObject loadDICOM(File f) throws Exception {
        DicomInputStream in = new DicomInputStream(f);
        try {
            return in.readDicomObject();
        } finally {
            in.close();
        }
    }
    
    public synchronized boolean next() throws IOException, InterruptedException {
       	if (responsesSended)
    		return false;
    	
       	if (!databaseFind.hasMoreResults()) {
       		if (databaseFind.getResultNumber() == 0) 
       			rsp.putInt(Tag.Status, VR.US, Status.Cancel);
       		else 
       			rsp.putInt(Tag.Status, VR.US, Status.Success);
       		databaseFind.closeConnection();
       	} else {
       		try {
				mwl = load(new File(databaseFind.getResultPathName()));
				if (!mwl.contains(Tag.SpecificCharacterSet))
					mwl.putNull(Tag.SpecificCharacterSet, VR.CS);
				rsp.putInt(Tag.Status, VR.US,
						mwl.containsAll(keys) ? Status.Pending
								: Status.PendingWarning);
				return true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				rsp.putInt(Tag.Status, VR.US, Status.ProcessingFailure);
				rsp.putString(Tag.ErrorComment, VR.LO, e.getMessage());
			}
       	}
		mwl = null;
		responsesSended = true;
		return true;
    }

    public DicomObject getCommand() {
        return rsp;
    }

    public DicomObject getDataset() {
        return mwl != null ? mwl.subSet(keys) : null;
    }

    public synchronized void cancel(Association a) throws IOException {
        //files = null;
    }

}
