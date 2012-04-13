package dicomcl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.Executor;

import org.dcm4che2.data.UID;
import org.dcm4che2.net.Device;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.NetworkConnection;
import org.dcm4che2.net.NewThreadExecutor;
import org.dcm4che2.net.TransferCapability;
import org.dcm4che2.net.service.VerificationService;

public class DicomServicesCore {
	
    private static final String[] NON_RETIRED_LE_TS = { UID.JPEGLSLossless,
        UID.JPEGLossless, UID.JPEGLosslessNonHierarchical14,
        UID.JPEG2000LosslessOnly, UID.DeflatedExplicitVRLittleEndian,
        UID.RLELossless, UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian, UID.JPEGBaseline1, UID.JPEGExtended24,
        UID.JPEGLSLossyNearLossless, UID.JPEG2000, UID.MPEG2, };

    private static final String[] CUIDS = {
        UID.BasicStudyContentNotificationSOPClassRetired,
        UID.StoredPrintStorageSOPClassRetired,
        UID.HardcopyGrayscaleImageStorageSOPClassRetired,
        UID.HardcopyColorImageStorageSOPClassRetired,
        UID.ComputedRadiographyImageStorage,
        UID.DigitalXRayImageStorageForPresentation,
        UID.DigitalXRayImageStorageForProcessing,
        UID.DigitalMammographyXRayImageStorageForPresentation,
        UID.DigitalMammographyXRayImageStorageForProcessing,
        UID.DigitalIntraoralXRayImageStorageForPresentation,
        UID.DigitalIntraoralXRayImageStorageForProcessing,
        UID.StandaloneModalityLUTStorageRetired,
        UID.EncapsulatedPDFStorage, UID.StandaloneVOILUTStorageRetired,
        UID.GrayscaleSoftcopyPresentationStateStorageSOPClass,
        UID.ColorSoftcopyPresentationStateStorageSOPClass,
        UID.PseudoColorSoftcopyPresentationStateStorageSOPClass,
        UID.BlendingSoftcopyPresentationStateStorageSOPClass,
        UID.XRayAngiographicImageStorage, UID.EnhancedXAImageStorage,
        UID.XRayRadiofluoroscopicImageStorage, UID.EnhancedXRFImageStorage,
        UID.XRayAngiographicBiPlaneImageStorageRetired,
        UID.PositronEmissionTomographyImageStorage,
        UID.StandalonePETCurveStorageRetired, UID.CTImageStorage,
        UID.EnhancedCTImageStorage, UID.NuclearMedicineImageStorage,
        UID.UltrasoundMultiframeImageStorageRetired,
        UID.UltrasoundMultiframeImageStorage, UID.MRImageStorage,
        UID.EnhancedMRImageStorage, UID.MRSpectroscopyStorage,
        UID.RTImageStorage, UID.RTDoseStorage, UID.RTStructureSetStorage,
        UID.RTBeamsTreatmentRecordStorage, UID.RTPlanStorage,
        UID.RTBrachyTreatmentRecordStorage,
        UID.RTTreatmentSummaryRecordStorage,
        UID.NuclearMedicineImageStorageRetired,
        UID.UltrasoundImageStorageRetired, UID.UltrasoundImageStorage,
        UID.RawDataStorage, UID.SpatialRegistrationStorage,
        UID.SpatialFiducialsStorage, UID.RealWorldValueMappingStorage,
        UID.SecondaryCaptureImageStorage,
        UID.MultiframeSingleBitSecondaryCaptureImageStorage,
        UID.MultiframeGrayscaleByteSecondaryCaptureImageStorage,
        UID.MultiframeGrayscaleWordSecondaryCaptureImageStorage,
        UID.MultiframeTrueColorSecondaryCaptureImageStorage,
        UID.VLImageStorageTrialRetired, UID.VLEndoscopicImageStorage,
        UID.VideoEndoscopicImageStorage, UID.VLMicroscopicImageStorage,
        UID.VideoMicroscopicImageStorage,
        UID.VLSlideCoordinatesMicroscopicImageStorage,
        UID.VLPhotographicImageStorage, UID.VideoPhotographicImageStorage,
        UID.OphthalmicPhotography8BitImageStorage,
        UID.OphthalmicPhotography16BitImageStorage,
        UID.StereometricRelationshipStorage,
        UID.VLMultiframeImageStorageTrialRetired,
        UID.StandaloneOverlayStorageRetired, UID.BasicTextSRStorage,
        UID.EnhancedSRStorage, UID.ComprehensiveSRStorage,
        UID.ProcedureLogStorage, UID.MammographyCADSRStorage,
        UID.KeyObjectSelectionDocumentStorage,
        UID.ChestCADSRStorage, UID.XRayRadiationDoseSRStorage,
        UID.StandaloneCurveStorageRetired,
        UID._12leadECGWaveformStorage, UID.GeneralECGWaveformStorage,
        UID.AmbulatoryECGWaveformStorage, UID.HemodynamicWaveformStorage,
        UID.CardiacElectrophysiologyWaveformStorage,
        UID.BasicVoiceAudioWaveformStorage, UID.HangingProtocolStorage,
        UID.SiemensCSANonImageStorage, 
    };

