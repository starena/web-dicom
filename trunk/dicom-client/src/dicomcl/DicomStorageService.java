package dicomcl;

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Gunter Zeilinger, Huetteldorferstr. 24/10, 1150 Vienna/Austria/Europe.
 * Portions created by the Initial Developer are Copyright (C) 2002-2005
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * Gunter Zeilinger <gunterze@gmail.com>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.CommandUtils;
import org.dcm4che2.net.DicomServiceException;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.PDVInputStream;
import org.dcm4che2.net.Status;
import org.dcm4che2.net.service.StorageService;
import org.dcm4che2.util.CloseUtils;


/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 10729 $ $Date: 2009-04-06 23:33:21 +0200 (Mon, 06 Apr 2009) $
 * @since Oct 13, 2005
 */
public class DicomStorageService extends StorageService {
	
	private static String[] CUIDS = DicomServicesCore.getCUIDS(); 
		
    private Executor executor = DicomServicesCore.getExecutor();

    private NetworkApplicationEntity ae = DicomServicesCore.getNetworkApplicationEntity();

    private int fileBufferSize = 256;

    private File destination;

    private boolean devnull;

	private Object eventListener;

    private int rspdelay = DicomServicesCore.getRspdelay();
    
	private final List<DicomObject> retrievedDicomObjects = new ArrayList<DicomObject>();  
	
    public DicomStorageService() {
        super(CUIDS);
        ae.register(this);
    }

    public void setFileBufferSize(int size) {
        fileBufferSize = size;
    }

    public void setDestination(String filePath) {
        this.destination = new File(filePath);
        this.devnull = "/dev/null".equals(filePath);
        if (!devnull)
            destination.mkdir();
    }

    public void setEventListener(Object eventListener) {
		this.eventListener = eventListener;
	}
    
    public DicomObject getFirstRetrievedDicomObject() {
    	try {
    		DicomObject dicomObject = retrievedDicomObjects.get(0);
    		retrievedDicomObjects.remove(0);
    		return dicomObject;
    	} catch (IndexOutOfBoundsException e) {
    		return null;
    	}
    }

    /** Overwrite {@link StorageService#cstore} to send delayed C-STORE RSP 
     * by separate Thread, so reading of following received C-STORE RQs from
     * the open association is not blocked.
     */
    @Override
    public void cstore(final Association as, final int pcid, DicomObject rq,
            PDVInputStream dataStream, String tsuid)
            throws DicomServiceException, IOException {
        final DicomObject rsp = CommandUtils.mkRSP(rq, CommandUtils.SUCCESS);
        onCStoreRQ(as, pcid, rq, dataStream, tsuid, rsp);
        if (rspdelay > 0) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(rspdelay);
                        as.writeDimseRSP(pcid, rsp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            as.writeDimseRSP(pcid, rsp);
        }
        onCStoreRSP(as, pcid, rq, dataStream, tsuid, rsp);
    }

    @Override
    protected void onCStoreRQ(Association as, int pcid, DicomObject rq,
            PDVInputStream dataStream, String tsuid, DicomObject rsp)
            throws IOException, DicomServiceException {
        if (destination == null) {
            super.onCStoreRQ(as, pcid, rq, dataStream, tsuid, rsp);
        }
        else {
            BufferedOutputStream bos = null;
            DicomOutputStream dos = null;
            String iuid = null;
            File file = null;
            try {
                String cuid = rq.getString(Tag.AffectedSOPClassUID);
                iuid = rq.getString(Tag.AffectedSOPInstanceUID);
                BasicDicomObject fmi = new BasicDicomObject();
                fmi.initFileMetaInformation(cuid, iuid, tsuid);
                file = devnull ? destination : new File(destination, iuid + ".part");
                bos = new BufferedOutputStream(new FileOutputStream(file), fileBufferSize);
                dos = new DicomOutputStream(bos);
                dos.writeFileMetaInformation(fmi);
                DicomObject dicomObject = dataStream.readDataset();
                dos.writeDataset(dicomObject, tsuid);
                retrievedDicomObjects.add(dicomObject);
            }
            catch (IOException e) {
                throw new DicomServiceException(rq, Status.ProcessingFailure, e
                        .getMessage());
            }
            finally {
                CloseUtils.safeClose(dos);
            }
            
            // Rename the file after it has been written. See DCM-279
            if (!devnull && file != null && file.exists()) {
                File rename = new File(destination, iuid);
                file.renameTo(rename);
            }
        }
    }

}
