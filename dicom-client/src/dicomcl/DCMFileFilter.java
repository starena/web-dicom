package dicomcl;

import java.io.File;
import java.io.FileFilter;

public class DCMFileFilter implements FileFilter
{
	@Override
	public boolean accept (File arg0) 
	{
		if (arg0.isFile() && (arg0.getName().endsWith(".dcm") || arg0.getName().endsWith(".DCM"))) return true;
		return false;
	}

}
