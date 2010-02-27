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

package org.psystems.dicom.daemon;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.filecache.FileCache;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.CommandUtils;
import org.dcm4che2.net.Device;
import org.dcm4che2.net.DicomServiceException;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.NetworkConnection;
import org.dcm4che2.net.NewThreadExecutor;
import org.dcm4che2.net.PDVInputStream;
import org.dcm4che2.net.Status;
import org.dcm4che2.net.TransferCapability;
import org.dcm4che2.net.service.StorageService;
import org.dcm4che2.net.service.VerificationService;
import org.dcm4che2.util.CloseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 12244 $ $Date: 2009-10-05 11:47:05 +0200 (Mon, 05 Oct
 *          2009) $
 * @since Oct 13, 2005
 */
public class DcmRcv extends StorageService {

	private static Logger LOG = LoggerFactory.getLogger(DcmRcv.class);

	private static final int KB = 1024;

	private static final String USAGE = "dcmrcv [Options] [<aet>[@<ip>]:]<port>";

	private static final String DESCRIPTION = "DICOM Server listening on specified <port> for incoming association "
			+ "requests. If no local IP address of the network interface is specified "
			+ "connections on any/all local addresses are accepted. If <aet> is "
			+ "specified, only requests with matching called AE title will be "
			+ "accepted.\n" + "Options:";

	private static final String EXAMPLE = "\nExample: dcmrcv DCMRCV:11112 -dest /tmp \n"
			+ "=> Starts server listening on port 11112, accepting association "
			+ "requests with DCMRCV as called AE title. Received objects "
			+ "are stored to /tmp.";

	private static String[] TLS1 = { "TLSv1" };

	private static String[] SSL3 = { "SSLv3" };

	private static String[] NO_TLS1 = { "SSLv3", "SSLv2Hello" };

	private static String[] NO_SSL2 = { "TLSv1", "SSLv3" };

	private static String[] NO_SSL3 = { "TLSv1", "SSLv2Hello" };

	private static char[] SECRET = { 's', 'e', 'c', 'r', 'e', 't' };

	private static final String[] ONLY_DEF_TS = { UID.ImplicitVRLittleEndian };

	private static final String[] NATIVE_TS = { UID.ExplicitVRLittleEndian,
			UID.ExplicitVRBigEndian, UID.ImplicitVRLittleEndian };

	private static final String[] NATIVE_LE_TS = { UID.ExplicitVRLittleEndian,
			UID.ImplicitVRLittleEndian };

