import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Create an in-memory, simple FactFinder that can load from a file.
 * <p/>
 *
 *  Format of file is:<br/>
 *    id:fact_name=fact_value<br/>
 * <br/>
 * Id is the id in the OSM file.
 * <p/>
 * Lines starting with # are ignored
 *
 **/
public class TextFactFinder implements OSMFactFinder{

  protected Map<Long, Map<String, Fact>> facts;

  public TextFactFinder() {
    facts = new HashMap<Long, Map<String, Fact>>();
  }

  public TextFactFinder(Map<Long, Map<String, Fact>> facts) {
    this.facts = facts;
  }

  //osm_id:[S|T:I|D]:fact_name:fact_value


  public int loadFacts(Reader reader) throws IOException {
    BufferedReader buff = new BufferedReader(reader);
    String line = null;

    while ((line = buff.readLine()) != null){
      line = line.trim();
      if (line.startsWith("#") || line.equals("")){
        continue;
      }
      //TODO: error checking
      String [] splits = line.split(":");
      Long id = Long.parseLong(splits[0]);
      Map<String, Fact> idFacts = facts.get(id);
      if (idFacts == null) {
        idFacts = new HashMap<String, Fact>();
        facts.put(id, idFacts);
      }
      Fact fact = new Fact();
      fact.name= splits[2];
      fact.value = splits[3];
      fact.type = Fact.Type.lookup(splits[1]);
      idFacts.put(fact.name, fact);
    }
    return facts.size();
  }

  public Map<Long, Map<String, Fact>> getFacts() {
    return facts;
  }

  public void setFacts(Map<Long, Map<String, Fact>> facts) {
    this.facts = facts;
  }

  public Map<String, Fact> lookup(long id) {
    return facts.get(id);
  }
}
