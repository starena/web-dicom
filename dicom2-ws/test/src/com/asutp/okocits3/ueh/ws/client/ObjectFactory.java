
package com.asutp.okocits3.ueh.ws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.asutp.okocits3.ueh.ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDataResponse_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "getDataResponse");
    private final static QName _RegisterTaskResponse_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "registerTaskResponse");
    private final static QName _RegisterTask_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "registerTask");
    private final static QName _Confirm_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "confirm");
    private final static QName _SubscribeResponse_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "subscribeResponse");
    private final static QName _Subscribe_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "subscribe");
    private final static QName _ConfirmResponse_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "confirmResponse");
    private final static QName _GetData_QNAME = new QName("http://ws.ueh.okocits3.asutp.com/", "getData");
    private final static QName _OkoParameterValueDouble_QNAME = new QName("", "valueDouble");
    private final static QName _OkoParameterValueDate_QNAME = new QName("", "valueDate");
    private final static QName _OkoParameterValueString_QNAME = new QName("", "valueString");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.asutp.okocits3.ueh.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegisterTask }
     * 
     */
    public RegisterTask createRegisterTask() {
        return new RegisterTask();
    }

    /**
     * Create an instance of {@link GetDataResponse }
     * 
     */
    public GetDataResponse createGetDataResponse() {
        return new GetDataResponse();
    }

    /**
     * Create an instance of {@link ConfirmResponse }
     * 
     */
    public ConfirmResponse createConfirmResponse() {
        return new ConfirmResponse();
    }

    /**
     * Create an instance of {@link RegisterTaskResponse }
     * 
     */
    public RegisterTaskResponse createRegisterTaskResponse() {
        return new RegisterTaskResponse();
    }

    /**
     * Create an instance of {@link Subscribe }
     * 
     */
    public Subscribe createSubscribe() {
        return new Subscribe();
    }

    /**
     * Create an instance of {@link Confirm }
     * 
     */
    public Confirm createConfirm() {
        return new Confirm();
    }

    /**
     * Create an instance of {@link SubscribeResponse }
     * 
     */
    public SubscribeResponse createSubscribeResponse() {
        return new SubscribeResponse();
    }

    /**
     * Create an instance of {@link OkoParameter }
     * 
     */
    public OkoParameter createOkoParameter() {
        return new OkoParameter();
    }

    /**
     * Create an instance of {@link SubscribeItem }
     * 
     */
    public SubscribeItem createSubscribeItem() {
        return new SubscribeItem();
    }

    /**
     * Create an instance of {@link GetData }
     * 
     */
    public GetData createGetData() {
        return new GetData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "getDataResponse")
    public JAXBElement<GetDataResponse> createGetDataResponse(GetDataResponse value) {
        return new JAXBElement<GetDataResponse>(_GetDataResponse_QNAME, GetDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "registerTaskResponse")
    public JAXBElement<RegisterTaskResponse> createRegisterTaskResponse(RegisterTaskResponse value) {
        return new JAXBElement<RegisterTaskResponse>(_RegisterTaskResponse_QNAME, RegisterTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "registerTask")
    public JAXBElement<RegisterTask> createRegisterTask(RegisterTask value) {
        return new JAXBElement<RegisterTask>(_RegisterTask_QNAME, RegisterTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Confirm }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "confirm")
    public JAXBElement<Confirm> createConfirm(Confirm value) {
        return new JAXBElement<Confirm>(_Confirm_QNAME, Confirm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscribeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "subscribeResponse")
    public JAXBElement<SubscribeResponse> createSubscribeResponse(SubscribeResponse value) {
        return new JAXBElement<SubscribeResponse>(_SubscribeResponse_QNAME, SubscribeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Subscribe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "subscribe")
    public JAXBElement<Subscribe> createSubscribe(Subscribe value) {
        return new JAXBElement<Subscribe>(_Subscribe_QNAME, Subscribe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "confirmResponse")
    public JAXBElement<ConfirmResponse> createConfirmResponse(ConfirmResponse value) {
        return new JAXBElement<ConfirmResponse>(_ConfirmResponse_QNAME, ConfirmResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.ueh.okocits3.asutp.com/", name = "getData")
    public JAXBElement<GetData> createGetData(GetData value) {
        return new JAXBElement<GetData>(_GetData_QNAME, GetData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valueDouble", scope = OkoParameter.class)
    public JAXBElement<Double> createOkoParameterValueDouble(Double value) {
        return new JAXBElement<Double>(_OkoParameterValueDouble_QNAME, Double.class, OkoParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valueDate", scope = OkoParameter.class)
    public JAXBElement<XMLGregorianCalendar> createOkoParameterValueDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OkoParameterValueDate_QNAME, XMLGregorianCalendar.class, OkoParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valueString", scope = OkoParameter.class)
    public JAXBElement<String> createOkoParameterValueString(String value) {
        return new JAXBElement<String>(_OkoParameterValueString_QNAME, String.class, OkoParameter.class, value);
    }

}
