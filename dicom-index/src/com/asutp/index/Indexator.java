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
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
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
	private String fieldContentName = "content";
	private String fieldSXMName = "sxm";
	
	Analyzer analyzer;

	public Indexator() {

//		analyzer = new SXMAnalyzer();
		analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		
		try {
//			 analyze();
			 make();
			try {
//				search("\"Сводная таблица работы артезианских скважин НГДУ \"Нижнесортымснефть\"\"");
				search("ЦДНГ-6");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void analyze() throws IOException {
		// TODO Auto-generated method stub
		// WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		// StandardAnalyzer analyzer = new
		// StandardAnalyzer(Version.LUCENE_CURRENT);
		

		String text = "Y==305&&X==203&&HEIGHT ==15&&WIDTH ==45&&LAYER==!SOIVidget_LAYER_0.30252851845360773&&PARAM==1100016,3,4010000,5,Давление НК [БН ЦДНГ-6 - (Северо-Юрьевское) 4 ГЗУ4],2,&&VTYPE==SOIVidget_INFO&&PARENT==0&&GNAME==!SOIVidget_INFO_0.5889943553914814&&^^\r\n" +
				"Y==665&&X==31&&NOTHIDING==1&&WIDTH==45&&LAYER==!SOIVidget_LAYER_0.18855396644095967&&ALIGN==Center&&HINT==КНС №1 Северо-Юрьевского м/р&&MTYPE==GotoUrl&&PARENT==0&&VTYPE==SOIVidget_MISC&&GNAME==!SOIVidget_MISC_0.9362122612031901&&DESCR==КНС 1&&URL==../../webclient.cgi?file=/common/kns/6_kns-1su.sxm&&^^";
		TokenStream stream = analyzer.tokenStream("content", new StringReader(
				text));
		System.out.println("Use tokinizer: " + stream.getClass());
		for (Iterator<Class<? extends Attribute>> iter = stream
				.getAttributeClassesIterator(); iter.hasNext();) {
			Class<? extends Attribute> att = iter.next();
			System.out.println("att=" + att);
		}

		while (stream.incrementToken()) {
			TermAttribute termAtt = stream.getAttribute(TermAttribute.class);
			System.out.print("<" + termAtt.term() + ">");
		}

	}

	private void make() throws IOException {
		File index = new File(outDir);
		SimpleFSDirectory indexdir = new SimpleFSDirectory(index);
		// StandardAnalyzer analyzer = new
		// StandardAnalyzer(Version.LUCENE_CURRENT);

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

			if (files[i].getName().equals(".svn"))
				continue;

			String filename = files[i].getName();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(files[i]), "Cp1251"));
			String inpStr = null;
			StringBuffer sb = new StringBuffer();
			while ((inpStr = br.readLine()) != null) {
				sb.append(inpStr);
			}
			br.close();
			Document doc = new Document();
			doc.add(new Field(fieldContentName, sb.toString(), Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field(fieldSXMName, filename, Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);

		}
	}

	private void search(String text) throws IOException, ParseException {

		System.out.println("searching query text = " + text);
		
		File index = new File(outDir);
		SimpleFSDirectory indexdir = new SimpleFSDirectory(index);
	

		// Now search the index:
		IndexSearcher isearcher = new IndexSearcher(indexdir, true); // read-only=true
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, fieldContentName,
				analyzer);
		Query query = parser.parse(text);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

		// assertEquals(1, hits.length);
		System.out.println("hits.length=" + hits.length);
		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out.println("Find: "
					+ hitDoc.get(fieldSXMName));
		}
		isearcher.close();
	}

}
