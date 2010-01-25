package org.psystems.dicom.sheduler.server;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomObjectToStringParam;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class DCMUtil {

	private static int frame = 1;
	private static float center;
	private static float width;
	private static String vlutFct;
	private static boolean autoWindowing;
	private static DicomObject prState;
	private static short[] pval2gray;
	private String fileExt = ".jpg";

	private void setFrameNumber(int frame) {
		this.frame = frame;
	}

	private void setWindowCenter(float center) {
		this.center = center;
	}

	private void setWindowWidth(float width) {
		this.width = width;
	}

	public final void setVoiLutFunction(String vlutFct) {
		this.vlutFct = vlutFct;
	}

	private final void setAutoWindowing(boolean autoWindowing) {
		this.autoWindowing = autoWindowing;
	}

	private final void setPresentationState(DicomObject prState) {
		this.prState = prState;
	}

	private final void setPValue2Gray(short[] pval2gray) {
		this.pval2gray = pval2gray;
	}

	public final void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public static void printTags(File dcmFile) throws IOException {
		DicomObject dcmObj;
		DicomInputStream din = null;
		try {
			din = new DicomInputStream(dcmFile);
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			// DicomObjectToStringParam param =
			// DicomObjectToStringParam.getDefaultParam();

			SpecificCharacterSet cs = null;

			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();

				short ma = (short) (tag >> 16);
				String major = Integer.toHexString(ma);

				short mi = (short) (tag);
				String minor = Integer.toHexString(mi);

				// Object major = StringUtils.shortToHex(tag >> 16, sb);
				// sb.append(',');
				// Object minor = StringUtils.shortToHex(tag, sb);

				// System.out.println("ELT="+tag+" - "+dcmObj.nameOf(tag)+" - "+element.getValueAsString(cs,
				// 100));

				if (tag == Tag.SpecificCharacterSet) {
					cs = SpecificCharacterSet.valueOf(element.getStrings(null, false));
					System.out.println("CharSet=" + element.getValueAsString(cs, 100));
					// String charset = element.getValueAsString(cs,
					// element.length());
					// cs = new SpecificCharacterSet("ISO-8859-5");
				}

				if (tag == 1048640) {
					System.out.println("Sex=" + element.getValueAsString(cs, element.length()));
				}

				if (tag == 1048624) {
					System.out.println("DateBirth=" + element.getValueAsString(cs, element.length()) + " == "
							+ element.getDate(false));
					System.out.println("DicomElement (" + major + "," + minor + ") {" + tag + "}  " + " ["
							+ dcmObj.nameOf(tag) + "]  = " + element.getValueAsString(cs, element.length())
							+ " == " + element.getDate(false));

				}

				if (tag == 524416 || tag == 524417 || tag == 1048592 || tag == 1048608) {

					System.out.println("DicomElement (" + major + "," + minor + ") {" + tag + "}  " + " ["
							+ dcmObj.nameOf(tag) + "]  = " + element.getValueAsString(cs, element.length()));

				}

				if (tag == 2145386512) {

					System.out.println("length=" + element.length());
					System.out.println("DicomElement (" + major + "," + minor + ") {" + tag + "}  " + " ["
							+ dcmObj.nameOf(tag) + "]  = " + element.getValueAsString(cs, 100));
					// https://jai-imageio.dev.java.net/binary-builds.html

					// byte[] rlePixelData = element.getFragment(1);
					// System.out.println("DicomElement IMAGE" +
					// rlePixelData.length);

				}

			}
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}

		

		// try {
		// System.out.print("converting image..." );
		// File src = new File("demo/Im00001.dcm");
		// File dest = new File("demo/Im00001.jpg");
		// convert(src,dest);
		// System.out.println("success!" );
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * @param rootDir
	 * @param dcmFile
	 * @return
	 * @throws IOException
	 */
	public static DicomObjectWrapper getDCMObject(File rootDir, File dcmFile) throws IOException {
		DicomObject dcmObj;
		DicomInputStream din = null;
		try {
		din = new DicomInputStream(dcmFile);
		dcmObj = din.readDicomObject();
		
		SpecificCharacterSet cs = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet).getStrings(null, false));
		Date birthDate = dcmObj.get(Tag.PatientBirthDate).getDate(false);
		Date studyDate = dcmObj.get(Tag.StudyDate).getDate(false);
		DicomElement element = dcmObj.get(Tag.PatientName); 
		String patName = element.getValueAsString(cs, element.length());
		
		
		
		DicomObjectWrapper proxy = new DicomObjectWrapper();
		proxy.setDCM_FILE_NAME(dcmFile.getParentFile().getName() +  File.separator + dcmFile.getName());//FIXME сделать относительный путь
		proxy.setPATIENT_NAME(patName);
		proxy.setPATIENT_BIRTH_DATE(new java.sql.Date(birthDate.getTime()));
		proxy.setSTUDY_DATE(new java.sql.Date(birthDate.getTime()));
		
		return proxy;
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}
	}

	/**
	 * Конвертация DCM-файла в JPEG
	 * 
	 * TODO убрать статику из глобальных переменных.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void convert(File src, File dest) throws IOException {
		Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
		param.setWindowCenter(center);
		param.setWindowWidth(width);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(src);
		BufferedImage bi;
		OutputStream out = null;
		try {
			reader.setInput(iis, false);
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				System.out.println("\nError: " + src + " - couldn't read!");
				return;
			}
			out = new BufferedOutputStream(new FileOutputStream(dest));
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);
		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);
		}
		System.out.print('.');
	}

}