	private static final String[] NON_RETIRED_TS = { UID.JPEGLSLossless,
			UID.JPEGLossless, UID.JPEGLosslessNonHierarchical14,
			UID.JPEG2000LosslessOnly, UID.DeflatedExplicitVRLittleEndian,
			UID.RLELossless, UID.ExplicitVRLittleEndian,
			UID.ExplicitVRBigEndian, UID.ImplicitVRLittleEndian,
			UID.JPEGBaseline1, UID.JPEGExtended24, UID.JPEGLSLossyNearLossless,
			UID.JPEG2000, UID.MPEG2, };

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
			UID.KeyObjectSelectionDocumentStorage, UID.ChestCADSRStorage,
			UID.XRayRadiationDoseSRStorage, UID.EncapsulatedPDFStorage,
			UID.EncapsulatedCDAStorage, UID.StandaloneCurveStorageRetired,
			UID._12leadECGWaveformStorage, UID.GeneralECGWaveformStorage,
			UID.AmbulatoryECGWaveformStorage, UID.HemodynamicWaveformStorage,
			UID.CardiacElectrophysiologyWaveformStorage,
			UID.BasicVoiceAudioWaveformStorage, UID.HangingProtocolStorage,
			UID.SiemensCSANonImageStorage,
			UID.Dcm4cheAttributesModificationNotificationSOPClass };

	private final Executor executor;

	private final Device device;

	private final NetworkApplicationEntity ae = new NetworkApplicationEntity();

	private final NetworkConnection nc = new NetworkConnection();

	private String[] tsuids = NON_RETIRED_LE_TS;

	private FileCache cache = new FileCache();

	private File devnull;

	private Properties calling2dir;

	private Properties called2dir;

	private String callingdefdir = "OTHER";

	private String calleddefdir = "OTHER";

	private int fileBufferSize = 1024;

	private int rspdelay = 0;

	private String keyStoreURL = "resource:tls/test_sys_2.p12";

	private char[] keyStorePassword = SECRET;

	private char[] keyPassword;

	private String trustStoreURL = "resource:tls/mesa_certs.jks";

	private char[] trustStorePassword = SECRET;

	private static String connectionStr = "jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM";

	private Connection connection;

	private int frame = 1;
	private float center;
	private float width;
	private String vlutFct;
	private boolean autoWindowing;
	private DicomObject prState;
	private short[] pval2gray;
	private String imageFileExt = ".jpg";
	private String dcmFileExt = ".dcm";

	private String imageDirPrefix = ".images";

	private String imageContentType = "image/jpeg";

	public DcmRcv(String name) {
		super(CUIDS);
		device = new Device(name);
		executor = new NewThreadExecutor(name);
		device.setNetworkApplicationEntity(ae);
		device.setNetworkConnection(nc);
		ae.setNetworkConnection(nc);
		ae.setAssociationAcceptor(true);
		ae.register(new VerificationService());
		ae.register(this);
	}

	public final void setAEtitle(String aet) {
		ae.setAETitle(aet);
	}

	public final void setHostname(String hostname) {
		nc.setHostname(hostname);
	}

	public final void setPort(int port) {
		nc.setPort(port);
	}

	public final void setTlsProtocol(String[] tlsProtocol) {
		nc.setTlsProtocol(tlsProtocol);
	}

	public final void setTlsWithoutEncyrption() {
		nc.setTlsWithoutEncyrption();
	}

	public final void setTls3DES_EDE_CBC() {
		nc.setTls3DES_EDE_CBC();
	}

	public final void setTlsAES_128_CBC() {
		nc.setTlsAES_128_CBC();
	}

	public final void setTlsNeedClientAuth(boolean needClientAuth) {
		nc.setTlsNeedClientAuth(needClientAuth);
	}

	public final void setKeyStoreURL(String url) {
		keyStoreURL = url;
	}

	public final void setKeyStorePassword(String pw) {
		keyStorePassword = pw.toCharArray();
	}

	public final void setKeyPassword(String pw) {
		keyPassword = pw.toCharArray();
	}

	public final void setTrustStorePassword(String pw) {
		trustStorePassword = pw.toCharArray();
	}

	public final void setTrustStoreURL(String url) {
		trustStoreURL = url;
	}

	public final void setPackPDV(boolean packPDV) {
		ae.setPackPDV(packPDV);
	}

	public final void setAssociationReaperPeriod(int period) {
		device.setAssociationReaperPeriod(period);
	}

	public final void setTcpNoDelay(boolean tcpNoDelay) {
		nc.setTcpNoDelay(tcpNoDelay);
	}

	public final void setRequestTimeout(int timeout) {
		nc.setRequestTimeout(timeout);
	}

	public final void setReleaseTimeout(int timeout) {
		nc.setReleaseTimeout(timeout);
	}

	public final void setSocketCloseDelay(int delay) {
		nc.setSocketCloseDelay(delay);
	}

	public final void setIdleTimeout(int timeout) {
		ae.setIdleTimeout(timeout);
	}

	public final void setDimseRspTimeout(int timeout) {
		ae.setDimseRspTimeout(timeout);
	}

	public final void setMaxPDULengthSend(int maxLength) {
		ae.setMaxPDULengthSend(maxLength);
	}

	public void setMaxPDULengthReceive(int maxLength) {
		ae.setMaxPDULengthReceive(maxLength);
	}

	public final void setReceiveBufferSize(int bufferSize) {
		nc.setReceiveBufferSize(bufferSize);
	}

	public final void setSendBufferSize(int bufferSize) {
		nc.setSendBufferSize(bufferSize);
	}

	public void setDimseRspDelay(int delay) {
		rspdelay = delay;
	}

	private static CommandLine parse(String[] args) {
		Options opts = new Options();

		OptionBuilder.withArgName("name");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("set device name, use DCMRCV by default");
		opts.addOption(OptionBuilder.create("device"));

		OptionBuilder.withArgName("NULL|3DES|AES");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("enable TLS connection without, 3DES or AES encryption");
		opts.addOption(OptionBuilder.create("tls"));

		OptionGroup tlsProtocol = new OptionGroup();
		tlsProtocol.addOption(new Option("tls1",
				"disable the use of SSLv3 and SSLv2 for TLS connections"));
		tlsProtocol.addOption(new Option("ssl3",
				"disable the use of TLSv1 and SSLv2 for TLS connections"));
		tlsProtocol.addOption(new Option("no_tls1",
				"disable the use of TLSv1 for TLS connections"));
		tlsProtocol.addOption(new Option("no_ssl3",
				"disable the use of SSLv3 for TLS connections"));
		tlsProtocol.addOption(new Option("no_ssl2",
				"disable the use of SSLv2 for TLS connections"));
		opts.addOptionGroup(tlsProtocol);

		opts.addOption("noclientauth", false,
				"disable client authentification for TLS");

		OptionBuilder.withArgName("file|url");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("file path or URL of P12 or JKS keystore, resource:tls/test_sys_2.p12 by default");
		opts.addOption(OptionBuilder.create("keystore"));

		OptionBuilder.withArgName("password");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("password for keystore file, 'secret' by default");
		opts.addOption(OptionBuilder.create("keystorepw"));

		OptionBuilder.withArgName("password");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("password for accessing the key in the keystore, keystore password by default");
		opts.addOption(OptionBuilder.create("keypw"));

		OptionBuilder.withArgName("file|url");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("file path or URL of JKS truststore, resource:tls/mesa_certs.jks by default");
		opts.addOption(OptionBuilder.create("truststore"));

		OptionBuilder.withArgName("password");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("password for truststore file, 'secret' by default");
		opts.addOption(OptionBuilder.create("truststorepw"));

		OptionBuilder.withArgName("dir");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("store received objects into files in specified directory <dir>."
						+ " Do not store received objects by default.");
		opts.addOption(OptionBuilder.create("dest"));

		OptionBuilder.withArgName("URL");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("jdbc connect  <URL>.\n example: "
				+ connectionStr);
		opts.addOption(OptionBuilder.create("jdbcconnect"));

		OptionBuilder.withArgName("file|url");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("file path or URL of properties for mapping Calling AETs to "
						+ "sub-directories of the storage directory specified by "
						+ "-dest, to separate the storage location dependend on "
						+ "Calling AETs.");
		opts.addOption(OptionBuilder.create("calling2dir"));

		OptionBuilder.withArgName("file|url");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("file path or URL of properties for mapping Called AETs to "
						+ "sub-directories of the storage directory specified by "
						+ "-dest, to separate the storage location dependend on "
						+ "Called AETs.");
		opts.addOption(OptionBuilder.create("called2dir"));

		OptionBuilder.withArgName("sub-dir");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("storage sub-directory used for Calling AETs for which no "
						+ " mapping is defined by properties specified by "
						+ "-calling2dir, 'OTHER' by default.");
		opts.addOption(OptionBuilder.create("callingdefdir"));

		OptionBuilder.withArgName("sub-dir");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("storage sub-directory used for Called AETs for which no "
						+ " mapping is defined by properties specified by "
						+ "-called2dir, 'OTHER' by default.");
		opts.addOption(OptionBuilder.create("calleddefdir"));

		OptionBuilder.withArgName("dir");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("register stored objects in cache journal files in specified directory <dir>."
						+ " Do not register stored objects by default.");
		opts.addOption(OptionBuilder.create("journal"));

		OptionBuilder.withArgName("pattern");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("cache journal file path, with "
				+ "'yyyy' will be replaced by the current year, "
				+ "'MM' by the current month, 'dd' by the current date, "
				+ "'HH' by the current hour and 'mm' by the current minute. "
				+ "'yyyy/MM/dd/HH/mm' by default.");
		opts.addOption(OptionBuilder.create("journalfilepath"));

		opts.addOption("defts", false, "accept only default transfer syntax.");
		opts.addOption("bigendian", false,
				"accept also Explict VR Big Endian transfer syntax.");
		opts.addOption("native", false,
				"accept only transfer syntax with uncompressed pixel data.");

		OptionBuilder.withArgName("maxops");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("maximum number of outstanding operations performed "
						+ "asynchronously, unlimited by default.");
		opts.addOption(OptionBuilder.create("async"));

		opts
				.addOption(
						"pdv1",
						false,
						"send only one PDV in one P-Data-TF PDU, "
								+ "pack command and data PDV in one P-DATA-TF PDU by default.");
		opts.addOption("tcpdelay", false,
				"set TCP_NODELAY socket option to false, true by default");

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("delay in ms for Socket close after sending A-ABORT, 50ms by default");
		opts.addOption(OptionBuilder.create("soclosedelay"));

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("delay in ms for DIMSE-RSP; useful for testing asynchronous mode");
		opts.addOption(OptionBuilder.create("rspdelay"));

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("timeout in ms for receiving -ASSOCIATE-RQ, 5s by default");
		opts.addOption(OptionBuilder.create("requestTO"));

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("timeout in ms for receiving A-RELEASE-RP, 5s by default");
		opts.addOption(OptionBuilder.create("releaseTO"));

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("period in ms to check for outstanding DIMSE-RSP, 10s by default");
		opts.addOption(OptionBuilder.create("reaper"));

		OptionBuilder.withArgName("ms");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("timeout in ms for receiving DIMSE-RQ, 60s by default");
		opts.addOption(OptionBuilder.create("idleTO"));

		OptionBuilder.withArgName("KB");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("maximal length in KB of received P-DATA-TF PDUs, 16KB by default");
		opts.addOption(OptionBuilder.create("rcvpdulen"));

		OptionBuilder.withArgName("KB");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("maximal length in KB of sent P-DATA-TF PDUs, 16KB by default");
		opts.addOption(OptionBuilder.create("sndpdulen"));

		OptionBuilder.withArgName("KB");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("set SO_RCVBUF socket option to specified value in KB");
		opts.addOption(OptionBuilder.create("sorcvbuf"));

		OptionBuilder.withArgName("KB");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("set SO_SNDBUF socket option to specified value in KB");
		opts.addOption(OptionBuilder.create("sosndbuf"));

		OptionBuilder.withArgName("KB");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("minimal buffer size to write received object to file, 1KB by default");
		opts.addOption(OptionBuilder.create("bufsize"));

		opts.addOption("h", "help", false, "print this message");
		opts.addOption("V", "version", false,
				"print the version information and exit");
		CommandLine cl = null;
		try {
			cl = new GnuParser().parse(opts, args);
		} catch (ParseException e) {
			exit("dcmrcv: " + e.getMessage());
			throw new RuntimeException("unreachable");
		}
		if (cl.hasOption("V")) {
			Package p = DcmRcv.class.getPackage();
			System.out.println("dcmrcv v" + p.getImplementationVersion());
			System.exit(0);
		}
		if (cl.hasOption("h") || cl.getArgList().size() == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
			System.exit(0);
		}
		return cl;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CommandLine cl = parse(args);
		DcmRcv dcmrcv = new DcmRcv(cl.hasOption("device") ? cl
				.getOptionValue("device") : "DCMRCV");
		final List<String> argList = cl.getArgList();
		String port = argList.get(0);
		String[] aetPort = split(port, ':', 1);
		dcmrcv.setPort(parseInt(aetPort[1], "illegal port number", 1, 0xffff));
		if (aetPort[0] != null) {
			String[] aetHost = split(aetPort[0], '@', 0);
			dcmrcv.setAEtitle(aetHost[0]);
			if (aetHost[1] != null) {
				dcmrcv.setHostname(aetHost[1]);
			}
		}

		if (cl.hasOption("jdbcconnect"))
			connectionStr = cl.getOptionValue("jdbcconnect");

		if (cl.hasOption("dest"))
			dcmrcv.setDestination(cl.getOptionValue("dest"));
		if (cl.hasOption("calling2dir"))
			dcmrcv.setCalling2Dir(loadProperties(cl
					.getOptionValue("calling2dir")));
		if (cl.hasOption("called2dir"))
			dcmrcv
					.setCalled2Dir(loadProperties(cl
							.getOptionValue("called2dir")));
		if (cl.hasOption("callingdefdir"))
			dcmrcv.setCallingDefDir(cl.getOptionValue("callingdefdir"));
		if (cl.hasOption("calleddefdir"))
			dcmrcv.setCalledDefDir(cl.getOptionValue("calleddefdir"));
		if (cl.hasOption("journal"))
			dcmrcv.setJournal(cl.getOptionValue("journal"));
		if (cl.hasOption("journalfilepath"))
			dcmrcv.setJournalFilePathFormat(cl
					.getOptionValue("journalfilepath"));

		if (cl.hasOption("defts"))
			dcmrcv.setTransferSyntax(ONLY_DEF_TS);
		else if (cl.hasOption("native"))
			dcmrcv.setTransferSyntax(cl.hasOption("bigendian") ? NATIVE_TS
					: NATIVE_LE_TS);
		else if (cl.hasOption("bigendian"))
			dcmrcv.setTransferSyntax(NON_RETIRED_TS);
		if (cl.hasOption("reaper"))
			dcmrcv
					.setAssociationReaperPeriod(parseInt(cl
							.getOptionValue("reaper"),
							"illegal argument of option -reaper", 1,
							Integer.MAX_VALUE));
		if (cl.hasOption("idleTO"))
			dcmrcv
					.setIdleTimeout(parseInt(cl.getOptionValue("idleTO"),
							"illegal argument of option -idleTO", 1,
							Integer.MAX_VALUE));
		if (cl.hasOption("requestTO"))
			dcmrcv.setRequestTimeout(parseInt(cl.getOptionValue("requestTO"),
					"illegal argument of option -requestTO", 1,
					Integer.MAX_VALUE));
		if (cl.hasOption("releaseTO"))
			dcmrcv.setReleaseTimeout(parseInt(cl.getOptionValue("releaseTO"),
					"illegal argument of option -releaseTO", 1,
					Integer.MAX_VALUE));
		if (cl.hasOption("soclosedelay"))
			dcmrcv.setSocketCloseDelay(parseInt(cl
					.getOptionValue("soclosedelay"),
					"illegal argument of option -soclosedelay", 1, 10000));
		if (cl.hasOption("rspdelay"))
			dcmrcv.setDimseRspDelay(parseInt(cl.getOptionValue("rspdelay"),
					"illegal argument of option -rspdelay", 0, 10000));
		if (cl.hasOption("rcvpdulen"))
			dcmrcv.setMaxPDULengthReceive(parseInt(cl
					.getOptionValue("rcvpdulen"),
					"illegal argument of option -rcvpdulen", 1, 10000)
					* KB);
		if (cl.hasOption("sndpdulen"))
			dcmrcv.setMaxPDULengthSend(parseInt(cl.getOptionValue("sndpdulen"),
					"illegal argument of option -sndpdulen", 1, 10000)
					* KB);
		if (cl.hasOption("sosndbuf"))
			dcmrcv.setSendBufferSize(parseInt(cl.getOptionValue("sosndbuf"),
					"illegal argument of option -sosndbuf", 1, 10000)
					* KB);
		if (cl.hasOption("sorcvbuf"))
			dcmrcv.setReceiveBufferSize(parseInt(cl.getOptionValue("sorcvbuf"),
					"illegal argument of option -sorcvbuf", 1, 10000)
					* KB);
		if (cl.hasOption("bufsize"))
			dcmrcv.setFileBufferSize(parseInt(cl.getOptionValue("bufsize"),
					"illegal argument of option -bufsize", 1, 10000)
					* KB);

		dcmrcv.setPackPDV(!cl.hasOption("pdv1"));
		dcmrcv.setTcpNoDelay(!cl.hasOption("tcpdelay"));
		if (cl.hasOption("async"))
			dcmrcv.setMaxOpsPerformed(parseInt(cl.getOptionValue("async"),
					"illegal argument of option -async", 0, 0xffff));
		dcmrcv.initTransferCapability();
		if (cl.hasOption("tls")) {
			String cipher = cl.getOptionValue("tls");
			if ("NULL".equalsIgnoreCase(cipher)) {
				dcmrcv.setTlsWithoutEncyrption();
			} else if ("3DES".equalsIgnoreCase(cipher)) {
				dcmrcv.setTls3DES_EDE_CBC();
			} else if ("AES".equalsIgnoreCase(cipher)) {
				dcmrcv.setTlsAES_128_CBC();
			} else {
				exit("Invalid parameter for option -tls: " + cipher);
			}
			if (cl.hasOption("tls1")) {
				dcmrcv.setTlsProtocol(TLS1);
			} else if (cl.hasOption("ssl3")) {
				dcmrcv.setTlsProtocol(SSL3);
			} else if (cl.hasOption("no_tls1")) {
				dcmrcv.setTlsProtocol(NO_TLS1);
			} else if (cl.hasOption("no_ssl3")) {
				dcmrcv.setTlsProtocol(NO_SSL3);
			} else if (cl.hasOption("no_ssl2")) {
				dcmrcv.setTlsProtocol(NO_SSL2);
			}
			dcmrcv.setTlsNeedClientAuth(!cl.hasOption("noclientauth"));

			if (cl.hasOption("keystore")) {
				dcmrcv.setKeyStoreURL(cl.getOptionValue("keystore"));
			}
			if (cl.hasOption("keystorepw")) {
				dcmrcv.setKeyStorePassword(cl.getOptionValue("keystorepw"));
			}
			if (cl.hasOption("keypw")) {
				dcmrcv.setKeyPassword(cl.getOptionValue("keypw"));
			}
			if (cl.hasOption("truststore")) {
				dcmrcv.setTrustStoreURL(cl.getOptionValue("truststore"));
			}
			if (cl.hasOption("truststorepw")) {
				dcmrcv.setTrustStorePassword(cl.getOptionValue("truststorepw"));
			}
			try {
				dcmrcv.initTLS();
			} catch (Exception e) {
				System.err.println("ERROR: Failed to initialize TLS context:"
						+ e.getMessage());
				System.exit(2);
			}
		}
		try {
			dcmrcv.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTransferSyntax(String[] tsuids) {
		this.tsuids = tsuids;
	}

	public void initTransferCapability() {
		TransferCapability[] tc = new TransferCapability[CUIDS.length + 1];
		tc[0] = new TransferCapability(UID.VerificationSOPClass, ONLY_DEF_TS,
				TransferCapability.SCP);
		for (int i = 0; i < CUIDS.length; i++)
			tc[i + 1] = new TransferCapability(CUIDS[i], tsuids,
					TransferCapability.SCP);
		ae.setTransferCapability(tc);
	}

	public void setFileBufferSize(int size) {
		fileBufferSize = size;
	}

	public void setMaxOpsPerformed(int maxOps) {
		ae.setMaxOpsPerformed(maxOps);
	}

	public void setDestination(String filePath) {
		File f = new File(filePath);
		if ("/dev/null".equals(filePath)) {
			devnull = f;
			cache.setCacheRootDir(null);
		} else {
			devnull = null;
			cache.setCacheRootDir(f);
		}
	}

	public void setCalling2Dir(Properties calling2dir) {
		this.calling2dir = calling2dir;
	}

	public void setCalled2Dir(Properties called2dir) {
		this.called2dir = called2dir;
	}

	private static Properties loadProperties(String url) {
		Properties props = new Properties();
		try {
			InputStream inStream = openFileOrURL(url);
			try {
				props.load(inStream);
			} finally {
				inStream.close();
			}
		} catch (Exception e) {
			exit("Failed to load properties from " + url);
		}
		return props;
	}

	public void setCallingDefDir(String callingdefdir) {
		this.callingdefdir = callingdefdir;
	}

	public void setCalledDefDir(String calleddefdir) {
		this.calleddefdir = calleddefdir;
	}

	public void setJournal(String journalRootDir) {
		cache.setJournalRootDir(new File(journalRootDir));
		// Prefix JournalFileName to distinguish from journal files created
		// by other applications than DcmRcv
		cache.setJournalFileName("DcmRcv." + cache.getJournalFileName());
	}

	public void setJournalFilePathFormat(String format) {
		cache.setJournalFilePathFormat(format);
	}

	public void initTLS() throws GeneralSecurityException, IOException {
		KeyStore keyStore = loadKeyStore(keyStoreURL, keyStorePassword);
		KeyStore trustStore = loadKeyStore(trustStoreURL, trustStorePassword);
		device.initTLS(keyStore, keyPassword != null ? keyPassword
				: keyStorePassword, trustStore);
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
			return DcmRcv.class.getClassLoader().getResourceAsStream(
					url.substring(9));
		}
		try {
			return new URL(url).openStream();
		} catch (MalformedURLException e) {
			return new FileInputStream(url);
		}
	}

	private static String toKeyStoreType(String fname) {
		return fname.endsWith(".p12") || fname.endsWith(".P12") ? "PKCS12"
				: "JKS";
	}

	public void start() throws IOException {
		device.startListening(executor);
		System.out.println("Start Server listening on port " + nc.getPort());
	}

	public void stop() {
		if (device != null)
			device.stopListening();

		if (nc != null)
			System.out.println("Stop Server listening on port " + nc.getPort());
		else
			System.out.println("Stop Server");
	}

	private static String[] split(String s, char delim, int defPos) {
		String[] s2 = new String[2];
		s2[defPos] = s;
		int pos = s.indexOf(delim);
		if (pos != -1) {
			s2[0] = s.substring(0, pos);
			s2[1] = s.substring(pos + 1);
		}
		return s2;
	}

	private static void exit(String msg) {
		System.err.println(msg);
		System.err.println("Try 'dcmrcv -h' for more information.");
		System.exit(1);
	}

	private static int parseInt(String s, String errPrompt, int min, int max) {
		try {
			int i = Integer.parseInt(s);
			if (i >= min && i <= max)
				return i;
		} catch (NumberFormatException e) {
			// parameter is not a valid integer; fall through to exit
		}
		exit(errPrompt);
		throw new RuntimeException();
	}

	/**
	 * Overwrite {@link StorageService#cstore} to send delayed C-STORE RSP by
	 * separate Thread, so reading of following received C-STORE RQs from the
	 * open association is not blocked.
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
		if (devnull == null && cache.getCacheRootDir() == null) {
			super.onCStoreRQ(as, pcid, rq, dataStream, tsuid, rsp);
		} else {
			String cuid = rq.getString(Tag.AffectedSOPClassUID);
			String iuid = rq.getString(Tag.AffectedSOPInstanceUID);

			File file = null;

			try {

				// проверяем, есть ли уже запись о файле в БД и берем его путь.
				String findName;
				try {

					checkMakeConnection();

					 
					String f = iuid + dcmFileExt;
//					System.out.println("[0]!!!! finded in DB " + f);
					findName = getDCMFileNamefromDB(f);
//					System.out.println("[1]!!!! finded in DB " + findName);

					if (findName != null) {

						findName = cache.getCacheRootDir() + File.separator
								+ findName;
//						System.out.println("[2]!!!! finded in DB " + findName);

						Matcher matcher = Pattern.compile("^(.*).dcm$")
								.matcher(findName);
						if (matcher.matches()) {
							findName = matcher.group(1) + ".part";
						}

//						System.out.println("[3]!!!! finded in DB " + findName);
						file = new File(findName);
					}

				} catch (SQLException e) {
					throw new IOException("SQL Exception! " + e);
				}

				if (findName == null) {
					file = devnull != null ? devnull : new File(mkDir(as), iuid
							+ ".part");

				} else {

				}
				LOG.info("M-WRITE {}", file);

				DicomOutputStream dos = new DicomOutputStream(
						new BufferedOutputStream(new FileOutputStream(file),
								fileBufferSize));
				try {
					BasicDicomObject fmi = new BasicDicomObject();
					fmi.initFileMetaInformation(cuid, iuid, tsuid);
					dos.writeFileMetaInformation(fmi);
					dataStream.copyTo(dos);
				} finally {
					CloseUtils.safeClose(dos);
				}

			} catch (IOException e) {
				if (devnull == null && file != null) {
					if (file.delete()) {
						LOG.info("M-DELETE {}", file);
					}
				}
				throw new DicomServiceException(rq, Status.ProcessingFailure, e
						.getMessage());
			}

			// Rename the file after it has been written. See DCM-279
			if (devnull == null && file != null) {
				// Добавляем расширение
				File rename = new File(file.getParent(), iuid + dcmFileExt);
				LOG.info("M-RENAME {} to {}", file, rename);
				file.renameTo(rename);
				// System.out.println("!! rename="+rename);

				try {
					extractImagesAndSaveDB(rename);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					String errid = "DcmRcv" + "_" + new Date().getTime();
					LOG.error("******** DATABASE SQL Error ********** ERRID=["
							+ errid + "] " + e);

					// throw new IOException("DATABASE Error ERRID=[" + errid
					// + "]" + e.getMessage());
					if (devnull == null && rename != null) {
						if (rename.delete()) {
							LOG.info("M-DELETE {}", rename);
						}

						// Удаляем извлеченные картинки
						File imageDir = new File(rename.getPath()
								+ imageDirPrefix);

						File[] files = imageDir.listFiles();
						for (int i = 0; i < files.length; i++) {
							if (files[i].isDirectory()) {
								if (files[i].delete()) {
									LOG.info("M-DELETE IMAGE {}", files[i]);
								}
							} else {
								files[i].delete();
							}
						}
						if (imageDir.delete()) {
							LOG.info("M-DELETE IMAGE DIR {}", rename);
						}

					}
					throw new DicomServiceException(rq,
							Status.ProcessingFailure, e.getMessage());

				}

				if (cache.getJournalRootDir() != null) {
					cache.record(rename);
				}
			}
		}
	}

	private File mkDir(Association as) {
		File dir = cache.getCacheRootDir();
		if (called2dir == null && calling2dir == null) {
			// относительный путь к папке
			String relativePath = getRelativeIternalDirPath();
			File dirAddon = new File(dir, relativePath);

			if (dirAddon.mkdirs()) {
				LOG.info("M-WRITE MAKEDIR ADDON {}", dirAddon);
			}
			return dirAddon;
		}
		if (called2dir != null) {
			dir = new File(dir, called2dir.getProperty(as.getCalledAET(),
					calleddefdir));
		}
		if (calling2dir != null) {
			dir = new File(dir, calling2dir.getProperty(as.getCallingAET(),
					callingdefdir));
		}

		if (dir.mkdirs()) {
			LOG.info("M-WRITE {}", dir);
		}
		return dir;
	}

	/**
	 * Получение пути к файлу относительно корня архива
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeFilePath(File file) {
		File dir = cache.getCacheRootDir();
		String s = file.getPath().replaceFirst(
				Matcher.quoteReplacement(dir.getPath() + File.separator), "");
		return s;
	}

	/**
	 * Получение имени DCM-файла в архиве
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeDcmFileName(File file) {
		// TODO Учесть File.separator
		Matcher matcher = Pattern.compile(".*\\\\(.*)$")
				.matcher(file.getPath());
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * Получение имени JPEG-файла в архиве
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeImageFileName(File file) {
		// TODO Учесть File.separator
		Matcher matcher = Pattern.compile(".*\\\\(.*)\\\\(.*)$").matcher(
				file.getPath());
		if (matcher.matches()) {
			return matcher.group(1) + File.separator + matcher.group(2);
		}
		return null;
	}

	/**
	 * Получение папки относительного пути папки внутри архива
	 * 
	 * @return
	 */
	private String getRelativeIternalDirPath() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatLevel1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatLevel2 = new SimpleDateFormat("H");
		String level1 = formatLevel1.format(calendar.getTime());
		String level2 = formatLevel2.format(calendar.getTime());
		return level1 + File.separator + level2;
	}

	/**
	 * Проверка-установка соединения
	 * 
	 * @throws SQLException
	 */
	private void checkMakeConnection() throws SQLException {

		if (connection != null && connection.isValid(0)) {
			return;
		}

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); // FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		Connection conn = DriverManager.getConnection(connectionStr
				+ ";create=true", props);
		// conn.setAutoCommit(false);
		// s = conn.createStatement();
		// s.execute(sql);
		//
		// conn.commit();

		// return conn;
		connection = conn;
	}

	/**
	 * Извлечение картинок и запись в БД
	 * 
	 * @param file
	 * @throws SQLException
	 * @throws IOException
	 */
	private void extractImagesAndSaveDB(File file) throws SQLException,
			IOException {

		ArrayList<String> images = extractImages(file);

		connection.setAutoCommit(false);
		insertUpdateCommonData(file, images);

		// insertImageData(DCM_FILE_NAME, "image/jpeg", IMAGE_FILE_NAME,
		// IMAGE_FILE_SIZE, WIDTH, HEIGHT);
		// updateDayStatInc(STUDY_DATE, "ALL_IMAGE_SIZE", IMAGE_FILE_SIZE);
		connection.commit();

	}

	/**
	 * Извлечение картинок
	 * 
	 * @param dcmFile
	 * @throws IOException
	 * @throws SQLException
	 */
	public ArrayList<String> extractImages(File dcmFile) throws IOException,
			SQLException {

		ArrayList<String> resultImages = new ArrayList<String>();
		File dest = new File(dcmFile.getPath() + imageDirPrefix);
		dest.mkdirs();

		dest = new File(dest, "1" + imageFileExt);
		// TODO Тут может быть наверное несколько картинок !!!

		Iterator<ImageReader> iter = ImageIO
				.getImageReadersByFormatName("DICOM");

		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader
				.getDefaultReadParam();
		param.setWindowCenter(center);
		param.setWindowWidth(width);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(dcmFile);
		BufferedImage bi;
		OutputStream out = null;
		try {
			reader.setInput(iis, false);
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				System.out.println("\nError: " + dcmFile + " - couldn't read!");
				return resultImages;
			}
			out = new BufferedOutputStream(new FileOutputStream(dest));
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);
		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);
			resultImages.add(dest.getPath());
		}
		return resultImages;
	}

	/**
	 * @param dcmFile
	 * @throws SQLException
	 * @throws IOException
	 */
	private void insertUpdateCommonData(File dcmFile, ArrayList<String> images)
			throws SQLException, IOException {

		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = null;

		try {
			long DCM_FILE_SIZE = dcmFile.length();
			din = new DicomInputStream(dcmFile);
			dcmObj = din.readDicomObject();

			// проверки

			// TODO Дать возможность задания с коммандной строки
			String charsetStr = null;
			if (charsetStr != null) {
				cs = new SpecificCharacterSet(charsetStr);
			}

			// читаем кодировку из dcm-файла
			if (charsetStr == null) {
				cs = SpecificCharacterSet.valueOf(dcmObj.get(
						Tag.SpecificCharacterSet).getStrings(null, false));
			}

			String DCM_FILE_NAME = getRelativeFilePath(dcmFile);
			String NAME = getRelativeDcmFileName(dcmFile);

			java.sql.Date PATIENT_BIRTH_DATE;

			if (dcmObj.get(Tag.PatientBirthDate) != null) {
				PATIENT_BIRTH_DATE = new java.sql.Date(dcmObj.get(
						Tag.PatientBirthDate).getDate(false).getTime());
			} else {
				PATIENT_BIRTH_DATE = new java.sql.Date(0);
				LOG
						.warn("Patient Birth Date (tag: PatientBirthDate) is empty!");
			}

			DicomElement element1 = dcmObj.get(Tag.PatientName);
			String PATIENT_NAME = element1.getValueAsString(cs, element1
					.length());

			element1 = dcmObj.get(Tag.PatientID);
			String PATIENT_ID = element1
					.getValueAsString(cs, element1.length());

			if (PATIENT_ID == null || PATIENT_ID.length() == 0) {
				PATIENT_ID = "не указан";
			}

			element1 = dcmObj.get(Tag.PatientSex);
			String PATIENT_SEX = "";
			if (element1 == null) {
				LOG.warn("Patient sex (tag: PatientSex) is empty!");
			} else {
				PATIENT_SEX = element1.getValueAsString(cs, element1.length());
				if (PATIENT_SEX.length() > 1) {
					LOG.warn("PATIENT_SEX to long [" + PATIENT_SEX + "]");
					PATIENT_SEX = PATIENT_SEX.substring(0, 1);
				}
			}

			element1 = dcmObj.get(Tag.StudyID);
			String STUDY_ID = "";
			if (element1 == null) {
				LOG.warn("Study ID (tag: StudyID) is empty!");
			} else {
				STUDY_ID = element1.getValueAsString(cs, element1.length());
			}

			java.sql.Date STUDY_DATE = new java.sql.Date(dcmObj.get(
					Tag.StudyDate).getDate(false).getTime());

			String STUDY_DOCTOR = "не указан";
			element1 = dcmObj.get(Tag.ReferringPhysicianName);
			if (element1 != null) {
				STUDY_DOCTOR = element1.getValueAsString(cs, element1.length());
				if (STUDY_DOCTOR == null || STUDY_DOCTOR.length() == 0) {
					STUDY_DOCTOR = "не указан";
				}
			}

			String STUDY_OPERATOR = "не указан";
			element1 = dcmObj.get(Tag.OperatorsName);
			if (element1 != null) {
				STUDY_OPERATOR = element1.getValueAsString(cs, element1
						.length());
				if (STUDY_OPERATOR == null || STUDY_OPERATOR.length() == 0) {
					STUDY_OPERATOR = "не указан";
				}
			}

			int HEIGHT = dcmObj.get(Tag.Rows).getInt(false);
			int WIDTH = dcmObj.get(Tag.Columns).getInt(false);

			// Вставка в БД

			PreparedStatement stmt = null;

			LOG.info("[" + DCM_FILE_NAME + "][" + PATIENT_NAME + "]["
					+ PATIENT_BIRTH_DATE + "][" + STUDY_DATE + "]");

			// Проверка на наличии этого файла в БД
			try {
				int id = checkDbDCMFile(DCM_FILE_NAME);
				LOG.info("File already in database [" + id + "] ["
						+ DCM_FILE_NAME + "]");
				LOG.info("update data in database [" + DCM_FILE_NAME + "]");

				stmt = connection
						.prepareStatement("update WEBDICOM.DCMFILE"
								+ " SET NAME = ? , DCM_FILE_SIZE = ? , PATIENT_NAME = ?, PATIENT_SEX = ?, PATIENT_BIRTH_DATE = ?, "
								+ " STUDY_ID =? , STUDY_DATE = ?, STUDY_DOCTOR =? , STUDY_OPERATOR = ?"
								+ " where ID = ?");

				stmt.setString(1, NAME);
				stmt.setLong(2, DCM_FILE_SIZE);
				stmt.setString(3, PATIENT_NAME);
				stmt.setString(4, PATIENT_SEX);
				stmt.setDate(5, PATIENT_BIRTH_DATE);
				stmt.setString(6, STUDY_ID);
				stmt.setDate(7, STUDY_DATE);
				stmt.setString(8, STUDY_DOCTOR);
				stmt.setString(9, STUDY_OPERATOR);
				stmt.setInt(10, id);
				stmt.executeUpdate();

				LOG.info("skip converting image.");

			} catch (NoDataFoundException ex) {
				// Делаем вставку
				LOG.info("insert data in database [" + DCM_FILE_NAME + "]");
				stmt = connection
						.prepareStatement("insert into WEBDICOM.DCMFILE"
								+ " (DCM_FILE_NAME, NAME, DCM_FILE_SIZE, PATIENT_ID, PATIENT_NAME, PATIENT_SEX, PATIENT_BIRTH_DATE,"
								+ " STUDY_ID, STUDY_DATE, STUDY_DOCTOR, STUDY_OPERATOR)"
								+ " values (?,?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");

				stmt.setString(1, DCM_FILE_NAME);
				stmt.setString(2, NAME);
				stmt.setLong(3, DCM_FILE_SIZE);
				stmt.setString(4, PATIENT_ID);
				stmt.setString(5, PATIENT_NAME);
				stmt.setString(6, PATIENT_SEX);
				stmt.setDate(7, PATIENT_BIRTH_DATE);
				stmt.setString(8, STUDY_ID);
				stmt.setDate(9, STUDY_DATE);
				stmt.setString(10, STUDY_DOCTOR);
				stmt.setString(11, STUDY_OPERATOR);

				stmt.executeUpdate();
				// Обновляем статистику
				updateDayStatInc(STUDY_DATE, "ALL_DCM_SIZE", DCM_FILE_SIZE);
			} finally {
				if (stmt != null)
					stmt.close();
			}

			// Вставка в БД информации о картинках

			for (Iterator<String> it = images.iterator(); it.hasNext();) {
				String fileImage = it.next();
				insertImageData(dcmFile, new File(fileImage), STUDY_DATE,
						WIDTH, HEIGHT);
			}

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				// TODO Дать возможность получения кодировки из коммандной
				// строки
				LOG.error("Unsupported character set " + e);
				// LOG.fatal("Unsupported character set" + charsetStr + " " +
				// e);
			}
			LOG.error("" + e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("" + e);
			throw e;

		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}

	}

	/**
	 * Проверка на наличии информации о DCM-файле в БД
	 * 
	 * @param fileName
	 * @return старое местоположение
	 * @throws SQLException
	 */
	private String getDCMFileNamefromDB(String fileName) throws SQLException {

//		String name = getRelativeDcmFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT DCM_FILE_NAME FROM WEBDICOM.DCMFILE WHERE NAME = ?");
		try {
			psSelect.setString(1, fileName);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getString("DCM_FILE_NAME");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		return null;
	}

	/**
	 * Проверка на наличии информации о DCM-файле в БД
	 * 
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	private int checkDbDCMFile(String fileName) throws SQLException {

		String name = getRelativeDcmFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.DCMFILE WHERE NAME = ?");
		try {
			psSelect.setString(1, name);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getInt("ID");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

	/**
	 * Проверка на наличии информации о Картинке-файле в БД
	 * 
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	private int checkDbImageFile(String fileName) throws SQLException {

		String name = getRelativeImageFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.IMAGES WHERE NAME = ?");
		try {
			psSelect.setString(1, name);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getInt("ID");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

	/**
	 * Вставка информации о картинках в БД
	 * 
	 * @param dcmFile
	 * @param imageFile
	 * @throws SQLException
	 */
	private void insertImageData(File dcmFile, File imageFile,
			java.sql.Date STUDY_DATE, int WIDTH, int HEIGHT)
			throws SQLException {

		Integer FID_DCMFILE = checkDbDCMFile(getRelativeFilePath(dcmFile));
		String IMAGE_FILE_NAME = getRelativeFilePath(imageFile);
		String NAME = getRelativeImageFileName(imageFile);
		long IMAGE_FILE_SIZE = imageFile.length();
		String CONTENT_TYPE = imageContentType;

		try {
			int idImage = checkDbImageFile(IMAGE_FILE_NAME);

			PreparedStatement psUpdate = null;

			LOG.info("update data in database [" + FID_DCMFILE + "] image ["
					+ IMAGE_FILE_NAME + "]");
			psUpdate = connection.prepareStatement("update WEBDICOM.IMAGES"
					+ " set FID_DCMFILE = ? ,"
					+ " CONTENT_TYPE = ? , IMAGE_FILE_NAME =? , NAME = ?, "
					+ " IMAGE_FILE_SIZE = ?, WIDTH = ?, HEIGHT = ?"
					+ " where ID = ?");

			psUpdate.setInt(1, FID_DCMFILE);
			psUpdate.setString(2, CONTENT_TYPE);
			psUpdate.setString(3, IMAGE_FILE_NAME);
			psUpdate.setString(4, NAME);
			psUpdate.setLong(5, IMAGE_FILE_SIZE);
			psUpdate.setInt(6, WIDTH);
			psUpdate.setInt(7, HEIGHT);
			psUpdate.setInt(8, idImage);
			psUpdate.executeUpdate();
			psUpdate.close();

		} catch (NoDataFoundException ex) {
			PreparedStatement psInsert = null;

			LOG.info("insert data in database [" + FID_DCMFILE + "] image ["
					+ IMAGE_FILE_NAME + "]");

			psInsert = connection
					.prepareStatement("insert into WEBDICOM.IMAGES"
							+ " (FID_DCMFILE, CONTENT_TYPE, IMAGE_FILE_NAME, NAME,  IMAGE_FILE_SIZE, WIDTH, HEIGHT)"
							+ " values (?, ?, ?, ?, ?, ?, ?)");

			psInsert.setInt(1, FID_DCMFILE);
			psInsert.setString(2, CONTENT_TYPE);
			psInsert.setString(3, IMAGE_FILE_NAME);
			psInsert.setString(4, NAME);
			psInsert.setLong(5, IMAGE_FILE_SIZE);
			psInsert.setInt(6, WIDTH);
			psInsert.setInt(7, HEIGHT);
			psInsert.executeUpdate();
			psInsert.close();

		}

		// Обновление статистики
		updateDayStatInc(STUDY_DATE, "ALL_IMAGE_SIZE", IMAGE_FILE_SIZE);

	}

	/**
	 * Обновление метрики дневной статистики (инкремент)
	 * 
	 * @param date
	 * @param metric
	 * @param value
	 * @throws SQLException
	 */
	private void updateDayStatInc(java.util.Date date, String metric, long value)
			throws SQLException {

		PreparedStatement stmt = null;

		// Calendar calendar = Calendar.getInstance();
		// long time = calendar.getTimeInMillis();
		// time = time - (time % (60 * 60 * 24 * 1000));
		// // calendar.setTimeInMillis(time);

		long time = date.getTime();
		time = time - (time % (60 * 60 * 24 * 1000));
		// date = new Date(time);

		LOG.info(metric + "=" + value + " of " + date);

		// Проверка на наличии этого файла в БД
		try {
			long valueOld = checkDayMetric(metric, new java.sql.Date(time));
			LOG.info("metric already in database [" + metric + "][" + date
					+ "][" + valueOld + "]");

			stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT "
					+ " SET METRIC_VALUE_LONG = ? "
					+ " where METRIC_NAME = ? AND METRIC_DATE = ?");

			long sumVal = value + valueOld;
			stmt.setLong(1, sumVal);
			stmt.setString(2, metric);
			stmt.setDate(3, new java.sql.Date(time));
			stmt.executeUpdate();

		} catch (NoDataFoundException ex) {
			// Делаем вставку
			LOG.info("insert data in database [" + metric + "][" + date + "]["
					+ value + "]");
			stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
					+ " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)"
					+ " values (?, ?, ?)");

			stmt.setString(1, metric);
			stmt.setDate(2, new java.sql.Date(time));
			stmt.setLong(3, value);
			stmt.executeUpdate();

		} finally {
			if (stmt != null)
				stmt.close();
		}

	}

	/**
	 * @param metric
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private long checkDayMetric(String metric, java.sql.Date date)
			throws SQLException {
		PreparedStatement psSelect = connection
				.prepareStatement("SELECT METRIC_VALUE_LONG FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and METRIC_DATE =? ");
		try {
			psSelect.setString(1, metric);
			psSelect.setDate(2, date);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getLong("METRIC_VALUE_LONG");
			}

		} finally {
			psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

}
