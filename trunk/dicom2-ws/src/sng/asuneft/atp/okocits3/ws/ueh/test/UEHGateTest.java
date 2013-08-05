package sng.asuneft.atp.okocits3.ws.ueh.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOMFeature;

import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcGroup;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcGroupFormulaOperand;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcGroupLimit;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcGroupPI;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcGroupRequestFilter;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.CalcLimit;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.ObjectFactory;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.UEHGate;
import sng.asuneft.atp.okocits3.ws.ueh.client.stub.UEHGateService;

public class UEHGateTest {

	private static UEHGateService service;
	private static UEHGate port;
	private static Properties globalProperties;
//	 private static final String WS_URL =
//	 "http://oao-oko-web1/ws-ueh/UEHGate?wsdl";
	 private static final String WS_URL = "http://localhost:8080/ws-ueh/UEHGate?wsdl";
//	 private static final String WS_URL = "http://asu-oko-web1/ws-ueh/UEHGate?wsdl";
//	private static final String WS_URL = "http://localhost:8080/okocits3-ueh-ws/UEHGate?wsdl";
//	private static final String WS_URL = "http://localhost:80/okocits3-ueh-ws/UEHGate?wsdl";

	private static ObjectFactory objectFactory = new ObjectFactory();

	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws DatatypeConfigurationException
	 */
	public static void main(String[] args) throws MalformedURLException,
			DatatypeConfigurationException {

		loadConfig();

		AuthNTLM();

		System.out.println("Testing: " + WS_URL);
		URL url = new URL(WS_URL);
		QName qname = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/",
				"UEHGateService");

		service = new UEHGateService(url, qname);
		// port = service.getDataGatePort();
		// MTOMFeature - Поддержка аттачментов
		port = service.getUEHGatePort(new MTOMFeature());

		UEHGateTest test = new UEHGateTest();

		// test.testGetCalcGroup();

		List<CalcGroupRequestFilter> filters = null;
		CalcGroupRequestFilter filter = null;
		//
		// //All
		 test.testGetCalcGroups(filters,false);
		//
		// //Id
//		filters = new ArrayList<CalcGroupRequestFilter>();
//		filter = new CalcGroupRequestFilter();
//		filter.setId(objectFactory.createCalcGroupRequestFilterId(171017904));
//		filters.add(filter);
//		//
//		test.testGetCalcGroups(filters,false);
		//
		// //Type
		// filters = new ArrayList<CalcGroupRequestFilter>();
		// filter = new CalcGroupRequestFilter();
		// filter.setType(objectFactory.createCalcGroupRequestFilterType("P"));
		// filters.add(filter);
		//
		// //Owner
		// filters = new ArrayList<CalcGroupRequestFilter>();
		// filter = new CalcGroupRequestFilter();
		// filter.setOwner(objectFactory.createCalcGroupRequestFilterOwner("ПУ СургутАСУнефть"));
		// filters.add(filter);

