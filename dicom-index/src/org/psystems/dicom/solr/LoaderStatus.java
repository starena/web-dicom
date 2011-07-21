package org.psystems.dicom.solr;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.solr.client.solrj.SolrServerException;

public class LoaderStatus implements LoaderStatusMBean {

	private Indexator loader;

	public LoaderStatus(Indexator loader) {
		this.loader = loader;

	}

	@Override
	public String getLogLevel() {
		// TODO Auto-generated method stub
		//return Loader.getLogger().getLevel().toString();
		
		if(Indexator.getLogger().getLevel()!=null) {
			return Indexator.getLogger().getLevel().toString();	
		}else {
			return Indexator.getLogger().getRootLogger().getLevel().toString();
		}
	}

	@Override
	public boolean isIndexing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLogLevel(String level) {
		Indexator.getLogger().setLevel(Level.toLevel(level));

	}

	@Override
	public void startIndexing() {
		// TODO Auto-generated method stub
		try {
			loader.startAllIndexing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
