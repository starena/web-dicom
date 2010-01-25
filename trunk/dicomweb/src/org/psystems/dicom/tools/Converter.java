package org.psystems.dicom.tools;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

public class Converter {

	private int frame = 1;
	private float center;
	private float width;
	private String vlutFct;
	private boolean autoWindowing;
	private DicomObject prState;
	private short[] pval2gray;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Converter();
	}

	public Converter() {
		DicomObject dcmObj;
		DicomInputStream din = null;
		try {
			 
//			din = new DicomInputStream(new File("demo/Im00001.dcm"));
			din = new DicomInputStream(new File("demo/6185.bin"));
			dcmObj = din.readDicomObject();
			System.out.println("dcmObj=" + dcmObj);

			DicomObjectToStringParam param = DicomObjectToStringParam
					.getDefaultParam();

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

				if (tag == 524293 && tag == Tag.SpecificCharacterSet) {
					cs = SpecificCharacterSet.valueOf(element.getStrings(null,
							false));
					// String charset = element.getValueAsString(cs,
					// element.length());
					// cs = new SpecificCharacterSet("ISO-8859-5");
					
					System.out.println("DicomElement (" + major + "," + minor
							+ ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
							+ "]  = "
							+ element.getValueAsString(cs, element.length()));
				}

				if (tag == 524416 || tag == 524417 || tag == 1048592
						|| tag == 1048608) {

					System.out.println("DicomElement (" + major + "," + minor
							+ ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
							+ "]  = "
							+ element.getValueAsString(cs, element.length()));

				}

				if (tag == 2145386512) {

					System.out.println("length=" + element.length());
					System.out.println("DicomElement (" + major + "," + minor
							+ ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
							+ "]  = " + element.getValueAsString(cs, 100));
					// https://jai-imageio.dev.java.net/binary-builds.html

					// byte[] rlePixelData = element.getFragment(1);
					// System.out.println("DicomElement IMAGE" +
					// rlePixelData.length);

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}



		 try {
		 System.out.print("converting image..." );
		// Конвертация картинок
		 
//		 File src = new File("demo/Im00001.dcm");
//		 File dest = new File("demo/Im00001.jpg");
		 File src = new File("demo/6185.bin");
		 File dest = new File("demo/6185.jpg");
		 convert(src,dest);
		 System.out.println("success!" );
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }

	}

	public void convert(File src, File dest) throws IOException {
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
