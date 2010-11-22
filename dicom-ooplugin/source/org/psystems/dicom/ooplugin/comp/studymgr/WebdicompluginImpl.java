package org.psystems.dicom.ooplugin.comp.studymgr;

import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
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

}
