package snippets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class Snippet1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Snippet1();
	}

	public Snippet1() {
		try {
			make();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void make() throws IOException {
		File index = new File("testdata/out");
		SimpleFSDirectory indexdir = new SimpleFSDirectory(index);
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

//		Document doc = new Document();
		IndexWriter writer = new IndexWriter(indexdir, analyzer,
				IndexWriter.MaxFieldLength.LIMITED);

//		doc.add(new Field("content", "plain text from file", Field.Store.YES,
//				Field.Index.ANALYZED));
//		writer.addDocument(doc);
		
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

		File dir = new File("testdata/in");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {

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
