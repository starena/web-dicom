package org.psystems.dicom.pdf;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class PdfTesterLauncher {

    static Logger logger = Logger.getLogger(PdfTesterLauncher.class);


    static String resource;
    static String resourceCommon;

    static String logException(Throwable e) {
	String marker = Thread.currentThread().getId() + "_" + Math.random() + " " + new Date();
	String stack = getExceptionString(e);
	logger.warn("DYNASERVER ERROR [" + marker + "] " + e + " stack trace:\n" + stack);
	// System.err.println("Portal Error ["+marker+"] "+e.getMessage()+" stack:\n"+stack);
	// return new DefaultGWTRPCException(marker,msg,e,stack);
	return "[" + marker + "] " + e.getMessage();
    }

    static String getExceptionString(Throwable e) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	pw.flush();
	pw.close();
	return sw.toString();
    }

    public static void main(String[] args) throws Exception {

	
	int jettyport = Integer.valueOf(args[0]).intValue();// 8080
//	String host = args[1];// "10.46.12.38"
//	int port = Integer.valueOf(args[2]).intValue();// 5020
//	String protocol = args[3];// OCM.SOI3
//	resource = args[4];// "resources"
//	resourceCommon = resource + "/common";
	// final String project = args[5];//"test"



	// Запуск jetty
//	System.setProperty("DEBUG", "true");
	Server server = new Server(jettyport);

	// Логгирование
	RequestLogHandler requestLogHandler = new RequestLogHandler();
	NCSARequestLog requestLog = new NCSARequestLog("logs/jetty-yyyy_mm_dd.request.log");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLogHandler.setRequestLog(requestLog);
        
	
	//Статичные файлы
	ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase("distrib/www");
        
        ContextHandler resourceContextHandler = new ContextHandler("/www");
        resourceContextHandler.setHandler(resource_handler);
        
	
	//Сервлеты
	ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
	servletContext.setContextPath("/");
	servletContext.addServlet(new ServletHolder(new ManagePdfServlet()), "/pdf/*");

	//JSP
	ServletContextHandler jspServletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
	jspServletContext.setResourceBase("distrib/www");
	jspServletContext.setContextPath("/www");
	jspServletContext.addServlet(new ServletHolder(new org.apache.jasper.servlet.JspServlet()), "*.jsp");
	
	//Приложения
//	WebAppContext webappConext = new WebAppContext();
//	webappConext.setContextPath("/manage");
//	webappConext.setWar("manage.war");

	HandlerCollection handlers = new HandlerCollection();
	ContextHandlerCollection contexts = new ContextHandlerCollection();
	contexts.setHandlers(new Handler[] {  servletContext, jspServletContext, resourceContextHandler, /*webappConext*/ });
	handlers.setHandlers(new Handler[] { contexts,  requestLogHandler });
	server.setHandler(handlers);

//	server.setHandler(context);
//	server.setHandler(webapp);

	server.start();
	
	
	
	server.join();	
	

    }

  
    
}