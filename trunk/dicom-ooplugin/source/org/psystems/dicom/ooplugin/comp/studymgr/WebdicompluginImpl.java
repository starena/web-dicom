package org.psystems.dicom.ooplugin.comp.studymgr;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XRefreshable;


public final class WebdicompluginImpl extends WeakBase
   implements org.psystems.dicom.ooplugin.studymgr.XDicomplugin,
              com.sun.star.lang.XServiceInfo
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = WebdicompluginImpl.class.getName();
    private static final String[] m_serviceNames = {
        "org.psystems.dicom.ooplugin.studymgr.Webdicomplugin" };


    public WebdicompluginImpl( XComponentContext context )
    {
        m_xContext = context;
    };

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(WebdicompluginImpl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // org.psystems.dicom.ooplugin.studymgr.XDicomplugin:
    public String getPORT()
    {
        return new String("http://localhost/blablabla/");
    }

    public void setPORT(String the_value)
    {

    }

    public String getStudy(String url)
    {
        // TODO: Exchange the default return implementation for "getStudy" !!!
        // NOTE: Default initialized polymorphic structs can cause problems
        // because of missing default initialization of primitive types of
        // some C++ compilers or different Any initialization in Java and C++
        // polymorphic structs.
   
    	
//    	Sub testDicomPlugin
//    		oDicomPlugin = createUnoService( "org.psystems.dicom.ooplugin.studymgr.Webdicomplugin" )
//    		print oDicomPlugin.getStudy( "test" );
//    		print oDicomPlugin.PORT( "port" );
//    	End Sub
    
        return new String("get взяли: url="+url);
    }

    public void sendStudy(String url, String data)
    {
        // TODO: Insert your implementation for "sendStudy" here.
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

	/**
	 * Парсер исследования
	 * @param s
	 * @return
	 */
	public static HashMap<String, String> parse(String s) {

		HashMap<String, String> result = new HashMap<String, String>();

		String[] strs = s.split("[\n|\r]");
		String curS = null;
		String tag = null;
		for (String string : strs) {
			Matcher matcher = Pattern.compile("^###(.*)###$").matcher(string);
			if (matcher.matches()) {
				if (tag != null) {
					result.put(tag, curS);
				}
				curS = null;// пошел новый тег
				tag = matcher.group(1);
			} else {
				if (curS != null)
					curS += string;
				else
					curS = string;
			}
		}
		return result;
	}
	
	
	/**
	 * Определение ID исследования по URL документа
	 * @param url
	 * @return
	 */
	public static String getIdFormURL(String url) {

		Matcher matcher = Pattern.compile("^.*[\\|/](\\d+)\\.odt$")
				.matcher(url);
		String studyId = null;
		if (matcher.matches()) {
			studyId = matcher.group(1);
		} else {

			matcher = Pattern.compile("^.*[\\|/](\\d+)-(\\d+)\\.odt$").matcher(
					url);
			if (matcher.matches()) {
				studyId = matcher.group(1);
				String prefix = matcher.group(2);
			}
		}

		return studyId;

	}
	
	public static HashMap<String, String> getConfiguration(String config) {
		
//		String cfg = "dicomuser:dicom@https://proxy.gp1.psystems.org:38081/browser.01";
		Matcher matcher = Pattern.compile("^(.*)\\:(.*)\\@(.*)\\://(.*)\\:(.*)/(.*)$").matcher(config);
		HashMap<String, String> result = new HashMap<String, String>();
		if (matcher.matches()) {
			
			result.put("login", matcher.group(1));
			result.put("password", matcher.group(2));
			result.put("protocol", matcher.group(3));
			result.put("host", matcher.group(4));
			result.put("port", matcher.group(5));
			result.put("url", matcher.group(6));
			
		}
		return result;
		
	}
	
	public static String getConfigURL(HashMap<String, String> cfg) {
		String s = cfg.get("protocol")+"://"+cfg.get("host")+":"+cfg.get("port");
		if(cfg.get("url")==null || cfg.get("url").equals("")) {
			
		}else {
			s += "/"+cfg.get("url");
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see org.psystems.dicom.ooplugin.studymgr.XDicomplugin#updateDocument(java.lang.String, com.sun.star.text.XTextDocument)
	 * 
	 * Обновление тегов
	 * 
	 */
	@Override
	public String updateDocument(String docName, String config, XTextDocument docObj) {
		
		//TODO сделать проброс эксепшинов
//		https://proxy.gp1.psystems.org:38081/browser.01/ootmpl/ES/issled_n_1.odt?id=8402
		
		//TODO Убрать жесткие пути
//		System.setProperty("javax.net.ssl.keyStore", "C:\\temp\\111\\client.jks");
//		System.setProperty("javax.net.ssl.keyStorePassword", "derenok");
//		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
//		System.setProperty("javax.net.ssl.trustStore", "C:\\temp\\111\\client.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "derenok");
		
		String studyId = null, studyType = null;
		

		HashMap<String, String> cfg = getConfiguration(config);

		String login = cfg.get("login");
		String password = cfg.get("password");
		String host = getConfigURL(cfg);

		studyId = getIdFormURL(docName);

		host += "/oostudy/" + studyId;
		
//		if(true) return "URL=[" + host + "];" + login + ";" + password+";"+docName+";"+studyId+";"+studyType;
		
		String result = null;
		
		HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(host); 

        System.out.println("executing request " + httpget.getURI());

        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " Exception! "+e + "host=" + host;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " Exception! "+e;
			
		}finally {

	        // When HttpClient instance is no longer needed, 
	        // shut down the connection manager to ensure
	        // immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		result = responseBody;
        
       
		
		//Парсим ответ:
		HashMap<String, String> variableMap = new HashMap<String, String>();
		try {
			variableMap = parse(result.trim());
		} catch(Exception e) {
			return "Exception!!! "+e;
		}
		
//		variableMap.put("PatientName", "DDV");
//		variableMap.put("StudyUID", "12345");
//		variableMap.put("PATIENT_NAME", "TESTNAME");
		
		

		XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime
		.queryInterface(XTextFieldsSupplier.class, docObj);
		
		// Создадим перечисление всех полей документа
		XEnumerationAccess xEnumerationAccess = xTextFieldsSupplier
				.getTextFields();
		
		
		
		XEnumeration xTextFieldsEnumeration = xEnumerationAccess
				.createEnumeration();
		XRefreshable xRefreshable = (XRefreshable) UnoRuntime
				.queryInterface(XRefreshable.class, xEnumerationAccess);

		
		try {
		while (xTextFieldsEnumeration.hasMoreElements()) {
			Object service = xTextFieldsEnumeration.nextElement();
			XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime
					.queryInterface(XServiceInfo.class, service);

			if (xServiceInfo
					.supportsService("com.sun.star.text.TextField.SetExpression")) {
				XPropertySet xPropertySet = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, service);
				String name = (String) xPropertySet
						.getPropertyValue("VariableName");
				Object content = variableMap.get(name);
				xPropertySet.setPropertyValue("SubType", new Short(
						com.sun.star.text.SetVariableType.STRING));
				xPropertySet.setPropertyValue("Content",
						content == null ? " " : content.toString());
				xPropertySet.setPropertyValue("IsVisible", true);
			}
		}
		xRefreshable.refresh();
		} catch (Exception ex) {
			return "Exception! "+ex;
		}
		
        return "result=["+result+"]";
	}

	/* (non-Javadoc)
	 * @see org.psystems.dicom.ooplugin.studymgr.XDicomplugin#sendDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.sun.star.text.XTextDocument)
	 */
	@Override
	public String sendDocument(String config,
			String pdffile, XTextDocument docObj) {
		// Отправка PDF
		
		//TODO сделать проброс эксепшинов
		String resultText = "";
		
		String studyId = null, studyType = null;;
		
	
		HashMap<String, String> cfg = getConfiguration(config);

		String login = cfg.get("login");
		String password = cfg.get("password");
		String host = getConfigURL(cfg);
		
		//TODO сделать проброс эксепшинов
//		https://proxy.gp1.psystems.org:38081/browser.01/ootmpl/ES/issled_n_1.odt?id=8402
		
//		System.setProperty("javax.net.ssl.keyStore", "C:\\temp\\111\\client.jks");
//		System.setProperty("javax.net.ssl.keyStorePassword", "derenok");
//		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
//		System.setProperty("javax.net.ssl.trustStore", "C:\\temp\\111\\client.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "derenok");
		
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		//авторизвция
		 httpclient.getCredentialsProvider().setCredentials(
				 //Для тестирования можно забить "localhost"
				 new AuthScope(cfg.get("host"), Integer.valueOf(cfg.get("port"))),
//				 new AuthScope(host, 38081),
	               // new AuthScope("localhost", 443), 
	                new UsernamePasswordCredentials(login, password));
	        
	    
		 
	    HttpPost httppost = new HttpPost(host+"/newstudy/upload");
	    File file = new File(pdffile);
	    
	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(file, "application/pdf");
	    mpEntity.addPart("upload", cbFile);
	    
	    try {
	    	
	    	XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime
			.queryInterface(XTextFieldsSupplier.class, docObj);
	    	
			// Создадим перечисление всех полей документа
			XEnumerationAccess xEnumerationAccess = xTextFieldsSupplier
					.getTextFields();
			XEnumeration xTextFieldsEnumeration = xEnumerationAccess
					.createEnumeration();

			mpEntity.addPart("content_type", new StringBody("application/pdf", Charset.forName("UTF-8")));
			//PATIENT_NAME
			mpEntity.addPart("00100010", new StringBody("DDVTEST333", Charset.forName("UTF-8")));
			
			
			while (xTextFieldsEnumeration.hasMoreElements()) {
				Object service = xTextFieldsEnumeration.nextElement();
				XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime
						.queryInterface(XServiceInfo.class, service);

				if (xServiceInfo.supportsService("com.sun.star.text.TextField.SetExpression")) {
					XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, service);
					
					String name = (String) xPropertySet.getPropertyValue("VariableName");
					String value = (String) xPropertySet.getPropertyValue("Content");
					
					mpEntity.addPart(name, new StringBody(value, Charset.forName("UTF-8")));
					
//					xPropertySet.setPropertyValue("SubType", new Short(
//							com.sun.star.text.SetVariableType.STRING));
//					xPropertySet.setPropertyValue("Content",
//							content == null ? " " : content.toString());
//					xPropertySet.setPropertyValue("IsVisible", true);
				}
			}
			
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception!!! "+e;
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception!!! "+e;
		} catch (WrappedTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception!!! "+e;
		} catch (UnknownPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception!!! "+e;
		}


	    httppost.setEntity(mpEntity);
	    System.out.println("executing request " + httppost.getRequestLine()); 
		 
		 
		 
	    
	    System.out.println("executing request " + httppost.getRequestLine());
	    
	    HttpResponse response;
		try {
			response = httpclient.execute(httppost);
		
	    HttpEntity resEntity = response.getEntity();

	    System.out.println(response.getStatusLine());
	    
	    if (resEntity != null) {
	    	resultText = EntityUtils.toString(resEntity);
	      System.out.println(resultText);
	    }
	    if (resEntity != null) {
	      resEntity.consumeContent();
	    }

	    httpclient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception! "+e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception! "+e;
		}
        
		
		return "["+response.getStatusLine().getStatusCode()+"]\n"+resultText;
	}
	
	   /**
	 * @author dima_d
	 * 
	 * Класс для аутентификации
	 *
	 */
	static class PreemptiveAuth implements HttpRequestInterceptor {

	        public void process(
	                final HttpRequest request, 
	                final HttpContext context) throws HttpException, IOException {
	            
	            AuthState authState = (AuthState) context.getAttribute(
	                    ClientContext.TARGET_AUTH_STATE);
	            
	            // If no auth scheme avaialble yet, try to initialize it preemptively
	            if (authState.getAuthScheme() == null) {
	                AuthScheme authScheme = (AuthScheme) context.getAttribute(
	                        "preemptive-auth");
	                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
	                        ClientContext.CREDS_PROVIDER);
	                HttpHost targetHost = (HttpHost) context.getAttribute(
	                        ExecutionContext.HTTP_TARGET_HOST);
	                if (authScheme != null) {
	                    Credentials creds = credsProvider.getCredentials(
	                            new AuthScope(
	                                    targetHost.getHostName(), 
	                                    targetHost.getPort()));
	                    if (creds == null) {
	                        throw new HttpException("No credentials for preemptive authentication");
	                    }
	                    authState.setAuthScheme(authScheme);
	                    authState.setCredentials(creds);
	                }
	            }
	            
	        }
	        
	    }


}
