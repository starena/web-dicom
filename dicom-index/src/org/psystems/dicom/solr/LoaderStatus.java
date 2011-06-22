package org.psystems.dicom.solr;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.solr.client.solrj.SolrServerException;

public class LoaderStatus implements LoaderStatusMBean {

	private Loader loader;

	public LoaderStatus(Loader loader) {
		this.loader = loader;

	}

	@Override
	public String getLogLevel() {
		// TODO Auto-generated method stub
		//return Loader.getLogger().getLevel().toString();
		
		if(Loader.getLogger().getLevel()!=null) {
			return Loader.getLogger().getLevel().toString();	
		}else {
			return Loader.getLogger().getRootLogger().getLevel().toString();
		}
	}

	@Override
	public boolean isIndexing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLogLevel(String level) {
		Loader.getLogger().setLevel(Level.toLevel(level));

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
		}
	}

}
