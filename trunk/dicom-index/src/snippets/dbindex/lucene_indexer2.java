package snippets.dbindex;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.lucene.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author dima_d
 * Индексатор
 * http://iborodikhin.net/lucene
 *
 */
public class lucene_indexer2 {
   private static Connection con;
   private static IndexWriter writer;

   public static void main(String[] args) throws Exception {
      // Loading configuration
      lucene_registry.load(args[0]);

      String dbhost = lucene_registry.get( "dbhost" );
      String dbuser = lucene_registry.get( "dbuser" );
      String dbpass = lucene_registry.get( "dbpass" );
      String dbname = lucene_registry.get( "dbname" );
      String index_dir = lucene_registry.get( "index_dir" );

      // Open index
      File index = new File( "../" + index_dir );
      if( !index.exists() ) {
         System.out.println("Index directory does not exist");
         System.exit(0);
      }
      SimpleFSDirectory indexdir = new SimpleFSDirectory(index);
      writer = new IndexWriter(indexdir, new StandardAnalyzer(Version.LUCENE_CURRENT), (args[1] == "1"), IndexWriter.MaxFieldLength.LIMITED);

      // Connecting to database
      String driver = "com.mysql.jdbc.Driver";
      String connection = "jdbc:mysql://"+ dbhost +":3306/"+dbname;
      Class.forName(driver);
      con = DriverManager.getConnection(connection, dbuser, dbpass);

      // Get unindexed documents from database
      Statement stmt = con.createStatement();
      String sql = "SELECT " + lucene_registry.get( "return_field" ) + "," + lucene_registry.get( "search_fields" ) + " FROM " + lucene_registry.get( "search_table" ) + " WHERE " + lucene_registry.get( "index_flag" ) + "=0";
      ResultSet rs = stmt.executeQuery(sql);
      while( rs.next() ) {
         indexDocument(rs);
      }
      con.close();
      rs.close();
      stmt.close();

      // Optimize index
      System.out.println("Optimizing index");
      writer.optimize();
      writer.close();
   }

   public static void indexDocument(ResultSet rs) throws Exception {
      String return_field = lucene_registry.get( "return_field" );
      String[] search_fields = lucene_registry.get( "search_fields" ).split(",");
      Document doc = new Document();
      doc.add( new Field("_"+return_field, rs.getString(return_field), Field.Store.YES, Field.Index.NO) );
      for(int i=0; i<search_fields.length; i++) {
            doc.add( new Field("_"+search_fields[i], search_fields[i], Field.Store.NO, Field.Index.ANALYZED) );
      }
      writer.addDocument( doc );
      System.out.println("Added to index post ID=" + rs.getString(return_field));
      Statement stmt = con.createStatement();
      stmt.executeUpdate("UPDATE "+lucene_registry.get( "search_table" )+" SET "+lucene_registry.get( "index_flag" )+"=1 WHERE "+lucene_registry.get( "return_field" )+"=" + rs.getString(return_field));
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