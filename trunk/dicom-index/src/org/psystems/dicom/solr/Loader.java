package org.psystems.dicom.solr;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Date;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
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
import org.psystems.dicom.solr.entity.Diagnosis;
import org.psystems.dicom.solr.entity.Employee;
import org.psystems.dicom.solr.entity.Patient;
import org.psystems.dicom.solr.entity.Service;
import org.xml.sax.SAXException;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * @author dima_d
 * 
 *         Индексатор словарей
 * 
 */
public class Loader {

	private CommonsHttpSolrServer server;
	private static Logger logger = Logger.getLogger(Loader.class.getName());

	private static final String VERSION = "0.1a";

	private static final String NAME = Loader.class.getSimpleName();

	private static final String USAGE = NAME + " [Options]";

	private static final String DESCRIPTION = "WebDicom index daemon.\n"
			+ "Options:";

	private static final String EXAMPLE = "\nExample: " + NAME
			+ " -p 8081 -c conf.xml\n"
			+ "=> Starts daemon listening on port 8081";

	private static String conf = NAME + ".xml";
	private static int portSolr = 8983;
	private static int portAdmin = 8984;

	/**
	 * @param args
	 * @return
	 */
	private static CommandLine parse(String[] args) {

		// args = new String[] {"-p", "8801", "--conf=1.xml"};
		args = new String[] { "-p", "8801", "--conf=1.xml", "--port-adm=8802" };

		Options opts = new Options();

		OptionBuilder.withArgName("port");
		OptionBuilder.withLongOpt("port");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("listening on specified port for Solr incoming requests ["
						+ portSolr + "]");
		opts.addOption(OptionBuilder.create("p"));

		OptionBuilder.withArgName("port");
		OptionBuilder.withLongOpt("port-adm");
		OptionBuilder.hasArg();
		OptionBuilder
				.withDescription("listening on specified port for Web Administration ["
						+ portAdmin + "]");
		opts.addOption(OptionBuilder.create());

		OptionBuilder.withArgName("file");
		OptionBuilder.withLongOpt("conf");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("configuration file [" + conf + "]");
		opts.addOption(OptionBuilder.create("c"));

		opts.addOption("h", "help", false, "print this message");
		opts.addOption("V", "version", false,
				"print the version information and exit");

		CommandLine cl = null;

		try {
			cl = new PosixParser().parse(opts, args);
		} catch (ParseException e) {
			exit(NAME + ": " + e.getMessage());
			throw new RuntimeException("unreachable");
		}
		if (cl.hasOption("V")) {
			System.out.println(NAME + " " + VERSION);
			System.exit(0);
		}

		if (cl.hasOption("c")) {
			conf = cl.getOptionValue("c");
		}
		if (cl.hasOption("port")) {
			portSolr = Integer.valueOf(cl.getOptionValue("port"));
		}
		if (cl.hasOption("port-adm")) {
			portSolr = Integer.valueOf(cl.getOptionValue("port-adm"));
		}

		if (cl.hasOption("h") || cl.getArgList().size() == 0) {

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
			System.exit(0);
		}

		return cl;
	}

	/**
	 * @param msg
	 */
	private static void exit(String msg) {
		System.err.println(msg);
		System.err.println("Try 'Loader -h' for more information.");
		System.exit(1);
	}

	public static void main(String[] args) throws IOException, SAXException {

		CommandLine cl = parse(args);
		if (cl.hasOption("p"))
			System.out.println("!!! port !!!!" + cl.getOptionValue("p"));

		try {
			new Loader();
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
		}
	}

	public Loader() throws IOException, SolrServerException,
			MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InterruptedException {

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
				"org.psystems.webdicom.mbeans:type=Loader,port=8000");
		adapter.setPort(8000);

		mbs.registerMBean(adapter, adapterName);

		adapter.start();

		server = new CommonsHttpSolrServer("http://localhost:8983/solr");

		// передача будет в бинарном формате
		((CommonsHttpSolrServer) server)
				.setRequestWriter(new BinaryRequestWriter());

		startAllIndexing();

		// Wait forever
		logger.info("Waiting forever...");
		Thread.sleep(Long.MAX_VALUE);

	}

	public void startAllIndexing() throws IOException, SolrServerException {
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
	 */
	public void syncDicDiagnosis(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Diagnosis...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Diagnosis dia = new Diagnosis();
			dia.setId(dia.getDicName() + i);
			dia.setDiagnosisCode("CODE" + i);
			dia.setDiagnosisDescription("DESCR" + i);

			solr.addBean(dia);
			solr.commit();
		}
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
	 */
	public void syncDicEmployes(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Employee...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Employee emp = new Employee();
			emp.setId(emp.getDicName() + i);
			if (i % 2 == 0) {
				emp.setEmployeeType(Employee.TYPE_DOCTOR);
			} else {
				emp.setEmployeeType(Employee.TYPE_OPERATOR);
			}
			emp.setEmployeeCode("CODE" + i);
			emp.setEmployeeName("NAME" + i);

			solr.addBean(emp);
			solr.commit();
		}
		logger.info("Sync Employee [OK]");
	}

	/**
	 * Синхронизация словаря пациентов
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicPatients(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Patient...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Patient patient = new Patient();
			patient.setId(patient.getDicName() + i);
			patient.setPatientId("ID" + i);
			patient.setPatientSex("M");
			patient.setPatientBirthDate(Date.valueOf("1974-03-01"));
			patient.setPatientName("PATIENT PATI PAT" + i);
			patient.setPatientShortName("PATPP74");

			solr.addBean(patient);
			solr.commit();
		}
		logger.info("Sync Patient [OK]");
	}

}
