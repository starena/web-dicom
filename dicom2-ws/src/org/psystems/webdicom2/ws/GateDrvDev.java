package org.psystems.webdicom2.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.io.FileUtils;
import org.psystems.webdicom2.ws.dto.DCM;
import org.psystems.webdicom2.ws.dto.DCMTag;
import org.psystems.webdicom2.ws.dto.Direction;
import org.psystems.webdicom2.ws.dto.RISCode;
import org.psystems.webdicom2.ws.dto.StudyResult;

public class GateDrvDev implements IGate {

	public static Logger logger = Logger.getLogger(GateDrvDev.class.getName());

	@Resource
	private WebServiceContext context;

	public static String testDrnDataDir = "/tmp/webdicom";
	public static String testDrnDatafile = "direction.xml";
	public static String testDCMDatafile = "dcm.xml";
	public static String attachmentUrl = "/dicom2-ws/attach/";

	public SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

	private Gate gate;

	// /private static Properties drnProp;

	public GateDrvDev(Gate gate) {
		this.gate = gate;
	}

	public GateDrvDev() {
		super();
	}

	/**
	 * For Testing
	 * 
	 * @param misId
	 * @return
	 * @throws IOException
	 */
	private Properties loadDrnFromPropFile(String misId) throws IOException {

		Properties drnProp = new Properties();

		try {
			FileInputStream fis = new FileInputStream(testDrnDataDir
					+ File.separator + misId + File.separator + testDrnDatafile);
			drnProp.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// empty properties
		}

		return drnProp;

	}

	/**
	 * @param drnProp
	 * @throws IOException
	 */
	private void saveDrnToPropFile(Properties drnProp) throws IOException {

		Properties tmp = new Properties() {

			@Override
			public Set<Object> keySet() {
				return Collections.unmodifiableSet(new TreeSet<Object>(super
						.keySet()));
			}

		};

		tmp.putAll(drnProp);

		File theDir = new File(testDrnDataDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}

		theDir = new File(testDrnDataDir + File.separator
				+ drnProp.getProperty("misId"));
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}

