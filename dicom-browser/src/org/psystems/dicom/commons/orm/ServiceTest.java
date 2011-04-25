package org.psystems.dicom.commons.orm;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ServiceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToPersistentString() {
		Service srv = new Service();
		srv.setServiceCode("SRV1");
		srv.setServiceAlias("SA");
		srv.setServiceDescription("Услуга номер 1");
		assertEquals(srv.toPersistentString(), "SRV1^SA^Услуга номер 1");
		
		srv.setServiceDescription("Услуга ^ номер 1");
		assertEquals(srv.toPersistentString(), "SRV1^SA^Услуга ##### номер 1");
		
		srv.setServiceDescription("Услуга ^ | номер 1");
		assertEquals(srv.toPersistentString(), "SRV1^SA^Услуга ##### @@@@@ номер 1");
	}
	
	
	public void testGetFromPersistentString() {
		
		assertNull(Service.getFromPersistentString(null));
		assertNull(Service.getFromPersistentString(""));
		
		Service srv = Service.getFromPersistentString("SRV1^SA^Услуга номер 1");
		assertEquals(srv.getServiceCode(), "SRV1");
		assertEquals(srv.getServiceAlias(), "SA");
		assertEquals(srv.getServiceDescription(), "Услуга номер 1");
		
		srv = Service.getFromPersistentString("SR ##### @@@@@ V1^SA#####@@@@@^Услуга @@@@@ ##### номер 1");
		assertEquals(srv.getServiceCode(), "SR ^ | V1");
		assertEquals(srv.getServiceAlias(), "SA^|");
		assertEquals(srv.getServiceDescription(), "Услуга | ^ номер 1");
		
		try {
			Service.getFromPersistentString("SRV1^SA^");
			fail("Wrong pattern");
		} catch (IllegalArgumentException ex) {}
		try {
			Service.getFromPersistentString("SRV1^^Услуга номер 1");
			fail("Wrong pattern");
		} catch (IllegalArgumentException ex) {}
	}

	public void testToPersistentCollectionString() {
		
		ArrayList<Service> srvs = new ArrayList<Service>();
		
		Service srv = new Service();
		srv.setServiceCode("SRV1");
		srv.setServiceAlias("SA");
		srv.setServiceDescription("Услуга номер 1");
		srvs.add(srv);
		assertEquals(srv.toPersistentString(), "SRV1^SA^Услуга номер 1");
		
		srv = new Service();
		srv.setServiceCode("SRV2");
		srv.setServiceAlias("SB");
		srv.setServiceDescription("Услуга номер 2");
		srvs.add(srv);
		assertEquals(srv.toPersistentString(), "SRV2^SB^Услуга номер 2");
		
		assertEquals(Service.toPersistentCollectionString(srvs.toArray(new Service[srvs.size()])), 
				"SRV1^SA^Услуга номер 1|SRV2^SB^Услуга номер 2");
		
		srvs.clear();
		
		srv = new Service();
		srv.setServiceCode("SRV1");
		srv.setServiceAlias("SA");
		srv.setServiceDescription("Услуга ^ | номер 1");
		srvs.add(srv);
		assertEquals(srv.toPersistentString(), "SRV1^SA^Услуга ##### @@@@@ номер 1");
		
		srv = new Service();
		srv.setServiceCode("SRV2");
		srv.setServiceAlias("SB^|");
		srv.setServiceDescription("Услуга номер 2");
		srvs.add(srv);
		assertEquals(srv.toPersistentString(), "SRV2^SB#####@@@@@^Услуга номер 2");
		
		assertEquals(Service.toPersistentCollectionString(srvs.toArray(new Service[srvs.size()])), 
				"SRV1^SA^Услуга ##### @@@@@ номер 1|SRV2^SB#####@@@@@^Услуга номер 2");
		
		
	}
	

	public void testGetCollectionFromPersistentString() {
		Service srvs[] = Service
				.getCollectionFromPersistentString("SRV1^SA^Услуга ##### @@@@@ номер 1|SRV2^SB#####@@@@@^Услуга номер 2");

		assertEquals(srvs[0].getServiceCode(), "SRV1");
		assertEquals(srvs[0].getServiceAlias(), "SA");
		assertEquals(srvs[0].getServiceDescription(), "Услуга ^ | номер 1");

		assertEquals(srvs[1].getServiceCode(), "SRV2");
		assertEquals(srvs[1].getServiceAlias(), "SB^|");
		assertEquals(srvs[1].getServiceDescription(), "Услуга номер 2");
	}

}
