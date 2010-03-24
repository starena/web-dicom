package com.asutp.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class SXMTokenizerDemo extends Tokenizer {

	private static final int IO_BUFFER_SIZE = 4096;

	private TermAttribute termAtt;
	private LinkedList<String> keyWords = new LinkedList<String>();

	public SXMTokenizerDemo(Reader input) {
		super(input);
		termAtt = addAttribute(TermAttribute.class);
		try {
			prepareModel(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void prepareModel(Reader input) throws IOException {

		// System.out.println(""+input);
		StringBuffer sb = new StringBuffer();
		while (true) {
			char[] ioBuffer = new char[IO_BUFFER_SIZE];
			int dataLen = input.read(ioBuffer);

			if (dataLen < 0)
				break;
			if (dataLen < IO_BUFFER_SIZE) {
				ioBuffer = Arrays.copyOf(ioBuffer, dataLen);
			}
			sb.append(ioBuffer);
		}
		// System.out.println("RESULT: " + sb.toString());
		tokenizing(sb);
	}

	private void tokenizing(StringBuffer sb) {

		keyWords.clear();
		
		String s = sb.toString();
		// System.out.println(""+s);

		Pattern pFile = Pattern.compile("[\\^\\^]+");
		String[] resultFile = pFile.split(s);
		for (int i = 0; i < resultFile.length; i++) {

			String line = resultFile[i];
			// System.out.println(">>>"+line);

			Pattern pLine = Pattern.compile("[\\&\\&]+");
			String[] resultLine = pLine.split(line);
			for (int j = 0; j < resultLine.length; j++) {
				String token = resultLine[j];
				// System.out.println(token);

				Matcher matcher = Pattern.compile("(.*)==(.*)").matcher(token);
				if (matcher.matches()) {
					
					String key = matcher.group(1);
					String val = matcher.group(2);
					if(key.equals("DESCR") || key.equals("PARAM")) {
					System.out.println(key + " = [" + val + "]");
					keyWords.add(val);
					}

				}
			}

		}

	}

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		if(keyWords.size() > 0) {
			String word = keyWords.removeFirst();
			termAtt.setTermBuffer(word);
			termAtt.setTermLength(word.length());
			return true;
		}
		return false;
	}
	
	@Override
	public final void end() {
		System.out.println(">>>>>>>>> END ");
	}

	@Override
	public void reset(Reader input) throws IOException {
		System.out.println(">>>>>>>>> RESET "+input);
	}


}