    private static String[] tsuids = NON_RETIRED_LE_TS;
	
	private static char[] SECRET = { 's', 'e', 'c', 'r', 'e', 't' };

    private static final String[] ONLY_DEF_TS = { UID.ImplicitVRLittleEndian };

    private static Executor executor = new NewThreadExecutor("DCMRCV");

    private static Device device = new Device("DCMRCV");

	private static NetworkApplicationEntity ae = new NetworkApplicationEntity();

    private static NetworkConnection nc = new NetworkConnection();

    private static int rspdelay = 0;

    private static String keyStoreURL = "resource:tls/test_sys_2.p12";

    private static char[] keyStorePassword = SECRET;

    private static char[] keyPassword;

    private static String trustStoreURL = "resource:tls/mesa_certs.jks";

    private static char[] trustStorePassword = SECRET;
    
    public static final void setAEtitle(String aet) {
    	ae.setAETitle(aet);
    }

    public static final void setHostname(String hostname) {
    	nc.setHostname(hostname);
    }

    public static final void setPort(int port) {
    	nc.setPort(port);
    }

    public static final void setTlsWithoutEncyrption() {
    	nc.setTlsWithoutEncyrption();
    }

    public static final void setTls3DES_EDE_CBC() {
    	nc.setTls3DES_EDE_CBC();
    }

    public static final void setTlsAES_128_CBC() {
    	nc.setTlsAES_128_CBC();
    }

    public static final void disableSSLv2Hello() {
    	nc.disableSSLv2Hello();
    }

    public static final void setTlsNeedClientAuth(boolean needClientAuth) {
    	nc.setTlsNeedClientAuth(needClientAuth);
    }

    public static final void setPackPDV(boolean packPDV) {
    	ae.setPackPDV(packPDV);
    }

    public static final void setAssociationReaperPeriod(int period) {
    	device.setAssociationReaperPeriod(period);
    }

    public static final void setTcpNoDelay(boolean tcpNoDelay) {
    	nc.setTcpNoDelay(tcpNoDelay);
    }

    public static final void setRequestTimeout(int timeout) {
    	nc.setRequestTimeout(timeout);
    }

    public static final void setReleaseTimeout(int timeout) {
    	nc.setReleaseTimeout(timeout);
    }

    public static final void setSocketCloseDelay(int delay) {
    	nc.setSocketCloseDelay(delay);
    }

    public static final void setIdleTimeout(int timeout) {
    	ae.setIdleTimeout(timeout);
    }

    public static final void setDimseRspTimeout(int timeout) {
    	ae.setDimseRspTimeout(timeout);
    }

    public static final void setMaxPDULengthSend(int maxLength) {
    	ae.setMaxPDULengthSend(maxLength);
    }

