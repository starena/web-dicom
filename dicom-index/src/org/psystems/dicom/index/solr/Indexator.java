package org.psystems.dicom.index.solr;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.psystems.dicom.commons.solr.entity.Diagnosis;
import org.psystems.dicom.commons.solr.entity.Employee;
import org.psystems.dicom.commons.solr.entity.Patient;
import org.psystems.dicom.commons.solr.entity.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * @author dima_d
 * 
 *         Индексатор словарей
 * 
 */
public class Indexator {

	private CommonsHttpSolrServer server;
	private Connection connectionOMITS;
	private String connectionStrLocal = "jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM";
//	private String connectionStrOMITS = "jdbc:oracle:thin:DICOM_USER/EPy8jC5l@localhost:30001:SRGP1";
	//FIXME вынести в конфиг
	private String connectionStrOMITS = "jdbc:oracle:thin:DICOM_USER/EPy8jC5l@192.168.95.5:1521:SRGP1";
	public static String oraDriverClass = "oracle.jdbc.driver.OracleDriver";
	private static Logger logger = Logger.getLogger(Indexator.class.getName());

	private static final String VERSION = "0.1a";

	private static final String NAME = Indexator.class.getSimpleName();

	private static final String USAGE = NAME + " [Options]";

	private static final String DESCRIPTION = "WebDicom index tool.\n"
			+ "Options:";

	private static final String EXAMPLE = "\nExample:\n"
			+ NAME
			+ " -d -c conf.xml\n ==> Starts daemon listening by config file <conf.xml>\n"
			+ NAME
			+ " -semp -db 2011-05-21\n ==> Synchronize anly patient records modified after 2011-05-21";

	private static String conf = "conf.xml";
	private static int portSolr = 8983;
	private static int portAdmin = 8984;
	private static boolean daemonMode = false;
	private static boolean syncDiagnosis = false;
	private static boolean syncEmplopyes = false;
	private static boolean syncPatients = false;
	private static boolean syncServices = false;

