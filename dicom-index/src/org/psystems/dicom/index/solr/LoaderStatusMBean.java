package org.psystems.dicom.index.solr;

/**
 * @author dima_d
 *
 * Интерфейс управления индексатора через JMX
 */
public interface LoaderStatusMBean {

	public String getLogLevel();

	public void setLogLevel(String level);

	public boolean isIndexing();

	public void startIndexing();

}
