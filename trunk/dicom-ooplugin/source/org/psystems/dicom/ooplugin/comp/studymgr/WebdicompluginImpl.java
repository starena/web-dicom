package org.psystems.dicom.ooplugin.comp.studymgr;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XRefreshable;
import com.sun.star.beans.NamedValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;


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

	/* (non-Javadoc)
	 * @see org.psystems.dicom.ooplugin.studymgr.XDicomplugin#updateDocument(java.lang.String, com.sun.star.text.XTextDocument)
	 */
	@Override
	public String updateDocument(String docName, XTextDocument docObj) {
		
		//Обновление тегов
		
		String result = null;
		
		HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://10.130.1.100/"); 

        System.out.println("executing request " + httpget.getURI());

        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception! "+e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Exception! "+e;
			
		}
		result = responseBody;
        

        // When HttpClient instance is no longer needed, 
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();        
		
		//TODO Сделать регулярку для вычленения ID исследования
		String studyId = docName;
		
		HashMap<String, String> variableMap = new HashMap<String, String>();
		variableMap.put("PatientName", "DDV");
		variableMap.put("StudyUID", "12345");
		
		

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
		
        return "!!!! TIS DOC IS : {"+xTextFieldsSupplier+"} URL={"+docName+"} result="+result;
	}

	/* (non-Javadoc)
	 * @see org.psystems.dicom.ooplugin.studymgr.XDicomplugin#sendDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.sun.star.text.XTextDocument)
	 */
	@Override
	public String sendDocument(String url, String login, String pwd,
			String pdffile, XTextDocument docObj) {
		// Отправка PDF
		return null;
	}

}
