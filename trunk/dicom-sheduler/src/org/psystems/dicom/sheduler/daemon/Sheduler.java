package org.psystems.dicom.sheduler.daemon;

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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
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

public class Sheduler {

	private int frame = 1;
	private float center;
	private float width;
	private String vlutFct;
	private boolean autoWindowing;
	private DicomObject prState;
	private short[] pval2gray;
	private String fileExt = ".jpg";

	private  final String USAGE = "sheduler [Options] SOURCE DEST\n";

	private  final String DESCRIPTION = "Convert DICOM file SOURCE to DEST and "
			+ " verify data in URL dababase.\nOptions:";

	private  final String EXAMPLE = "\nExample: dcm2dcm in.dcm out.dcm\n"
			+ " => Decode DICOM object from DICOM file in.dcm and encode it with"
			+ " Implicit VR Little Endian Transfer Syntax to DICOM file out.dcm.";
	
	private String VERSION = "0.1a";

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

		new Sheduler();
	}
	
	/**
	 * Разборщик коммандной строки
	 * @param args
	 * @return
	 */
	private CommandLine parse(String[] args) {
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options opts = new Options();
		
		opts.addOption( OptionBuilder.withLongOpt( "source-dir" )
                .withDescription( "use PATH source dir" )
                .hasArg()
                .withArgName("PATH")
                .create("s") );
		
		opts.addOption( OptionBuilder.withLongOpt( "dest-dir" )
                .withDescription( "use PATH destination dir" )
                .hasArg()
                .withArgName("PATH")
                .create("d") );
		
		opts.addOption( OptionBuilder.withLongOpt( "connection" )
                .withDescription( "use URL for JDBC connector" )
                .hasArg()
                .withArgName("URL")
                .create("c") );
		
		opts.addOption("h", "help", false, "print this message");
        opts.addOption("V", "version", false,
                "print the version information and exit");
		
		
		CommandLine cl = null;
        try {
            cl = new PosixParser().parse(opts, args);
        } catch (ParseException e) {
            exit("dcm2dcm: " + e.getMessage());
            throw new RuntimeException("unreachable");
        }
        if (cl.hasOption('V')) {
            System.out.println("dcm2dcm version " + VERSION); //TODO Взять нормально версию
            System.exit(0);
        }
        if (cl.hasOption('h') || cl.getArgList().size() < 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
            System.exit(0);
        }
        return cl;
	}
	
	 private void exit(String msg) {
	        System.err.println(msg);
	        System.err.println("Try 'sheduler -h' for more information.");
	        System.exit(1);
	    }



	public Sheduler() {


		String[] args = new String[] { "-d dst/vvv/ddd" , "--source-dir=src/bbb/cccc" };
//		String[] args = new String[] { "--src-dir aaa/bbb/ss"};
		
//		HelpFormatter formatter = new HelpFormatter();
//        formatter.printHelp(USAGE, DESCRIPTION, options, EXAMPLE);


		CommandLine cl = parse(args);

			// validate that block-size has been set
			if (cl.hasOption("block-size")) {
				// print the value of block-size
				System.out.println(cl.getOptionValue("block-size"));
			}
			
			if (cl.hasOption("source-dir")) {
				// print the value of block-size
				System.out.println(cl.getOptionValue("source-dir"));
			}
			
		

		if (true)
			return;//FIXME Убрать !!!

		DicomObject dcmObj;
		DicomInputStream din = null;
		try {

			// din = new DicomInputStream(new File("demo/Im00001.dcm"));
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
			System.out.print("converting image...");
			// Конвертация картинок

			// File src = new File("demo/Im00001.dcm");
			// File dest = new File("demo/Im00001.jpg");
			File src = new File("demo/6185.bin");
			File dest = new File("demo/6185.jpg");
			convert(src, dest);
			System.out.println("success!");
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
