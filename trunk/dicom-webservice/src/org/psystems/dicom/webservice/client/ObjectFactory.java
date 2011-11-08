
package org.psystems.dicom.webservice.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.psystems.dicom.webservice.client package. 
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

    private final static QName _MakeDirectionResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "makeDirectionResponse");
    private final static QName _GetDirectionBydirectionIdResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionBydirectionIdResponse");
    private final static QName _QueryDirectionResp_QNAME = new QName("http://webservice.dicom.psystems.org", "queryDirectionResp");
    private final static QName _GetStudyProtocolsResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "getStudyProtocolsResponse");
    private final static QName _GetStudyProtocols_QNAME = new QName("http://webservice.dicom.psystems.org/", "getStudyProtocols");
    private final static QName _GetDirectionBydirectionId_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionBydirectionId");
    private final static QName _QueryStudyResp_QNAME = new QName("http://webservice.dicom.psystems.org", "queryStudyResp");
    private final static QName _GetDirectionByIdResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionByIdResponse");
    private final static QName _GetDirectionById_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionById");
    private final static QName _QueryDirectionReq_QNAME = new QName("http://webservice.dicom.psystems.org", "queryDirectionReq");
    private final static QName _GetStudyImages_QNAME = new QName("http://webservice.dicom.psystems.org/", "getStudyImages");
    private final static QName _MakeDirection_QNAME = new QName("http://webservice.dicom.psystems.org/", "makeDirection");
    private final static QName _GetStudyImagesResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "getStudyImagesResponse");
    private final static QName _QueryStudyReq_QNAME = new QName("http://webservice.dicom.psystems.org", "queryStudyReq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.psystems.dicom.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Diagnosis }
     * 
     */
    public Diagnosis createDiagnosis() {
        return new Diagnosis();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link QueryDirection }
     * 
     */
    public QueryDirection createQueryDirection() {
        return new QueryDirection();
    }

    /**
     * Create an instance of {@link QueryStudyReq }
     * 
     */
    public QueryStudyReq createQueryStudyReq() {
        return new QueryStudyReq();
    }

    /**
     * Create an instance of {@link GetDirectionBydirectionIdResponse }
     * 
     */
    public GetDirectionBydirectionIdResponse createGetDirectionBydirectionIdResponse() {
        return new GetDirectionBydirectionIdResponse();
    }

    /**
     * Create an instance of {@link GetDirectionById }
     * 
     */
    public GetDirectionById createGetDirectionById() {
        return new GetDirectionById();
    }

    /**
     * Create an instance of {@link QueryDirectionReq }
     * 
     */
    public QueryDirectionReq createQueryDirectionReq() {
        return new QueryDirectionReq();
    }

    /**
     * Create an instance of {@link Direction }
     * 
     */
    public Direction createDirection() {
        return new Direction();
    }

    /**
     * Create an instance of {@link Employee }
     * 
     */
    public Employee createEmployee() {
        return new Employee();
    }

    /**
     * Create an instance of {@link GetDirectionBydirectionId }
     * 
     */
    public GetDirectionBydirectionId createGetDirectionBydirectionId() {
        return new GetDirectionBydirectionId();
    }

    /**
     * Create an instance of {@link GetStudyProtocolsResponse }
     * 
     */
    public GetStudyProtocolsResponse createGetStudyProtocolsResponse() {
        return new GetStudyProtocolsResponse();
    }

    /**
     * Create an instance of {@link GetStudyImages }
     * 
     */
    public GetStudyImages createGetStudyImages() {
        return new GetStudyImages();
    }

    /**
     * Create an instance of {@link QueryStudy }
     * 
     */
    public QueryStudy createQueryStudy() {
        return new QueryStudy();
    }

    /**
     * Create an instance of {@link GetStudyImagesResponse }
     * 
     */
    public GetStudyImagesResponse createGetStudyImagesResponse() {
        return new GetStudyImagesResponse();
    }

    /**
     * Create an instance of {@link Patient }
     * 
     */
    public Patient createPatient() {
        return new Patient();
    }

    /**
     * Create an instance of {@link MakeDirectionResponse }
     * 
     */
    public MakeDirectionResponse createMakeDirectionResponse() {
        return new MakeDirectionResponse();
    }

    /**
     * Create an instance of {@link MakeDirection }
     * 
     */
    public MakeDirection createMakeDirection() {
        return new MakeDirection();
    }

    /**
     * Create an instance of {@link ManufacturerDevice }
     * 
     */
    public ManufacturerDevice createManufacturerDevice() {
        return new ManufacturerDevice();
    }

    /**
     * Create an instance of {@link QueryStudyResp }
     * 
     */
    public QueryStudyResp createQueryStudyResp() {
        return new QueryStudyResp();
    }

    /**
     * Create an instance of {@link GetDirectionByIdResponse }
     * 
     */
    public GetDirectionByIdResponse createGetDirectionByIdResponse() {
        return new GetDirectionByIdResponse();
    }

    /**
     * Create an instance of {@link QueryDirectionResp }
     * 
     */
    public QueryDirectionResp createQueryDirectionResp() {
        return new QueryDirectionResp();
    }

    /**
     * Create an instance of {@link GetStudyProtocols }
     * 
     */
    public GetStudyProtocols createGetStudyProtocols() {
        return new GetStudyProtocols();
    }

    /**
     * Create an instance of {@link Study }
     * 
     */
    public Study createStudy() {
        return new Study();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MakeDirectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "makeDirectionResponse")
    public JAXBElement<MakeDirectionResponse> createMakeDirectionResponse(MakeDirectionResponse value) {
        return new JAXBElement<MakeDirectionResponse>(_MakeDirectionResponse_QNAME, MakeDirectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectionBydirectionIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getDirectionBydirectionIdResponse")
    public JAXBElement<GetDirectionBydirectionIdResponse> createGetDirectionBydirectionIdResponse(GetDirectionBydirectionIdResponse value) {
        return new JAXBElement<GetDirectionBydirectionIdResponse>(_GetDirectionBydirectionIdResponse_QNAME, GetDirectionBydirectionIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryDirectionResp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org", name = "queryDirectionResp")
    public JAXBElement<QueryDirectionResp> createQueryDirectionResp(QueryDirectionResp value) {
        return new JAXBElement<QueryDirectionResp>(_QueryDirectionResp_QNAME, QueryDirectionResp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyProtocolsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getStudyProtocolsResponse")
    public JAXBElement<GetStudyProtocolsResponse> createGetStudyProtocolsResponse(GetStudyProtocolsResponse value) {
        return new JAXBElement<GetStudyProtocolsResponse>(_GetStudyProtocolsResponse_QNAME, GetStudyProtocolsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyProtocols }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getStudyProtocols")
    public JAXBElement<GetStudyProtocols> createGetStudyProtocols(GetStudyProtocols value) {
        return new JAXBElement<GetStudyProtocols>(_GetStudyProtocols_QNAME, GetStudyProtocols.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectionBydirectionId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getDirectionBydirectionId")
    public JAXBElement<GetDirectionBydirectionId> createGetDirectionBydirectionId(GetDirectionBydirectionId value) {
        return new JAXBElement<GetDirectionBydirectionId>(_GetDirectionBydirectionId_QNAME, GetDirectionBydirectionId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStudyResp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org", name = "queryStudyResp")
    public JAXBElement<QueryStudyResp> createQueryStudyResp(QueryStudyResp value) {
        return new JAXBElement<QueryStudyResp>(_QueryStudyResp_QNAME, QueryStudyResp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectionByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getDirectionByIdResponse")
    public JAXBElement<GetDirectionByIdResponse> createGetDirectionByIdResponse(GetDirectionByIdResponse value) {
        return new JAXBElement<GetDirectionByIdResponse>(_GetDirectionByIdResponse_QNAME, GetDirectionByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectionById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getDirectionById")
    public JAXBElement<GetDirectionById> createGetDirectionById(GetDirectionById value) {
        return new JAXBElement<GetDirectionById>(_GetDirectionById_QNAME, GetDirectionById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryDirectionReq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org", name = "queryDirectionReq")
    public JAXBElement<QueryDirectionReq> createQueryDirectionReq(QueryDirectionReq value) {
        return new JAXBElement<QueryDirectionReq>(_QueryDirectionReq_QNAME, QueryDirectionReq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyImages }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getStudyImages")
    public JAXBElement<GetStudyImages> createGetStudyImages(GetStudyImages value) {
        return new JAXBElement<GetStudyImages>(_GetStudyImages_QNAME, GetStudyImages.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MakeDirection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "makeDirection")
    public JAXBElement<MakeDirection> createMakeDirection(MakeDirection value) {
        return new JAXBElement<MakeDirection>(_MakeDirection_QNAME, MakeDirection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStudyImagesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getStudyImagesResponse")
    public JAXBElement<GetStudyImagesResponse> createGetStudyImagesResponse(GetStudyImagesResponse value) {
        return new JAXBElement<GetStudyImagesResponse>(_GetStudyImagesResponse_QNAME, GetStudyImagesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStudyReq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org", name = "queryStudyReq")
    public JAXBElement<QueryStudyReq> createQueryStudyReq(QueryStudyReq value) {
        return new JAXBElement<QueryStudyReq>(_QueryStudyReq_QNAME, QueryStudyReq.class, null, value);
    }

}
