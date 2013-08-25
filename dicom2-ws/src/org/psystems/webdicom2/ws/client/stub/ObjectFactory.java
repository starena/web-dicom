
package org.psystems.webdicom2.ws.client.stub;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.psystems.webdicom2.ws.client.stub package. 
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

    private final static QName _SendPdf_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPdf");
    private final static QName _GetRISCodes_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getRISCodes");
    private final static QName _GetStudyResult_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getStudyResult");
    private final static QName _SendDirection_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendDirection");
    private final static QName _RemoveDirrectionResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "removeDirrectionResponse");
    private final static QName _SendFinalResult_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendFinalResult");
    private final static QName _SendDirectionResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendDirectionResponse");
    private final static QName _RemoveDirrection_QNAME = new QName("http://ws.webdicom2.psystems.org/", "removeDirrection");
    private final static QName _SendPdfResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPdfResponse");
    private final static QName _GetStudyResultResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getStudyResultResponse");
    private final static QName _GetRISCodesResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getRISCodesResponse");
    private final static QName _SendPhysician_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPhysician");
    private final static QName _SendPhysicianResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPhysicianResponse");
    private final static QName _SendFinalResultResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendFinalResultResponse");
    private final static QName _SendPdfArg1_QNAME = new QName("", "arg1");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.psystems.webdicom2.ws.client.stub
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RemoveDirrectionResponse }
     * 
     */
    public RemoveDirrectionResponse createRemoveDirrectionResponse() {
        return new RemoveDirrectionResponse();
    }

    /**
     * Create an instance of {@link RemoveDirrection }
     * 
     */
    public RemoveDirrection createRemoveDirrection() {
        return new RemoveDirrection();
    }

    /**
     * Create an instance of {@link SendPdfResponse }
     * 
     */
    public SendPdfResponse createSendPdfResponse() {
        return new SendPdfResponse();
    }

    /**
     * Create an instance of {@link RisCode }
     * 
     */
    public RisCode createRisCode() {
        return new RisCode();
    }

    /**
     * Create an instance of {@link SendFinalResultResponse }
     * 
     */
    public SendFinalResultResponse createSendFinalResultResponse() {
        return new SendFinalResultResponse();
    }

    /**
     * Create an instance of {@link SendFinalResult }
     * 
     */
    public SendFinalResult createSendFinalResult() {
        return new SendFinalResult();
    }

    /**
     * Create an instance of {@link GetRISCodesResponse }
     * 
     */
    public GetRISCodesResponse createGetRISCodesResponse() {
        return new GetRISCodesResponse();
    }

    /**
     * Create an instance of {@link GetStudyResultResponse }
     * 
     */
    public GetStudyResultResponse createGetStudyResultResponse() {
        return new GetStudyResultResponse();
    }

    /**
     * Create an instance of {@link SendPhysician }
     * 
     */
    public SendPhysician createSendPhysician() {
        return new SendPhysician();
    }

    /**
     * Create an instance of {@link Direction }
     * 
     */
    public Direction createDirection() {
        return new Direction();
    }

    /**
     * Create an instance of {@link SendDirectionResponse }
     * 
     */
    public SendDirectionResponse createSendDirectionResponse() {
        return new SendDirectionResponse();
    }

    /**
     * Create an instance of {@link GetStudyResult }
     * 
     */
    public GetStudyResult createGetStudyResult() {
        return new GetStudyResult();
    }

    /**
     * Create an instance of {@link SendPhysicianResponse }
     * 
     */
    public SendPhysicianResponse createSendPhysicianResponse() {
        return new SendPhysicianResponse();
    }

    /**
     * Create an instance of {@link StudyResult }
     * 
     */
    public StudyResult createStudyResult() {
        return new StudyResult();
    }

    /**
     * Create an instance of {@link SendPdf }
     * 
     */
    public SendPdf createSendPdf() {
        return new SendPdf();
    }

    /**
     * Create an instance of {@link GetRISCodes }
     * 
     */
    public GetRISCodes createGetRISCodes() {
        return new GetRISCodes();
    }

    /**
     * Create an instance of {@link SendDirection }
     * 
     */
    public SendDirection createSendDirection() {
        return new SendDirection();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPdf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendPdf")
    public JAXBElement<SendPdf> createSendPdf(SendPdf value) {
        return new JAXBElement<SendPdf>(_SendPdf_QNAME, SendPdf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRISCodes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getRISCodes")
    public JAXBElement<GetRISCodes> createGetRISCodes(GetRISCodes value) {
        return new JAXBElement<GetRISCodes>(_GetRISCodes_QNAME, GetRISCodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getStudyResult")
    public JAXBElement<GetStudyResult> createGetStudyResult(GetStudyResult value) {
        return new JAXBElement<GetStudyResult>(_GetStudyResult_QNAME, GetStudyResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendDirection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendDirection")
    public JAXBElement<SendDirection> createSendDirection(SendDirection value) {
        return new JAXBElement<SendDirection>(_SendDirection_QNAME, SendDirection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDirrectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "removeDirrectionResponse")
    public JAXBElement<RemoveDirrectionResponse> createRemoveDirrectionResponse(RemoveDirrectionResponse value) {
        return new JAXBElement<RemoveDirrectionResponse>(_RemoveDirrectionResponse_QNAME, RemoveDirrectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendFinalResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendFinalResult")
    public JAXBElement<SendFinalResult> createSendFinalResult(SendFinalResult value) {
        return new JAXBElement<SendFinalResult>(_SendFinalResult_QNAME, SendFinalResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendDirectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendDirectionResponse")
    public JAXBElement<SendDirectionResponse> createSendDirectionResponse(SendDirectionResponse value) {
        return new JAXBElement<SendDirectionResponse>(_SendDirectionResponse_QNAME, SendDirectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDirrection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "removeDirrection")
    public JAXBElement<RemoveDirrection> createRemoveDirrection(RemoveDirrection value) {
        return new JAXBElement<RemoveDirrection>(_RemoveDirrection_QNAME, RemoveDirrection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPdfResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendPdfResponse")
    public JAXBElement<SendPdfResponse> createSendPdfResponse(SendPdfResponse value) {
        return new JAXBElement<SendPdfResponse>(_SendPdfResponse_QNAME, SendPdfResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getStudyResultResponse")
    public JAXBElement<GetStudyResultResponse> createGetStudyResultResponse(GetStudyResultResponse value) {
        return new JAXBElement<GetStudyResultResponse>(_GetStudyResultResponse_QNAME, GetStudyResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRISCodesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getRISCodesResponse")
    public JAXBElement<GetRISCodesResponse> createGetRISCodesResponse(GetRISCodesResponse value) {
        return new JAXBElement<GetRISCodesResponse>(_GetRISCodesResponse_QNAME, GetRISCodesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPhysician }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendPhysician")
    public JAXBElement<SendPhysician> createSendPhysician(SendPhysician value) {
        return new JAXBElement<SendPhysician>(_SendPhysician_QNAME, SendPhysician.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPhysicianResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendPhysicianResponse")
    public JAXBElement<SendPhysicianResponse> createSendPhysicianResponse(SendPhysicianResponse value) {
        return new JAXBElement<SendPhysicianResponse>(_SendPhysicianResponse_QNAME, SendPhysicianResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendFinalResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "sendFinalResultResponse")
    public JAXBElement<SendFinalResultResponse> createSendFinalResultResponse(SendFinalResultResponse value) {
        return new JAXBElement<SendFinalResultResponse>(_SendFinalResultResponse_QNAME, SendFinalResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = SendPdf.class)
    public JAXBElement<byte[]> createSendPdfArg1(byte[] value) {
        return new JAXBElement<byte[]>(_SendPdfArg1_QNAME, byte[].class, SendPdf.class, ((byte[]) value));
    }

}
