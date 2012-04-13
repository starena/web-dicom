package dicomcl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;

public class DicomFile 
{
	private File dcmFile;
	private String PatientName;
	private String PatientBirthDate;
	private String PatientSex;
	private String StudyDescription;
	private String Modality;
	private String StudyDate;
	private BufferedImage image;
	private DicomObject dicomObject;
	
	public boolean loadFileError = false;
	public boolean loadImageError = false;
	
	public String getFileName()
	{
		return dcmFile.getName();
	}
	
	public String getPath()
	{
		return dcmFile.getAbsolutePath();
	}
	
	public DicomFile (BasicDicomObject basicDicomObject)
	{
		if (basicDicomObject != null)
		{
			PatientName = basicDicomObject.getString (Tag.PatientName);
			if (PatientName == null || PatientName.length()==0) PatientName = "[Unknown]";
			PatientBirthDate = basicDicomObject.getString (Tag.PatientBirthDate);
			if (PatientBirthDate == null || PatientBirthDate.length()==0) PatientBirthDate = "[Unknown]";
			PatientSex = basicDicomObject.getString (Tag.PatientSex);
			if (PatientSex == null || PatientSex.length()==0) PatientSex = "[Unknown]";
			StudyDescription = basicDicomObject.getString (Tag.StudyDescription);
			if (StudyDescription == null || StudyDescription.length()==0) StudyDescription = "[Unknown]";
			Modality = basicDicomObject.getString (Tag.Modality);
			if (Modality == null || Modality.length()==0) Modality = "[Unknown]";
			StudyDate = basicDicomObject.getString (Tag.StudyDate);
			if (StudyDate == null || StudyDate.length()==0) StudyDate = "[Unknown]";
		}
		else loadFileError = true;
	}
	
	public DicomFile (File dcmFile)
	{
		dicomObject = null;
		this.dcmFile = dcmFile;
		try 
		{
			FileInputStream fis = new FileInputStream (dcmFile);
			DicomInputStream dis = new DicomInputStream (fis);
			dicomObject = dis.readDicomObject();
			dis.close();
			fis.close();
		}
		catch (FileNotFoundException e) { loadFileError = true; } 
		catch (IOException e) { loadFileError = true; }
		catch (NullPointerException e) { loadFileError = true; }
		
		if (dicomObject != null)
		{
			PatientName = dicomObject.getString (Tag.PatientName);
			if (PatientName == null || PatientName.length()==0) PatientName = "[Unknown]";
			PatientBirthDate = dicomObject.getString (Tag.PatientBirthDate);
			if (PatientBirthDate == null || PatientBirthDate.length()==0) PatientBirthDate = "[Unknown]";
			PatientSex = dicomObject.getString (Tag.PatientSex);
			if (PatientSex == null || PatientSex.length()==0) PatientSex = "[Unknown]";
			StudyDescription = dicomObject.getString (Tag.StudyDescription);
			if (StudyDescription == null || StudyDescription.length()==0) StudyDescription = "[Unknown]";
			Modality = dicomObject.getString (Tag.Modality);
			if (Modality == null || Modality.length()==0) Modality = "[Unknown]";
			StudyDate = dicomObject.getString (Tag.StudyDate);
			if (StudyDate == null || StudyDate.length()==0) StudyDate = "[Unknown]";
		}
	}
	
	public DicomObject getDicomObject()
	{
		return dicomObject;
	}
	
	public void loadImage()
	{
		image = null;
		try 
		{  
			Iterator <ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM"); 
			ImageReader reader = (ImageReader) iter.next();
			DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
			ImageInputStream iis = ImageIO.createImageInputStream (dcmFile);  
			reader.setInput (iis,false);     
			image = reader.read (0,param);     
			iis.close();
		}
		catch (NoClassDefFoundError e)
		{
			loadImageError = true;
		}
		catch (IOException e) 
		{
			loadImageError = true;
		}
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public String getShortInfo()
	{
		if (!loadFileError)
			return "Patient name: "+PatientName
				+"\nPatient birth date: "+PatientBirthDate
				+"\nPatient sex: "+PatientSex
				+"\nStudy description: "+StudyDescription
				+"\nModality: "+Modality
				+"\nStudy date: "+StudyDate;
		else return "Loading file information failed.";
	}
}
