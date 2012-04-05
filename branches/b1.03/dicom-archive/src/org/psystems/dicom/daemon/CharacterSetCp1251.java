package org.psystems.dicom.daemon;

import java.io.UnsupportedEncodingException;

import org.dcm4che2.data.ConfigurationError;
import org.dcm4che2.data.SpecificCharacterSet;

public class CharacterSetCp1251 extends SpecificCharacterSet {

	private static String codepage = "Cp1251";

	public CharacterSetCp1251() {
		super(codepage);
	}
	
//	public CharacterSetCp1251(String charset) {
//		super(charset);
//	}

	@Override
	public String decode(byte[] val) {
		String s = decode(val, 0, val.length, codepage);
//		System.out.println("decode "+s);
		return s; 
	}

	static String decode(byte[] b, int off, int len, String cs) {
		try {
			return new String(b, off, len, cs);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationError(e);
		}
	}

	@Override
	public byte[] encode(String val) {
		// TODO Auto-generated method stub
		// return super.encode(val);
		try {
			return val.getBytes(codepage);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationError(e);
		}
	}

}
