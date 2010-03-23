package com.asutp.index;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class SXMTokenizer extends Tokenizer {

	private int offset = 0, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

	private TermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private char previousToken;// Предыдущий токен
	private char delimiterPars = '=';
	private char delimiterTokens = '&';
	private char delimiterEndLine = '^';

	public SXMTokenizer(Reader input) {
		super(input);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(TermAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();

		int length = 0;
		int start = bufferIndex;
		char[] buffer = termAtt.termBuffer();

		// char c = (char) input.read();
		// System.out.println("token!!!="+c);
		// String token = new String(buffer,"Cp1251");
		// System.out.println("token["+buffer.length+"]"+token);

		// return false;

		while (true) {

			if (bufferIndex >= dataLen) {
				offset += dataLen;
				dataLen = input.read(ioBuffer);
				if (dataLen == -1) {
					dataLen = 0; // so next offset += dataLen won't decrement
					// offset
					if (length > 0)
						break;
					else
						return false;
				}
				bufferIndex = 0;
			}

			final char c = ioBuffer[bufferIndex++];
			// System.out.println("["+c+"]");

			if (!isTokenString(c)) { // if it's a token char

				if (isTokenchar(c)) {
					previousToken = c;
					break;
				}

				if (length == 0) // start of token
					start = offset + bufferIndex - 1;
				else if (length == buffer.length)
					buffer = termAtt.resizeTermBuffer(1 + length);

				buffer[length++] = normalize(c); // buffer it, normalized

				previousToken = c;

				if (length == MAX_WORD_LEN) // buffer overflow!
					break;

			} else if (length > 0) // at non-Letter w/ chars
				break; // return 'em
		}

		termAtt.setTermLength(length);
		offsetAtt
				.setOffset(correctOffset(start), correctOffset(start + length));
		return true;
	}

	private boolean isTokenchar(char c) {
		if( c == delimiterPars || c == delimiterTokens || c ==  delimiterEndLine) return true;
		return false;
	}

	private boolean isTokenString(char c) {

		if (c == delimiterPars && previousToken == delimiterPars)
			return true;
		if (c == delimiterTokens && previousToken == delimiterTokens)
			return true;
		if (c == delimiterEndLine && previousToken == delimiterEndLine)
			return true;
		// return Character.isLetter(c);
		return false;
	}

	protected char normalize(char c) {
		return Character.toUpperCase(c);
	}

	@Override
	public final void end() {
		// set final offset
		int finalOffset = correctOffset(offset);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset(Reader input) throws IOException {
		super.reset(input);
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
	}

}
