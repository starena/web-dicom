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
 * See listed authors below.
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

package org.psystems.dicom.archive;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Jul 11, 2007
 */
public class Dcm2Jpg {

    private static final String USAGE =
        "dcm2jpg [Options] <dcmfile> <jpegfile>\n" +
        "or dcm2jpg [Options] <dcmfile>... <outdir>\n" +
        "or dcm2jpg [Options] <indir>... <outdir>";
    private static final String DESCRIPTION = 
        "Convert DICOM image(s) to JPEG(s)\nOptions:";
    private static final String EXAMPLE = null;
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

    public void convert(File src, File dest) throws IOException {
        Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
        ImageReader reader = iter.next();
        DicomImageReadParam param = 
            (DicomImageReadParam) reader.getDefaultReadParam();
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
            if(reader.getNumImages(false)<=0) {
            	System.out.println("\nError: " + src + " - Don't haven any images!");
                return;
            }
//            System.out.println("!!! frame="+frame+" p="+reader.getNumImages(false));
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

    public int mconvert(List<String> args, int optind, File destDir)
            throws IOException {
        int count = 0;
        for (int i = optind, n = args.size() - 1; i < n; ++i) {
            File src = new File(args.get(i));
            count += mconvert(src, new File(destDir, src2dest(src)));
        }
        return count;
    }

    private String src2dest(File src) {
        String srcname = src.getName();
        return src.isFile() ? srcname + this.fileExt : srcname;
    }

    public int mconvert(File src, File dest) throws IOException {
        if (!src.exists()) {
            System.err.println("WARNING: No such file or directory: " + src
                    + " - skipped.");                
            return 0;
        }
        if (src.isFile()) {
            try {
                convert(src, dest);
            } catch (Exception e) {
                System.err.println("WARNING: Failed to convert " + src + ":");
                e.printStackTrace(System.err);
                System.out.print('F');
                return 0;
            }
            System.out.print('.');
            return 1;
        }
        File[] files = src.listFiles();
        if (files.length > 0 && !dest.exists()) {
            dest.mkdirs();
        }
        int count = 0;
        for (int i = 0; i < files.length; ++i) {
            count += mconvert(files[i], new File(dest, src2dest(files[i])));
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    public static void main(String args[]) throws Exception {
        CommandLine cl = parse(args);
        Dcm2Jpg dcm2jpg = new Dcm2Jpg();
        if (cl.hasOption("f")) {
            dcm2jpg.setFrameNumber(
                    parseInt(cl.getOptionValue("f"),
                            "illegal argument of option -f",
                            1, Integer.MAX_VALUE));
        }
        if (cl.hasOption("p")) {
            dcm2jpg.setPresentationState(loadDicomObject(
                    new File(cl.getOptionValue("p"))));
        }
        if (cl.hasOption("pv2gray")) {
            dcm2jpg.setPValue2Gray(loadPVal2Gray(
                    new File(cl.getOptionValue("pv2gray"))));
        }        
        if (cl.hasOption("c")) {
            dcm2jpg.setWindowCenter(
                    parseFloat(cl.getOptionValue("c"),
                            "illegal argument of option -c"));
        }
        if (cl.hasOption("w")) {
            dcm2jpg.setWindowWidth(
                    parseFloat(cl.getOptionValue("w"),
                            "illegal argument of option -w"));
        }
        if (cl.hasOption("sigmoid")) {
            dcm2jpg.setVoiLutFunction(DicomImageReadParam.SIGMOID);
        }
        dcm2jpg.setAutoWindowing(!cl.hasOption("noauto"));
        if (cl.hasOption("jpgext")) {
            dcm2jpg.setFileExt(cl.getOptionValue("jpgext"));
        }
        final List<String> argList = cl.getArgList();
        int argc = argList.size();

        File dest = new File(argList.get(argc-1));
        long t1 = System.currentTimeMillis();
        int count = 1;
        if (dest.isDirectory()) {
            count = dcm2jpg.mconvert(argList, 0, dest);
        } else {
            File src = new File(argList.get(0));
            if (argc > 2 || src.isDirectory()) {
                exit("dcm2jpg: when converting several files, "
                        + "last argument must be a directory\n");
            }
            dcm2jpg.convert(src, dest);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("\nconverted " + count + " files in " + (t2 - t1)
                / 1000f + " s.");
    }

    private static DicomObject loadDicomObject(File file) {
        DicomInputStream in = null;
        try {
            in = new DicomInputStream(file);
            return in.readDicomObject();
        } catch (IOException e) {
            exit(e.getMessage());
            throw new RuntimeException();
        } finally {
            CloseUtils.safeClose(in);
        }
    }

    private static short[] loadPVal2Gray(File file) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(
                    file)));
            short[] pval2gray = new short[256];
            int n = 0;
            String line;
            while ((line = r.readLine()) != null) {
                try {
                    int val = Integer.parseInt(line.trim());
                    if (n == pval2gray.length) {
                        if (n == 0x10000) {
                            exit("Number of entries in " + file + " > 2^16");
                        }
                        short[] tmp = pval2gray;
                        pval2gray = new short[n << 1];
                        System.arraycopy(tmp, 0, pval2gray, 0, n);
                    }
                    pval2gray[n++] = (short) val;
                } catch (NumberFormatException nfe) {
                    // ignore lines where Integer.parseInt fails
                }
            }
            if (n != pval2gray.length) {
                exit("Number of entries in " + file + ": " + n
                        + " != 2^[8..16]");
            }
            return pval2gray;
        } catch (IOException e) {
            exit(e.getMessage());
            throw new RuntimeException();
        } finally {
            CloseUtils.safeClose(r);
        }
    }

