package snippets.dbindex;
import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.*;
import org.apache.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.HiCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.TopDocs;

/**
 * @author dima_d
 *
 * http://iborodikhin.net/lucene
 * Поисковый сервер
 */
public class lucene_search2 {
   private static ServerSocket ssock = null;
   public static Socket csock = null;
   public static String port = null;

   public static void main(String[] args) {
      System.out.println("Started Lucene_Search_Server");
      // Load configuration
      lucene_registry.load(args[0]);
      String port = lucene_registry.get("listen_port");
      int port_no = Integer.parseInt( port );

      // Listen port
      try {
         ssock = new ServerSocket( port_no );
      } catch( IOException e ) {
         System.out.println( "Cannot bind port no: " +port );
         System.exit( -1 );
      }

      System.out.println("Bind to port no "+port);

      // Accept connection
      try {
         while(true) {
            new lucene_thread(ssock.accept(), args[0]).start();
         }
      } catch ( IOException e ) {
         System.out.println("Accept failed: "+port);
         System.exit( -1 );
      }
   } 
}

class lucene_thread extends Thread {
   private static Socket csock = null;
   public static int search_depth = 0;
   public static Analyzer analyzer = null;
   public static Searcher searcher = null;

   public lucene_thread( Socket s, String args ) {
      csock = s;
   }
   public void run() {
      System.out.println("New thread");

      String index_dir = lucene_registry.get( "index_dir" );
      String port = lucene_registry.get( "listen_port" );
      String depth = lucene_registry.get( "search_depth" );
      int search_depth = Integer.valueOf( depth );
      String[] search_fields = lucene_registry.get( "search_fields" ).split(",");

      try {
         System.out.println("Accepted connection");

         // Get reader / writer
         PrintWriter out = new PrintWriter(csock.getOutputStream(), true);
         BufferedReader in = new BufferedReader( new InputStreamReader( csock.getInputStream() ) );

         // Parse input and process output
         String inputLine;
         inputLine = in.readLine();

         System.out.println("Requested " + inputLine);

         // Open index
         searcher = new IndexSearcher("../"+index_dir);
         analyzer = new StandardAnalyzer();

         // Load query
         QueryParser parser = new QueryParser( "_"+search_fields[0], analyzer );
         Query query = parser.parse( inputLine );

         // Do search
         TopDocCollector collector = new TopDocCollector( search_depth );
         searcher.search( query, collector );
         ScoreDoc[] hits = collector.topDocs().scoreDocs;
         for ( int i = 0; i < hits.length; i++ ) {
            int docId = hits[i].doc;
            Document d = searcher.doc( docId );
            out.println( d.get( "_"+lucene_registry.get( "return_field" ) ) );
         }
         System.out.println("Results count: "+hits.length);

         // Close sockets
         out.close();
         in.close();
         csock.close();
      } catch( Exception e ) {}
   }
}

class lucene_registry {
	private static Properties props = null;

	public static String get( String key ) {
		if(props == null) {
			return "";
		}
		return props.getProperty(key);
	}

	public static void load( String filename ) {
		try {
			if(props == null) {
				props = new Properties();
				props.load( new FileInputStream( filename ) );
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}