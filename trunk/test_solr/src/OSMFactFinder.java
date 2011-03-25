import java.util.Map;

/**
 * Given an OSM id, return a Map of facts about that ID.  This is just for demo purposes
 * as a way to inject textual info that is not available in the OSM file.
 *
 **/
public interface OSMFactFinder {

  /**
   * Lookup facts about an OSM node id.
   * @param id The id to lookup
   * @return A Map of facts.  Key is a short name (unique) of the fact, value is the fact.  null if it doesn't exist
   */
  public Map<String, Fact> lookup(long id);
}
