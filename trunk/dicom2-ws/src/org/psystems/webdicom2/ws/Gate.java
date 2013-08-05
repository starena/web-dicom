package org.psystems.webdicom2.ws;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.jws.soap.SOAPBinding.Style;

import org.psystems.webdicom2.ws.dto.Direction;

/**
 * @author dima_d
 * 
 *         http://jax-ws.java.net/
 * 
 *         Типы данных:
 *         http://download.oracle.com/docs/cd/E12840_01/wls/docs103/
 *         webserv/data_types.html
 * 
 *         аттачменты:
 *         http://www.mkyong.com/webservices/jax-ws/jax-ws-attachment-with-mtom/
 */
@javax.xml.ws.soap.MTOM
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class Gate {

	public static Logger logger = Logger.getLogger(Gate.class.getName());

	@Resource
	private WebServiceContext context;

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public String makeDirection(Direction drn) {
		return "12345";
	}

}