	/**
	 * @param args
	 * @return
	 */
	private static void argsParse(String[] args) {

		// args = new String[] { "--conf=1.xml"};
//		args = new String[] { "-sdia" , "-ssrv", "-semp", "-spat"};
		Options opts = new Options();

		opts.addOption("d", "daemon", false, "work as daemon");

		OptionBuilder.withArgName("file");
		OptionBuilder.withLongOpt("conf");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("configuration file [" + conf + "]");
		opts.addOption(OptionBuilder.create("c"));

		// Option property = OptionBuilder.withArgName("property=value")
		// .hasArgs(2).withValueSeparator().withDescription(
		// "use value for given property").create("D");
		// opts.addOption(property);

		opts.addOption("spat", "syncpatients", false, "Synchronize patients");
		opts.addOption("sdia", "syncdiagnosis", false, "Synchronize diagnisis");
		opts.addOption("semp", "syncemployes", false, "Synchronize employes");
		opts.addOption("ssrv", "syncservices", false, "Synchronize services");

		opts.addOption("db", "datebegin", false,
				"Begin date interval (pattern: yyyy-mm-dd)");
		opts.addOption("de", "dateend", false,
				"End date interval (pattern: yyyy-mm-dd)");

		opts.addOption("h", "help", false, "print this message");
		opts.addOption("V", "version", false,
				"print the version information and exit");

		CommandLine cl = null;

		if (args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
			System.exit(0);
		}

		try {
			cl = new PosixParser().parse(opts, args);
		} catch (ParseException e) {
			System.err.println(NAME + ": " + e.getMessage());
			logger.fatal(NAME + ": " + e.getMessage());
			System.err.println("Try 'Loader -h' for more information.");
			System.exit(1);
			throw new RuntimeException("unreachable");
		}
		if (cl.hasOption("V")) {
			System.out.println(NAME + " " + VERSION);
			System.exit(0);
		}

		if (cl.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
			System.exit(0);
		}

		if (cl.hasOption("c"))
			conf = cl.getOptionValue("c");

		if (cl.hasOption("d"))
			daemonMode = true;

		if (cl.hasOption("sdia"))
			syncDiagnosis = true;

		if (cl.hasOption("semp"))
			syncEmplopyes = true;

		if (cl.hasOption("spat"))
			syncPatients = true;

		if (cl.hasOption("ssrv"))
			syncServices = true;

		try {
			praseConfig();
		} catch (ParserConfigurationException e) {
			logger.fatal("Broken config! " + e.getMessage());
			System.err.println("Broken config! " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			logger.fatal("Broken config! " + e.getMessage());
			System.err.println("Broken config! " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.fatal("Broken config! " + e.getMessage());
			System.err.println("Broken config! " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Парсинг конфигурационного файла
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static void praseConfig() throws ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(conf));
		Element nodeRoot = doc.getDocumentElement();

		for (int level_0 = 0; level_0 < nodeRoot.getChildNodes().getLength(); level_0++) {
			Node node_0 = nodeRoot.getChildNodes().item(level_0);

			if (node_0.getNodeName().equals("index")) {

				for (int level_1 = 0; level_1 < node_0.getChildNodes()
						.getLength(); level_1++) {
					Node node_1 = node_0.getChildNodes().item(level_1);

					if (node_1.getNodeName().equals("solr")) {
						portSolr = Integer.valueOf(node_1.getAttributes()
								.getNamedItem("port").getNodeValue());
					}
					if (node_1.getNodeName().equals("admin")) {

						portAdmin = Integer.valueOf(node_1.getAttributes()
								.getNamedItem("port").getNodeValue());

					}
				}
			}
		}

	}

	/**
	 * Проверка-установка соединения
	 * 
	 * @throws SQLException
	 */
	void checkMakeConnection() throws SQLException {

		if (connectionOMITS != null && connectionOMITS.isValid(0)) {
			return;
		}

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); // FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		Connection conn = DriverManager.getConnection(connectionStrLocal
				+ ";create=true", props);
		// conn.setAutoCommit(false);
		// s = conn.createStatement();
		// s.execute(sql);
		//
		// conn.commit();

		// return conn;
		connectionOMITS = conn;
	}

	private Connection getConnectionOMITS() {

		try {
			Class.forName(oraDriverClass);
			Properties props = new Properties(); // connection properties
			connectionOMITS = DriverManager.getConnection(connectionStrOMITS,
					props);
		} catch (ClassNotFoundException exx) {
			try {
				throw new SQLException("driver not found!  '" + oraDriverClass
						+ "'");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connectionOMITS;

	}

	public static void main(String[] args) throws IOException, SAXException {

		argsParse(args);
		logger.info("starting");
		logger.info("using config variables: config-file=" + conf + " port="
				+ portSolr + " admport=" + portAdmin);

		try {
			new Indexator();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Indexator() throws IOException, SolrServerException,
			MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InterruptedException, SQLException {

		// Get the Platform MBean Server
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		HtmlAdaptorServer adapter = new HtmlAdaptorServer();

		// Construct the ObjectName for the MBean we will register
		ObjectName name = new ObjectName(
				"org.psystems.webdicom.mbeans:type=Loader");

		// Create the Loader MBean
		LoaderStatus mbean = new LoaderStatus(this);

		// Register the Hello World MBean
		mbs.registerMBean(mbean, name);

		// Теперь мы регистрируем коннектор, который
		// будет доступен по HTTP-протоколу
		ObjectName adapterName = new ObjectName(
				"org.psystems.webdicom.mbeans:type=Loader,port=" + portAdmin);
		adapter.setPort(portAdmin);

		mbs.registerMBean(adapter, adapterName);

		adapter.start();

		server = new CommonsHttpSolrServer("http://localhost:" + portSolr
				+ "/solr");

		// передача будет в бинарном формате
		((CommonsHttpSolrServer) server)
				.setRequestWriter(new BinaryRequestWriter());

		// Индексируем все словари
		if (!syncDiagnosis && !syncServices && !syncEmplopyes && !syncPatients) {
			startAllIndexing();
		} else {

			if (syncDiagnosis) {
				syncDicDiagnosis(server);
				server.optimize();
			}
			if (syncServices) {
				syncDicServices(server);
				server.optimize();
			}
			if (syncEmplopyes) {
				syncDicEmployes(server);
				server.optimize();
			}
			if (syncPatients) {
				syncDicPatients(server);
				server.optimize();
			}
		}

		if (daemonMode) {
			// Wait forever
			logger.info("Waiting forever...");
			Thread.sleep(Long.MAX_VALUE);
		}

	}

	public void startAllIndexing() throws IOException, SolrServerException,
			SQLException {

		logger.info("all indexing...");
		
		syncDicPatients(server);
		server.optimize();
		syncDicDiagnosis(server);
		server.optimize();
		syncDicServices(server);
		server.optimize();
		syncDicEmployes(server);
		server.optimize();
	}

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Синхронизация словаря диагнозов
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws SQLException 
	 */
	public void syncDicDiagnosis(SolrServer solr) throws IOException,
			SolrServerException, SQLException {
		
		logger.info("Sync Diagnosis...");
		
		connectionOMITS = getConnectionOMITS();
		System.out.println("!!! connection = " + connectionOMITS);


		PreparedStatement psSelect = null;

		try {
			psSelect = connectionOMITS
					.prepareStatement("select v.CODE, v.NAME from  V_MKB10 v ");

			ResultSet rs = psSelect.executeQuery();
			int index = 0;

			while (rs.next()) {

			
				Diagnosis dia = new Diagnosis();
				dia.setId("Diagnosis_"+rs.getString("CODE"));
				dia.setDiagnosisCode(rs.getString("CODE"));
				dia.setDiagnosisDescription(rs.getString("NAME"));
				
				
				//TODO убрать!!!
				if(index % 100 == 0) 
				System.out.println("!!!! Load = " + index + " [Diagnosis]" + dia);

				logger.warn((index++) + " [Diagnosis]" + dia);
				
				solr.addBean(dia);
				solr.commit();

			}
			rs.close();

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if (connectionOMITS != null)
					connectionOMITS.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		// int maxDocs = 30;
		// for (int i = 0; i < maxDocs; i++) {
		// Patient patient = new Patient();
		// patient.setId(patient.getDicName() + i);
		// patient.setPatientId("ID" + i);
		// patient.setPatientSex("M");
		// patient.setPatientBirthDate(Date.valueOf("1974-03-01"));
		// patient.setPatientName("PATIENT PATI PAT" + i);
		// patient.setPatientShortName("PATPP74");
		//
		// solr.addBean(patient);
		// solr.commit();
		// }
	
		
		
//		int maxDocs = 30;
//		for (int i = 0; i < maxDocs; i++) {
//			Diagnosis dia = new Diagnosis();
//			dia.setId(dia.getDicName() + i);
//			dia.setDiagnosisCode("CODE" + i);
//			dia.setDiagnosisDescription("DESCR" + i);
//
//			solr.addBean(dia);
//			solr.commit();
//		}
		
		logger.info("Sync Diagnosis [OK]");
	}

	/**
	 * Синхронизация словаря услуг
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicServices(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Service...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Service srv = new Service();
			srv.setId(srv.getDicName() + i);
			srv.setServiceAlias("ALIAS" + i);
			srv.setServiceCode("CODE" + i);
			srv.setServiceDescription("DESCR" + i);
			srv.setModality("CR");

			solr.addBean(srv);
			solr.commit();
		}

		Service srv;

		srv = new Service();
		srv.setId("service_" + "A.04.20.001.01");
		srv.setModality("US");
		srv.setServiceCode("A.04.20.001.01");
		srv.setServiceAlias("Узи матки и придатков (трансабдоминально)");
		srv
				.setServiceDescription("Ультразвуковое исследование матки и придатков (трансабдоминально)");
		solr.addBean(srv);

		srv = new Service();
		srv.setId("service_" + "A.04.20.002.01");
		srv.setModality("US");
		srv.setServiceCode("A.04.20.002.01");
		srv.setServiceAlias("Узи молочных желез");
		srv.setServiceDescription("Ультразвуковое исследование молочных желез");
		solr.addBean(srv);

		srv = new Service();
		srv.setId("service_" + "A.03.16.001.01");
		srv.setModality("ES");
		srv.setServiceCode("A.03.16.001.01");
		srv.setServiceAlias("Эгдс");
		srv.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");
		solr.addBean(srv);

		srv = new Service();
		srv.setId("service_" + "A.05.23.001.03");
		srv.setModality("ES");
		srv.setServiceCode("A.05.23.001.03");
		srv.setServiceAlias("ЭЭГ");
		srv
				.setServiceDescription("Электроэнцефалография с компьютерной обработкой и функциональными пробами");
		solr.addBean(srv);

		srv = new Service();
		srv.setId("service_" + "USI01");
		srv.setModality("US");
		srv.setServiceCode("USI01");
		srv.setServiceAlias("USI IS COOL");
		srv.setServiceDescription("USI IS VERY VERY COOL!!!");
		solr.addBean(srv);

		solr.commit();
		logger.info("Sync Service [OK]");
	}

	/**
	 * Синхронизация словаря сотрудников
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws SQLException 
	 */
	public void syncDicEmployes(SolrServer solr) throws IOException,
			SolrServerException, SQLException {
		
		logger.info("Sync Employee...");
		
		connectionOMITS = getConnectionOMITS();
		System.out.println("!!! connection = " + connectionOMITS);


		PreparedStatement psSelect = null;

		try {
			psSelect = connectionOMITS
					.prepareStatement("select v.ID, v.FULL_NAME from lpu.v_doctor v");

			ResultSet rs = psSelect.executeQuery();
			int index = 0;

			while (rs.next()) {
				
				Employee emp = new Employee();
				emp.setId("Employee_" + rs.getString("ID"));
				
				//FIXME Реализовать!
				emp.setEmployeeType(Employee.TYPE_DOCTOR);
				//emp.setEmployeeType(Employee.TYPE_OPERATOR);
				
				//FIXME Реализовать!
				emp.setEmployeeCode("CODE" + rs.getShort("ID"));
				emp.setEmployeeName(rs.getString("FULL_NAME"));
				
				//TODO убрать!!!
				if(index % 100 == 0) 
				System.out.println("!!!! Load = " + index + " [Employee]" + emp);

				logger.warn((index++) + " [Employee]" + emp);
				
				solr.addBean(emp);
				solr.commit();

			}
			rs.close();

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if (connectionOMITS != null)
					connectionOMITS.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		
		
//		logger.info("Sync Employee...");
//		int maxDocs = 30;
//		for (int i = 0; i < maxDocs; i++) {
//			Employee emp = new Employee();
//			emp.setId(emp.getDicName() + i);
//			if (i % 2 == 0) {
//				emp.setEmployeeType(Employee.TYPE_DOCTOR);
//			} else {
//				emp.setEmployeeType(Employee.TYPE_OPERATOR);
//			}
//			emp.setEmployeeCode("CODE" + i);
//			emp.setEmployeeName("NAME" + i);
//
//			solr.addBean(emp);
//			solr.commit();
//		}
		logger.info("Sync Employee [OK]");
	}

	/**
	 * Синхронизация словаря пациентов
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws SQLException
	 */
	public void syncDicPatients(SolrServer solr) throws IOException,
			SolrServerException, SQLException {

		logger.info("Sync Patient...");
		
		connectionOMITS = getConnectionOMITS();
		System.out.println("!!! connection = " + connectionOMITS);


		PreparedStatement psSelect = null;

		try {
			psSelect = connectionOMITS
					.prepareStatement("select v.ID, v.FIRST_NAME, v.SUR_NAME, v.PATR_NAME, v.CODE, v.BIRTHDAY, v.SEX "
							+ " from  lpu.v_patient v ");

			ResultSet rs = psSelect.executeQuery();
			int index = 0;

			while (rs.next()) {

				Patient patient = new Patient();
				patient.setId("Patient_" + rs.getString("ID"));
				patient.setPatientId(rs.getString("ID"));
				patient.setPatientSex(rs.getString("SEX"));
				patient.setPatientBirthDate(rs.getDate("BIRTHDAY"));
				patient.setPatientName(rs.getString("SUR_NAME") + " "
						+ rs.getString("FIRST_NAME") + " "
						+ rs.getString("PATR_NAME"));
				patient.setPatientShortName(rs.getString("CODE"));
				
				
				//TODO убрать!!!
				if(index % 100 == 0) 
				System.out.println("!!!! Load = " + index + " [Patient]" + patient);

				logger.warn((index++) + " [Patient]" + patient);
				
				solr.addBean(patient);
				solr.commit();

			}
			rs.close();

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if (connectionOMITS != null)
					connectionOMITS.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		// int maxDocs = 30;
		// for (int i = 0; i < maxDocs; i++) {
		// Patient patient = new Patient();
		// patient.setId(patient.getDicName() + i);
		// patient.setPatientId("ID" + i);
		// patient.setPatientSex("M");
		// patient.setPatientBirthDate(Date.valueOf("1974-03-01"));
		// patient.setPatientName("PATIENT PATI PAT" + i);
		// patient.setPatientShortName("PATPP74");
		//
		// solr.addBean(patient);
		// solr.commit();
		// }
		logger.info("Sync Patient [OK]");
	}

}