    private static CommandLine parse(String[] args) {
        Options opts = new Options();
        OptionBuilder.withArgName("frame");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "frame to convert, 1 (= first frame) by default");
        opts.addOption(OptionBuilder.create("f"));
        OptionBuilder.withArgName("prfile");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "file path of presentation state to apply");
        opts.addOption(OptionBuilder.create("p"));
        OptionBuilder.withArgName("center");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Window Center");
        opts.addOption(OptionBuilder.create("c"));
        OptionBuilder.withArgName("width");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Window Width");
        opts.addOption(OptionBuilder.create("w"));
        opts.addOption("sigmoid", false,
                "apply sigmoid VOI LUT function with given Window Center/Width");
        opts.addOption("noauto", false,
                "disable auto-windowing for images w/o VOI attributes");
        OptionBuilder.withArgName("file");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "file path of P-Value to gray value map");
        opts.addOption(OptionBuilder.create("pv2gray"));
        OptionBuilder.withArgName(".xxx");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "jpeg file extension used with destination directory argument,"
                + " default: '.jpg'.");
        opts.addOption(OptionBuilder.create("jpgext"));
        opts.addOption("h", "help", false, "print this message");
        opts.addOption("V", "version", false,
                "print the version information and exit");
        CommandLine cl = null;
        try {
            cl = new GnuParser().parse(opts, args);
        } catch (ParseException e) {
            exit("dcm2jpg: " + e.getMessage());
            throw new RuntimeException("unreachable");
        }
        if (cl.hasOption('V')) {
            Package p = Dcm2Jpg.class.getPackage();
            System.out.println("dcm2jpg v" + p.getImplementationVersion());
            System.exit(0);
        }
        if (cl.hasOption('h') || cl.getArgList().size() < 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
            System.exit(0);
        }
        return cl;
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

    private static float parseFloat(String s, String errPrompt) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            exit(errPrompt);
            throw new RuntimeException();
        }
    }
       
    private static void exit(String msg) {
        System.err.println(msg);
        System.err.println("Try 'dcm2jpg -h' for more information.");
        System.exit(1);
    }

}
