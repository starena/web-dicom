package org.psystems.dicom.browser.server;

import junit.framework.TestCase;
import java.security.*;


public class NewStudyServletTest extends TestCase {

	public void testDoMD5() {
		
		

		String sessionid="12345";
		        
		byte[] defaultBytes = sessionid.getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
		            
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			String foo = messageDigest.toString();
			System.out.println("sessionid "+sessionid+" md5 version is "+hexString.toString());
			sessionid=hexString+"";
		}catch(NoSuchAlgorithmException nsae){
		            
		}

	}

}
