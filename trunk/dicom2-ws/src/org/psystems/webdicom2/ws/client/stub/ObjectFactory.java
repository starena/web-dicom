
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
    private final static QName _GetDCMContent_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getDCMContent");
    private final static QName _GetDCMResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getDCMResponse");
    private final static QName _GetCompliteStudyResultResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getCompliteStudyResultResponse");
    private final static QName _SendDirection_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendDirection");
    private final static QName _GetDCMContentResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getDCMContentResponse");
    private final static QName _GetDCM_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getDCM");
    private final static QName _RemoveDirrectionResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "removeDirrectionResponse");
    private final static QName _SendFinalResult_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendFinalResult");
    private final static QName _SendDirectionResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendDirectionResponse");
    private final static QName _RemoveDirrection_QNAME = new QName("http://ws.webdicom2.psystems.org/", "removeDirrection");
    private final static QName _SendPdfResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPdfResponse");
    private final static QName _GetRISCodesResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getRISCodesResponse");
    private final static QName _SendPhysician_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPhysician");
    private final static QName _SendPhysicianResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendPhysicianResponse");
    private final static QName _SendFinalResultResponse_QNAME = new QName("http://ws.webdicom2.psystems.org/", "sendFinalResultResponse");
    private final static QName _GetCompliteStudyResult_QNAME = new QName("http://ws.webdicom2.psystems.org/", "getCompliteStudyResult");
    private final static QName _GetDCMContentResponseReturn_QNAME = new QName("", "return");
    private final static QName _SendPdfContent_QNAME = new QName("", "content");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.psystems.webdicom2.ws.client.stub
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendPhysicianResponse }
     * 
     */
    public SendPhysicianResponse createSendPhysicianResponse() {
        return new SendPhysicianResponse();
    }

    /**
     * Create an instance of {@link Direction }
     * 
     */
    public Direction createDirection() {
        return new Direction();
    }

    /**
     * Create an instance of {@link GetDCMContentResponse }
     * 
     */
    public GetDCMContentResponse createGetDCMContentResponse() {
        return new GetDCMContentResponse();
    }

    /**
     * Create an instance of {@link RemoveDirrection }
     * 
     */
    public RemoveDirrection createRemoveDirrection() {
        return new RemoveDirrection();
    }

    /**
     * Create an instance of {@link StudyResult }
     * 
     */
    public StudyResult createStudyResult() {
        return new StudyResult();
    }

    /**
     * Create an instance of {@link SendDirectionResponse }
     * 
     */
    public SendDirectionResponse createSendDirectionResponse() {
        return new SendDirectionResponse();
    }

    /**
     * Create an instance of {@link RemoveDirrectionResponse }
     * 
     */
    public RemoveDirrectionResponse createRemoveDirrectionResponse() {
        return new RemoveDirrectionResponse();
    }

    /**
     * Create an instance of {@link GetDCM }
     * 
     */
    public GetDCM createGetDCM() {
        return new GetDCM();
    }

    /**
     * Create an instance of {@link Dcm.Tags }
     * 
     */
    public Dcm.Tags createDcmTags() {
        return new Dcm.Tags();
    }

    /**
     * Create an instance of {@link GetRISCodesResponse }
     * 
     */
    public GetRISCodesResponse createGetRISCodesResponse() {
        return new GetRISCodesResponse();
    }

    /**
     * Create an instance of {@link SendFinalResultResponse }
     * 
     */
    public SendFinalResultResponse createSendFinalResultResponse() {
        return new SendFinalResultResponse();
    }

    /**
     * Create an instance of {@link GetRISCodes }
     * 
     */
    public GetRISCodes createGetRISCodes() {
        return new GetRISCodes();
    }

    /**
     * Create an instance of {@link Dcm.Tags.Entry }
     * 
     */
    public Dcm.Tags.Entry createDcmTagsEntry() {
        return new Dcm.Tags.Entry();
    }

    /**
     * Create an instance of {@link GetCompliteStudyResult }
     * 
     */
    public GetCompliteStudyResult createGetCompliteStudyResult() {
        return new GetCompliteStudyResult();
    }

    /**
     * Create an instance of {@link SendDirection }
     * 
     */
    public SendDirection createSendDirection() {
        return new SendDirection();
    }

    /**
     * Create an instance of {@link GetDCMContent }
     * 
     */
    public GetDCMContent createGetDCMContent() {
        return new GetDCMContent();
    }

    /**
     * Create an instance of {@link SendPhysician }
     * 
     */
    public SendPhysician createSendPhysician() {
        return new SendPhysician();
    }

    /**
     * Create an instance of {@link Dcm }
     * 
     */
    public Dcm createDcm() {
        return new Dcm();
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
     * Create an instance of {@link GetDCMResponse }
     * 
     */
    public GetDCMResponse createGetDCMResponse() {
        return new GetDCMResponse();
    }

    /**
     * Create an instance of {@link SendFinalResult }
     * 
     */
    public SendFinalResult createSendFinalResult() {
        return new SendFinalResult();
    }

    /**
     * Create an instance of {@link GetCompliteStudyResultResponse }
     * 
     */
    public GetCompliteStudyResultResponse createGetCompliteStudyResultResponse() {
        return new GetCompliteStudyResultResponse();
    }

    /**
     * Create an instance of {@link SendPdf }
     * 
     */
    public SendPdf createSendPdf() {
        return new SendPdf();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDCMContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getDCMContent")
    public JAXBElement<GetDCMContent> createGetDCMContent(GetDCMContent value) {
        return new JAXBElement<GetDCMContent>(_GetDCMContent_QNAME, GetDCMContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDCMResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getDCMResponse")
    public JAXBElement<GetDCMResponse> createGetDCMResponse(GetDCMResponse value) {
        return new JAXBElement<GetDCMResponse>(_GetDCMResponse_QNAME, GetDCMResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompliteStudyResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getCompliteStudyResultResponse")
    public JAXBElement<GetCompliteStudyResultResponse> createGetCompliteStudyResultResponse(GetCompliteStudyResultResponse value) {
        return new JAXBElement<GetCompliteStudyResultResponse>(_GetCompliteStudyResultResponse_QNAME, GetCompliteStudyResultResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDCMContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getDCMContentResponse")
    public JAXBElement<GetDCMContentResponse> createGetDCMContentResponse(GetDCMContentResponse value) {
        return new JAXBElement<GetDCMContentResponse>(_GetDCMContentResponse_QNAME, GetDCMContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDCM }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getDCM")
    public JAXBElement<GetDCM> createGetDCM(GetDCM value) {
        return new JAXBElement<GetDCM>(_GetDCM_QNAME, GetDCM.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompliteStudyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.webdicom2.psystems.org/", name = "getCompliteStudyResult")
    public JAXBElement<GetCompliteStudyResult> createGetCompliteStudyResult(GetCompliteStudyResult value) {
        return new JAXBElement<GetCompliteStudyResult>(_GetCompliteStudyResult_QNAME, GetCompliteStudyResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetDCMContentResponse.class)
    public JAXBElement<byte[]> createGetDCMContentResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetDCMContentResponseReturn_QNAME, byte[].class, GetDCMContentResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "content", scope = SendPdf.class)
    public JAXBElement<byte[]> createSendPdfContent(byte[] value) {
        return new JAXBElement<byte[]>(_SendPdfContent_QNAME, byte[].class, SendPdf.class, ((byte[]) value));
    }

}