		FileOutputStream fos = new FileOutputStream(testDrnDataDir
				+ File.separator + drnProp.getProperty("misId")
				+ File.separator + testDrnDatafile);
		tmp.storeToXML(fos, "Webdicom direction properties file", "UTF-8");
		fos.close();
	}

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public Direction sendDirection(Direction drn) throws WsException {

		try {

			Properties drnProp = loadDrnFromPropFile(drn.getBarCode());

			drnProp.put("barCode", drn.getBarCode());
			drnProp.put("misId", drn.getMisId());
			drnProp.put("dateBirsday", drn.getDateBirsday());
			drnProp.put("dateStudy", drn.getDateStudy());
			drnProp.put("modality", drn.getModality());
			drnProp.put("patientId", drn.getPatientId());
			drnProp.put("patientName", drn.getPatientName());
			drnProp.put("serviceName", drn.getServiceName());
			drnProp.put("sex", drn.getSex());

			saveDrnToPropFile(drnProp);

		} catch (IOException e) {
			e.printStackTrace();
			throw new WsException(e);
		}

		return drn;
	}

	/**
	 * Удалить направление
	 * 
	 * @param misId
	 *            - ID из МИС
	 * 
	 * @return - количество связанных исследований
	 * @throws WsException
	 * 
	 */
	public int removeDirection(String misId) throws WsException {
		File drnDir = new File(testDrnDataDir + File.separator + misId);
		System.out.println("! path=" + drnDir.getAbsolutePath());
		try {
			FileUtils.deleteDirectory(drnDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WsException(e);
		}
		return 0;
	}

	/**
	 * Получение списка выполненных сейрий
	 * 
	 * @param misId
	 * @return
	 */
	public DCM[] getDCM(String misId) {

		File drnDir = new File(testDrnDataDir + File.separator + misId);
		ArrayList<DCM> result = new ArrayList<DCM>();

		File[] files = drnDir.listFiles();
		for (File dcmDir : files) {
			if (dcmDir.isDirectory()) {

				result.add(getDCMDto(drnDir, dcmDir));
			}
		}

		return result.toArray(new DCM[result.size()]);

	}

	/**
	 * @param drnDir
	 * @param dcmDir
	 * @return
	 */
	private DCM getDCMDto(File drnDir, File dcmDir) {
		DCM dcmDto = new DCM();

		HttpServletRequest request = (HttpServletRequest) gate.getMessageContext()
				.get(MessageContext.SERVLET_REQUEST);

		String hostName = request.getServerName();
		int port = request.getServerPort();

		String serverUrl = "http://" + hostName + ":" + port;

		File[] dataFiles = dcmDir.listFiles();
		for (File datafile : dataFiles) {
			if (datafile.getName().endsWith(".pdf")
					|| datafile.getName().endsWith(".jpg")) {

				if (datafile.getName().endsWith(".pdf")) {
					dcmDto.pdfId = dcmDir.getName();
					dcmDto.contentUrl = serverUrl + attachmentUrl
							+ dcmDir.getName() + ".pdf";
				}
				if (datafile.getName().endsWith(".jpg")) {
					dcmDto.imageId = dcmDir.getName();
					dcmDto.contentUrl = serverUrl + attachmentUrl
							+ dcmDir.getName() + ".jpg";
				}

				dcmDto.dcmId = dcmDir.getName();
				dcmDto.misId = drnDir.getName();

			}
		}
		return dcmDto;
	}

	/**
	 * 
	 * DCM-ки по дате
	 * 
	 * @param date
	 *            - формат YYYYMMDD
	 * @return
	 * @throws WsException
	 */
	public DCM[] getDCMbyDate(String date) throws WsException {

		ArrayList<DCM> result = new ArrayList<DCM>();

		File dicomDir = new File(testDrnDataDir);
		File[] drnDirs = dicomDir.listFiles();
		for (File drnDir : drnDirs) {
			if (drnDir.isDirectory()) {

				File[] dcmDirs = drnDir.listFiles();
				for (File dcmDir : dcmDirs) {
					if (dcmDir.isDirectory()) {

						Properties dcmProp = new Properties();

						try {
							FileInputStream fis = new FileInputStream(
									testDrnDataDir + File.separator
											+ drnDir.getName() + File.separator
											+ dcmDir.getName() + File.separator
											+ testDCMDatafile);
							dcmProp.loadFromXML(fis);
							fis.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							throw new WsException(e);
						} catch (InvalidPropertiesFormatException e) {
							e.printStackTrace();
							throw new WsException(e);
						} catch (IOException e) {
							e.printStackTrace();
							throw new WsException(e);
						}

						if (dcmProp.get("StudyDate").equals(date)) {

							result.add(getDCMDto(drnDir, dcmDir));
						}

					}
				}

			}
		}

		return result.toArray(new DCM[result.size()]);

	}

	/**
	 * Список тэгов
	 * 
	 * @param dcmId
	 * @return
	 * @throws WsException
	 */
	public DCMTag[] getDCMTags(String dcmId) throws WsException {

		File dicomDir = new File(testDrnDataDir);
		File[] drnDirs = dicomDir.listFiles();
		for (File drnDir : drnDirs) {
			if (drnDir.isDirectory()) {

				File[] dcmDirs = drnDir.listFiles();
				for (File dcmDir : dcmDirs) {
					if (dcmDir.isDirectory() && dcmId.equals(dcmDir.getName())) {

						Properties dcmProp = new Properties();

						try {
							FileInputStream fis = new FileInputStream(
									testDrnDataDir + File.separator
											+ drnDir.getName() + File.separator
											+ dcmDir.getName() + File.separator
											+ testDCMDatafile);
							dcmProp.loadFromXML(fis);
							fis.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							throw new WsException(e);
						} catch (InvalidPropertiesFormatException e) {
							e.printStackTrace();
							throw new WsException(e);
						} catch (IOException e) {
							e.printStackTrace();
							throw new WsException(e);
						}

						ArrayList<DCMTag> tags = new ArrayList<DCMTag>();

						for (Iterator<Object> iter = dcmProp.keySet()
								.iterator(); iter.hasNext();) {
							String key = (String) iter.next();
							String val = (String) dcmProp.get(key);
							DCMTag tag = new DCMTag();
							tag.name = key;
							tag.value = val;
							tag.id = "";
							tag.type = "";
							tags.add(tag);
						}

						return tags.toArray(new DCMTag[tags.size()]);

					}
				}

			}
		}
		return null;
	}

	/**
	 * 
	 * Получение бинарного контента
	 * 
	 * @param misId
	 * @param contentId
	 * @return
	 * @throws IOException
	 */
	public byte[] getDCMContent(String misId, String contentId)
			throws WsException {

		File drnDir = new File(testDrnDataDir + File.separator + misId);

		File[] files = drnDir.listFiles();
		for (File studyDir : files) {
			if (studyDir.isDirectory() && studyDir.getName().equals(contentId)) {

				File[] dataFiles = studyDir.listFiles();
				for (File datafile : dataFiles) {
					if (datafile.getName().endsWith(".pdf")
							|| datafile.getName().endsWith(".jpg")) {

						String fileID = datafile.getName().replaceFirst(".pdf",
								"");
						fileID = datafile.getName().replaceFirst(".jpg", "");

						// fileID = "data.jpg";

						// System.out.println("!!!! id="+id);
						// System.out.println("!!!! fileID="+fileID);
						// System.out.println("!!!! datafile="+datafile);

						// try {
						// return ImageIO.read(datafile);
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// throw new WsException(e.getCause());
						// }

						try {
							FileInputStream fis = new FileInputStream(datafile);
							byte[] data = new byte[(int) datafile.length()];
							fis.read(data);
							fis.close();
							return data;
						} catch (IOException e) {
							e.printStackTrace();
							throw new WsException(e.getCause());
						}

					}
				}
			}
		}
		return null;

	}

	/**
	 * @param misId
	 * @return
	 */
	public StudyResult getCompliteStudyResult(String misId) {

		StudyResult result = new StudyResult();

		File drnDir = new File(testDrnDataDir + File.separator + misId);
		ArrayList<String> pdfs = new ArrayList<String>();
		ArrayList<String> jpgs = new ArrayList<String>();

		File[] files = drnDir.listFiles();
		for (File dcmDir : files) {
			if (dcmDir.isDirectory()) {
				File[] dataFiles = dcmDir.listFiles();
				for (File datafile : dataFiles) {
					if (datafile.getName().endsWith(".pdf")) {
						pdfs.add(dcmDir.getName());
					} else if (datafile.getName().endsWith(".jpg")) {
						jpgs.add(dcmDir.getName());
					}
				}
			}
		}

		result.imageUrls = jpgs.toArray(new String[jpgs.size()]);
		result.pdfUrls = pdfs.toArray(new String[pdfs.size()]);

		try {

			Properties drnProp = loadDrnFromPropFile(misId);
			result.result = drnProp.getProperty("finalResult");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Создание PDF-ки в исследовании
	 * 
	 * @param misId
	 * @param studyUID
	 * @param content
	 * @return
	 */
	public String sendPdf(String misId, byte[] content) throws WsException {

		long id = new Date().getTime();
		File theDir = new File(testDrnDataDir + File.separator + misId
				+ File.separator + id);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}
		try {
			// Properties drnProp = loadDrnFromPropFile(barCode);

			String filename = "data.pdf";
			// drnProp.put("pdf." + id, filename);
			FileOutputStream fos = new FileOutputStream(testDrnDataDir
					+ File.separator + misId + File.separator + id
					+ File.separator + filename);
			fos.write(content);
			fos.flush();
			fos.close();

			DCM dcm = new DCM();
			dcm.dcmId = "" + id;
			dcm.misId = misId;
			saveDCMProps(dcm);

			return "http://localhost:8080/pdf/" + misId + File.separator
					+ filename;
		} catch (IOException e) {
			e.printStackTrace();
			throw new WsException(e);
		}

	}

	/**
	 * @param dcm
	 * @throws IOException
	 */
	private void saveDCMProps(DCM dcm) throws IOException {

		Properties prop = new Properties() {

			@Override
			public Set<Object> keySet() {
				return Collections.unmodifiableSet(new TreeSet<Object>(super
						.keySet()));
			}

		};

		// tmp.putAll(drnProp);

		Properties drnProp = loadDrnFromPropFile(dcm.misId);

		prop.put("misId", dcm.misId);
		prop.put("dcmId", dcm.dcmId);
		prop.put("StudyId", dcm.misId);

		prop.put("StudyDate", df.format(new Date()));

		prop.put("modality", drnProp.get("modality"));
		prop.put("deviceName", "ANYDEVICE");
		prop.put("patientName", drnProp.get("patientName"));
		prop.put("physicianName", drnProp.get("physicianName"));

		FileOutputStream fos = new FileOutputStream(testDrnDataDir
				+ File.separator + dcm.misId + File.separator + dcm.dcmId
				+ File.separator + testDCMDatafile);
		prop.storeToXML(fos, "Webdicom dcm properties file", "UTF-8");
		fos.close();

	}

	public String sendImage(String misId, byte[] content) throws WsException {

		long id = new Date().getTime();
		File theDir = new File(testDrnDataDir + File.separator + misId
				+ File.separator + id);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}
		try {
			// Properties drnProp = loadDrnFromPropFile(barCode);

			String filename = "data.jpg";
			FileOutputStream fos = new FileOutputStream(testDrnDataDir
					+ File.separator + misId + File.separator + id
					+ File.separator + filename);
			fos.write(content);
			fos.flush();
			fos.close();

			DCM dcm = new DCM();
			dcm.dcmId = "" + id;
			dcm.misId = misId;
			saveDCMProps(dcm);

			return "http://localhost:8080/img/" + misId + File.separator
					+ filename;
		} catch (IOException e) {
			e.printStackTrace();
			throw new WsException(e);
		}

	}

	/**
	 * Передача окончательного результата
	 * 
	 * @param misId
	 * @param resultStr
	 */
	public String sendFinalResult(String misId, String resultStr) {

		try {
			Properties drnProp = loadDrnFromPropFile(misId);
			drnProp.put("finalResult", resultStr);
			saveDrnToPropFile(drnProp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Что лучше возвращать?
		return misId + "=" + resultStr;
	}

	/**
	 * Передача ФИО врача узкого специалиста
	 * 
	 * @param misId
	 * @param resultStr
	 */
	public String sendPhysician(String misId, String fio) {

		try {
			Properties drnProp = loadDrnFromPropFile(misId);
			drnProp.put("physicianName", fio);
			saveDrnToPropFile(drnProp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Что лучше возвращать?
		return misId + "=" + fio;
	}

	/**
	 * TODO Не используется
	 * 
	 * @return
	 */
	public RISCode[] getRISCodes() {

		ArrayList<RISCode> result = new ArrayList<RISCode>();

		RISCode code = new RISCode();
		code.modality = "MG";
		code.risCode = "MG00.1";
		code.description = "Исследование молочной железы 1";

		result.add(code);

		code = new RISCode();
		code.modality = "DF";
		code.risCode = "DF00.1";
		code.description = "Рентген 1";

		result.add(code);

		return result.toArray(new RISCode[result.size()]);

	}
}
