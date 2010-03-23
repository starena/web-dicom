package com.asutp.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;

public class Indexator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Indexator();
	}

	private String inputDir = "testdata/in";
	private String outDir = "testdata/out";

	public Indexator() {
		
		
		
		try {
//			analyze();
			make();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void analyze() throws IOException {
		// TODO Auto-generated method stub
//		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		SXMAnalyzer analyzer = new SXMAnalyzer();
		
		String text = "Y==305&&X==203&&HEIGHT ==15&&WIDTH ==45&&LAYER==!SOIVidget_LAYER_0.30252851845360773&&PARAM==1100016,3,4010000,5,Давление НК [БН ЦДНГ-6 - (Северо-Юрьевское) 4 ГЗУ4],2,&&VTYPE==SOIVidget_INFO&&PARENT==0&&GNAME==!SOIVidget_INFO_0.5889943553914814&&^^";
		TokenStream stream = analyzer.tokenStream("content", new StringReader(text));
		System.out.println("Use tokinizer: "+stream.getClass());
		for(Iterator<Class<? extends Attribute>> iter = stream.getAttributeClassesIterator(); iter.hasNext();) {
			Class<? extends Attribute> att = iter.next();
			System.out.println("att="+att);
		} 
		
		while(stream.incrementToken()) {
		TermAttribute termAtt = stream.getAttribute(TermAttribute.class);
		System.out.print("<" +termAtt.term()+">");
		}

		
		

	}

	private void make() throws IOException {
		File index = new File(outDir);
		SimpleFSDirectory indexdir = new SimpleFSDirectory(index);
		SXMAnalyzer analyzer = new SXMAnalyzer();
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		

		IndexWriter writer = new IndexWriter(indexdir, analyzer,
				IndexWriter.MaxFieldLength.LIMITED);

		scan(writer);

		System.out.println("Optimizing index");
		writer.optimize();
		writer.close();
		System.out.println("!! success !!!");
	}

	/**
	 * @param writer
	 * @throws IOException
	 */
	private void scan(IndexWriter writer) throws IOException {

		File dir = new File(inputDir);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {

			if(files[i].getName().equals(".svn")) continue;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(files[i]), "Cp1251"));
			String inpStr = null;
			StringBuffer sb = new StringBuffer();
			while ((inpStr = br.readLine()) != null) {
				sb.append(inpStr);
			}
			br.close();
			Document doc = new Document();
			doc.add(new Field("content", sb.toString(), Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);

		}
	}

	
}