		// test.testGetCalcGroups(filters);

//		 test.newCalcGroup();

//		test.setLimit();

//		 test.deleteCalcGroup();
	}

	/**
	 * 
	 */
	private void deleteCalcGroup() {

		port.deleteCalcGroup(171988287);

	}

	/**
	 * @throws DatatypeConfigurationException
	 * 
	 */
	private void setLimit() throws DatatypeConfigurationException {

		DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
		XMLGregorianCalendar date = dataTypeFactory
				.newXMLGregorianCalendar("2012-09-12T19:23:45");

		LinkedList<CalcLimit> limits = new LinkedList<CalcLimit>();

		for (int i = 0; i <= 23; i++) {

			CalcLimit limit = new CalcLimit();
			limit.setIdCalcgroup(171988287);
			limit.setIdCalcgroupLimit(171988286);
			limit.setHour(i);
			limit.setMinR(i * 1000 + 100d);
			limit.setMinY(i * 1000 + 200d);
			limit.setMaxY(i * 1000 + 300d);
			limit.setMaxR(i * 1000 + 400d);
			limit.setDate(date);

			limits.add(limit);

		}

		port.setLimits(limits);

	}

	/**
	 * 
	 */
	private void newCalcGroup() {
		CalcGroupPI calcgroup = new CalcGroupPI();
		
//		calcgroup.setOwner("ОАО \"Сургутнефтегаз\"");
		calcgroup.setOwnerId(171000000);
		calcgroup.setType("P");
//		calcgroup.setTypeLimit("ТЭК");
		calcgroup.setTypeLimitId(171002342);
		calcgroup.setName("ТЕСТ");
		CalcGroup cgOko = port.newCalcGroup(calcgroup);
		System.out.println(" CalcGroup id: " + cgOko.getId() + " name: "
				+ cgOko.getName());
	}

	/**
	 * 
	 */
	public void testGetCalcGroup() {
		CalcGroup cg = port.getCalcGroup(171017904);

		System.out.println(" CalcGroup " + cg.getId() + ";" + cg.getName()
				+ ";" + cg.getType() + ";" + cg.getOwnerId() + "; formula: ");
		List<CalcGroupFormulaOperand> formula = cg.getFormula();
		for (CalcGroupFormulaOperand operand : formula) {
			System.out.println("   [" + operand.getOperator() + "]"
					+ operand.getObjId() + ";" + operand.getTypePar()
					+ operand.getParId());
		}
		System.out.println(" Limit groups:");
		List<CalcGroupLimit> limits = cg.getCalcGroupLimits();
		for (CalcGroupLimit cgl : limits) {
			System.out.println(" CalcGroupLimit id:" + cgl.getId()+"["+cgl.getType()+"] ["+cgl.getName()+"] owner:"+cgl.getOwnerId()+" typelimit:"+cgl.getTypeLimitId());
		}
		

	}

	/**
	 * 
	 */
	public void testGetCalcGroups(List<CalcGroupRequestFilter> filters, boolean withLimit) {

		List<CalcGroup> cgs = port.getCalcGroups(filters,withLimit);
		if (cgs == null) {
			System.out.println("No Found CalcGroups!");
		}
		for (CalcGroup cg : cgs) {
			System.out.println(" CalcGroup " + cg.getId() + ";" + cg.getName()
					+ ";" + cg.getType() + ";" + cg.getOwnerId() + "; formula: ");
			List<CalcGroupFormulaOperand> formula = cg.getFormula();
			for (CalcGroupFormulaOperand operand : formula) {
				System.out.println("   [" + operand.getOperator() + "]"
						+ operand.getObjId() + ";" + operand.getTypePar()
						+ operand.getParId());
			}
			System.out.println(" Limit groups:");
			List<CalcGroupLimit> limits = cg.getCalcGroupLimits();
			for (CalcGroupLimit cgl : limits) {
				System.out.println(" CalcGroupLimit id:" + cgl.getId()+"["+cgl.getType()+"] ["+cgl.getName()+"] owner:"+cgl.getOwnerId()+" typelimit:"+cgl.getTypeLimitId());
			}
		}
	}

	/**
	 * Загрузка конфига для тестов. если нет конфига - создается дефолотовый -
	 * config.properties
	 */
	private static void loadConfig() {

		String file = "config.properties";
		globalProperties = new Properties();

		try {
			globalProperties.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("Config file [" + file
					+ "] not foud. make new default file");

			globalProperties.setProperty("login", "oao\\derenok_dv");
			globalProperties.setProperty("password", "secret");
			try {
				globalProperties.store(new FileOutputStream(file), null);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void AuthBASIC() {
		// BASIC авторизация
		// http://www.mkyong.com/webservices/jax-ws/application-authentication-with-jax-ws/
		// http://stackoverflow.com/questions/1613212/jax-ws-and-basic-authentication-when-user-names-and-passwords-are-in-a-database
		/******************* UserName & Password ******************************/
		Map<String, Object> req_ctx = ((BindingProvider) port)
				.getRequestContext();
		req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Username", Collections.singletonList(globalProperties
				.getProperty("login")));
		headers.put("Password", Collections.singletonList(globalProperties
				.getProperty("password")));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		/**********************************************************************/
	}

	private static void AuthNTLM() {
		// NTLM авторизация
		// http://stackoverflow.com/questions/4865165/jax-ws-sharepoint-401-unauthorized-ntlm

		String username = globalProperties.getProperty("login");
		String password = globalProperties.getProperty("password");

		NtlmAuthenticator authenticator = new NtlmAuthenticator(username,
				password);
		Authenticator.setDefault(authenticator);

	}

}