    public static void setMaxPDULengthReceive(int maxLength) {
    	ae.setMaxPDULengthReceive(maxLength);
    }

    public static final void setReceiveBufferSize(int bufferSize) {
    	nc.setReceiveBufferSize(bufferSize);
    }

    public static final void setSendBufferSize(int bufferSize) {
        nc.setSendBufferSize(bufferSize);
    }

    public static final void setKeyStoreURL(String url) {
        keyStoreURL = url;
    }

    public static final void setKeyStorePassword(String pw) {
        keyStorePassword = pw.toCharArray();
    }

    public static final void setKeyPassword(String pw) {
        keyPassword = pw.toCharArray();
    }

    public static final void setTrustStorePassword(String pw) {
        trustStorePassword = pw.toCharArray();
    }

    public static final void setTrustStoreURL(String url) {
        trustStoreURL = url;
    }

    public static void setTransferSyntax(String[] tsuids) {
        DicomServicesCore.tsuids = tsuids;
    }

    public static void initTransferCapability() {
        TransferCapability[] tc = new TransferCapability[CUIDS.length + 1];
        tc[0] = new TransferCapability(UID.VerificationSOPClass, ONLY_DEF_TS,
                TransferCapability.SCP);
        for (int i = 0; i < CUIDS.length; i++)
            tc[i + 1] = new TransferCapability(CUIDS[i], tsuids,
                    TransferCapability.SCP);
        ae.setTransferCapability(tc);
    }

    public static void setMaxOpsPerformed(int maxOps) {
        ae.setMaxOpsPerformed(maxOps);
    }

    public static void initTLS() throws GeneralSecurityException, IOException {
        KeyStore keyStore = loadKeyStore(keyStoreURL, keyStorePassword);
        KeyStore trustStore = loadKeyStore(trustStoreURL, trustStorePassword);
        device.initTLS(keyStore,
                keyPassword != null ? keyPassword : keyStorePassword,
                trustStore);
    }

    private static KeyStore loadKeyStore(String url, char[] password)
            throws GeneralSecurityException, IOException {
        KeyStore key = KeyStore.getInstance(toKeyStoreType(url));
        InputStream in = openFileOrURL(url);
        try {
            key.load(in, password);
        } finally {
            in.close();
        }
        return key;
    }

    private static InputStream openFileOrURL(String url) throws IOException {
        if (url.startsWith("resource:")) {
            return DicomStorageService.class.getClassLoader().getResourceAsStream(
                    url.substring(9));
        }
        try {
            return new URL(url).openStream();
        } catch (MalformedURLException e) {
            return new FileInputStream(url);
        }
    }

    private static String toKeyStoreType(String fname) {
        return fname.endsWith(".p12") || fname.endsWith(".P12")
                 ? "PKCS12" : "JKS";
    }

    public static void setDimseRspDelay(int delay) {
        rspdelay = delay;
    }
    
    public static NetworkApplicationEntity getNetworkApplicationEntity() {
    	return ae;
    }
    
    public static Executor getExecutor() {
    	return executor;
    }

    public static String[] getCUIDS() {
		return CUIDS;
	}

	public static Device getDevice() {
		return device;
	}

	public static NetworkConnection getNetworkConnection() {
		return nc;
	}

	public static int getRspdelay() {
		return rspdelay;
	}

	public static void prepare() {
        device.setNetworkApplicationEntity(ae);
        device.setNetworkConnection(nc);
        ae.setNetworkConnection(nc);
        ae.setAssociationAcceptor(true);
        ae.register(new VerificationService());
	}
	
    public static void start() throws IOException {
    	device.startListening(executor);
        System.out.println("Start Server listening on port " + nc.getPort());
    }

    public static void stop() {
        if (device != null)
        	device.stopListening();

        if (nc != null)
            System.out.println("Stop Server listening on port " + nc.getPort());
        else
            System.out.println("Stop Server");
    }


}
