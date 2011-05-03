package org.psystems.dicom.solr;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;
import org.dcm4che2.util.StringUtils;
import org.xml.sax.SAXException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 *
 *
 **/
public class Driver {

	private static Logger logger = Logger.getLogger(Driver.class.getName());

	public static void main(String[] args) throws IOException, SAXException {

		new Driver();
	}

	public void test() throws IOException {
		int maxDocs = 20;
		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");

		List<SolrInputDocument> docBuffer = new ArrayList<SolrInputDocument>(
				maxDocs);

		try {
			for (int i = 0; i < maxDocs; i++) {

				/*
				 * SolrInputDocument currentDicomStudy = new
				 * SolrInputDocument(); currentDicomStudy.addField("id", "key" +
				 * i); currentDicomStudy.addField("studyId", "study st" + i);
				 * currentDicomStudy.addField("patientName", " VASIA FIO" + i);
				 * currentDicomStudy.addField("studyDescription",
				 * "Description of study FIO" + i); //
				 * docBuffer.add(currentDicomStudy);
				 * server.add(currentDicomStudy);
				 */

				Study study = new Study();
				study.id = "key" + i;
				study.patientName = "FIO" + i;
				study.study_Id = "STUDYID" + i;
				study.studyDescription = "Description of study FIO" + i;
				study.diagnozis = new String[] { "aaa", "bbb", "ccc" };
				ArrayList<String> services = new ArrayList<String>();
				services.add("service1");
				services.add("service2");
				services.add("service3");
				study.services = services;
				server.addBean(study);
				server.commit();

			}

			// server.add(docBuffer);
			// int numIndexed = docBuffer.size();
			// docBuffer.clear();

			server.optimize();

		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Driver() {
		super();
		
//		try {
//			test();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		try {
			
			ArrayList<File> files = getAllDcmFiles((new File(
			"C:/WORK/workspace/dicom-archive/database/instance/dcm.data")));
			
//			ArrayList<File> files = getAllPDFFiles(new File(
//					"C:/temp/pdf"));
			
			SolrServer server = new CommonsHttpSolrServer(
			"http://localhost:8983/solr");
			//�������� ����� � �������� �������
			((CommonsHttpSolrServer) server).setRequestWriter(new BinaryRequestWriter());
			
			for (File file : files) {
				System.out.println("file=" + file.getName());
//				indexFilesSolrCell(server,file,file.getName());
//				peristPdfTags(server,file);
				perisiTags(server,file);
			}
			
			server.optimize();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  /**
	   * Method to index all types of files into Solr. 
	   * @param fileName
	   * @param solrId
	   * @throws IOException
	   * @throws SolrServerException
	   */
	  public static void indexFilesSolrCell(SolrServer solr, File file, String solrId) 
	    throws IOException, SolrServerException {
	    
//	    String urlString = "http://localhost:8983/solr"; 
//	    SolrServer solr = new CommonsHttpSolrServer(urlString);
	    
	    ContentStreamUpdateRequest up 
	      = new ContentStreamUpdateRequest("/update/extract");
	    
	    up.addFile(file);
	    
	    up.setParam("literal.id", solrId);
	    up.setParam("uprefix", "attr_");
	    up.setParam("fmap.content", "attr_content");
	    
	    up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
	    
	    solr.request(up);
	    
	    QueryResponse rsp = solr.query(new SolrQuery("*:*"));
	    
	    System.out.println(rsp);
	  }

//	private void peristPdfTags(SolrServer server, File file) {
//		// TODO Auto-generated method stub
//		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
//		up.addFile(file);
//		up.setParam("literal.id", "mailing_lists.pdf");
//		up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
//		result = server.request(up);
//		assertNotNull("Couldn't upload mailing_lists.pdf", result);
//		rsp = server.query( new SolrQuery( "*:*") );
//		Assert.assertEquals( 1, rsp.getResults().getNumFound() );
//
//	}

	public void makeIndex() {

	}
	
	/**
	 * �������� �����
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	private ArrayList<File> getAllPDFFiles(File dir) throws IOException {
		ArrayList<File> files = new ArrayList<File>();
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				if (file.isDirectory()) {
					files.addAll(getAllDcmFiles(file));
				} else {
					if (!file.getName().endsWith(".pdf"))
						continue;
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 * �������� �����
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	private ArrayList<File> getAllDcmFiles(File dir) throws IOException {
		ArrayList<File> files = new ArrayList<File>();
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				if (file.isDirectory()) {
					files.addAll(getAllDcmFiles(file));
				} else {
					if (!file.getName().endsWith(".dcm"))
						continue;
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 * ���������� �����
	 * 
	 * @param dcmFile
	 */
	private void extractTags(File dcmFile) {

	}

	private void perisiTags(SolrServer server, File dcmFile) throws IOException, SolrServerException {
		
		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = new Win1251CharacterSet();
		// SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");

		try {
			long fileSize = dcmFile.length();
			din = new DicomInputStream(dcmFile);
			dcmObj = din.readDicomObject();

			// ������ ��������� �� dcm-�����
			if (dcmObj.get(Tag.SpecificCharacterSet) != null
					&& dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
				cs = SpecificCharacterSet.valueOf(dcmObj.get(
						Tag.SpecificCharacterSet).getStrings(null, false));
			}
			
			Study study = new Study();
			DicomElement tag = null;
			
			tag = dcmObj.get(Tag.SOPInstanceUID);
			if(tag!=null)
			study.setId(tag.getValueAsString(cs, tag.length()));
			
			tag = dcmObj.get(Tag.PatientName);
			if(tag!=null) {
				study.setPatientName(tag.getValueAsString(cs, tag.length()));
				study.setText(tag.getValueAsString(cs, tag.length()));
			}
			
			tag = dcmObj.get(Tag.StudyID);
			if(tag!=null)
			study.setStudy_Id(tag.getValueAsString(cs, tag.length()));

			HashMap<String, String> tags = new HashMap<String, String>();
			
			// ������������ ����
			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				// ���������� �������� ����
				if (element.vr().equals(VR.OW) || element.vr().equals(VR.OB)
						|| element.vr().equals(VR.SQ)) {
					continue;
				}

				int tagId = element.tag();

				StringBuffer sb = new StringBuffer();
				StringUtils.shortToHex(tagId >> 16, sb);
				String major = sb.toString();

				sb = new StringBuffer();
				StringUtils.shortToHex(tagId, sb);
				String minor = sb.toString();
				
				String type = element.vr().toString();

				int length = element.length();
				int maxLength = 200;// TODO ������ ������� �����������.
				if (length > maxLength)
					length = maxLength;

				String value = "Not be converted";
				if (!element.vr().equals(VR.SQ)) {
					value = element.getValueAsString(cs, length);
				}

//				System.out.println(type + "[" + dcmObj.nameOf(tagId) + "]("
//						+ major + ";" + minor + ") " + value);
				
				String tagKey = major + "" + minor;
				tags.put(tagKey, value);
				
			}
			
			study.setTags(tags);
			
			//���������� ��������
			study.setImagefull(new String("123456789!!������!!").getBytes());
			extractImages(dcmFile,study);
			
			server.addBean(study);
			server.commit();

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				logger.fatal("Unsupported character set" + cs + " " + e);
			}
			logger.fatal("" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("" + e);
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	/**
	 * ��������� � ������ ��������
	 * @param dcmFile
	 * @param study
	 * @throws IOException
	 */
	public void extractImages(File dcmFile, Study study) throws IOException {

		int frame = 1;
		float center = 0;
		String vlutFct = null;
		boolean autoWindowing = false;
		DicomObject prState = null;
		short[] pval2gray = null;

		Iterator<ImageReader> iter = ImageIO
				.getImageReadersByFormatName("DICOM");

		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader
				.getDefaultReadParam();
		param.setWindowCenter(center);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(dcmFile);
		BufferedImage bi = null;
		OutputStream out = null;
		ByteArrayOutputStream baos = null;
		try {
			reader.setInput(iis, false);
			if (reader.getNumImages(false) <= 0) {
				// �������� ����...
				System.out.println("\nError: " + dcmFile
						+ " - Don't have any images!");
				return;
			}
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				// �������� �� ��������
				System.out.println("\nError: " + dcmFile + " - couldn't read!");
				return;
			}

			baos = new ByteArrayOutputStream();

			out = new BufferedOutputStream(baos);
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);

			// ��������� ��������
			study.setImagefull(baos.toByteArray());

			// ������ ������ ����� ��������
			study.setImage100x100(getResizedImage(bi, 100, 100));

		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);
			CloseUtils.safeClose(baos);
		}
		
		
	}

	/**
	 * ��������� ����������� ����� ��������
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	private byte[] getResizedImage(BufferedImage image, int width, int height)
			throws IOException {

		// BufferedImage resizedImage = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_ARGB);
		BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, "jpg", baos);
		return baos.toByteArray();
	}
}
