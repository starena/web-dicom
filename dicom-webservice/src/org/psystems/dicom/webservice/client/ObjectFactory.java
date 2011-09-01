
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
    private final static QName _QueryStudiesResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "queryStudiesResponse");
    private final static QName _GetDirectionBydirectionId_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionBydirectionId");
    private final static QName _GetDirectionById_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionById");
    private final static QName _GetDirectionByIdResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "getDirectionByIdResponse");
    private final static QName _QueryDirectionsResponse_QNAME = new QName("http://webservice.dicom.psystems.org/", "queryDirectionsResponse");
    private final static QName _QueryStudies_QNAME = new QName("http://webservice.dicom.psystems.org/", "queryStudies");
    private final static QName _QueryDirections_QNAME = new QName("http://webservice.dicom.psystems.org/", "queryDirections");
    private final static QName _MakeDirection_QNAME = new QName("http://webservice.dicom.psystems.org/", "makeDirection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.psystems.dicom.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link QueryDirections }
     * 
     */
    public QueryDirections createQueryDirections() {
        return new QueryDirections();
    }

    /**
     * Create an instance of {@link GetDirectionById }
     * 
     */
    public GetDirectionById createGetDirectionById() {
        return new GetDirectionById();
    }

    /**
     * Create an instance of {@link MakeDirection }
     * 
     */
    public MakeDirection createMakeDirection() {
        return new MakeDirection();
    }

    /**
     * Create an instance of {@link Employee }
     * 
     */
    public Employee createEmployee() {
        return new Employee();
    }

    /**
     * Create an instance of {@link Patient }
     * 
     */
    public Patient createPatient() {
        return new Patient();
    }

    /**
     * Create an instance of {@link Direction }
     * 
     */
    public Direction createDirection() {
        return new Direction();
    }

    /**
     * Create an instance of {@link QueryDirection }
     * 
     */
    public QueryDirection createQueryDirection() {
        return new QueryDirection();
    }

    /**
     * Create an instance of {@link ManufacturerDevice }
     * 
     */
    public ManufacturerDevice createManufacturerDevice() {
        return new ManufacturerDevice();
    }

    /**
     * Create an instance of {@link QueryStudiesResponse }
     * 
     */
    public QueryStudiesResponse createQueryStudiesResponse() {
        return new QueryStudiesResponse();
    }

    /**
     * Create an instance of {@link GetDirectionBydirectionIdResponse }
     * 
     */
    public GetDirectionBydirectionIdResponse createGetDirectionBydirectionIdResponse() {
        return new GetDirectionBydirectionIdResponse();
    }

    /**
     * Create an instance of {@link Diagnosis }
     * 
     */
    public Diagnosis createDiagnosis() {
        return new Diagnosis();
    }

    /**
     * Create an instance of {@link Study }
     * 
     */
    public Study createStudy() {
        return new Study();
    }

    /**
     * Create an instance of {@link QueryDirectionsResponse }
     * 
     */
    public QueryDirectionsResponse createQueryDirectionsResponse() {
        return new QueryDirectionsResponse();
    }

    /**
     * Create an instance of {@link GetDirectionByIdResponse }
     * 
     */
    public GetDirectionByIdResponse createGetDirectionByIdResponse() {
        return new GetDirectionByIdResponse();
    }

    /**
     * Create an instance of {@link MakeDirectionResponse }
     * 
     */
    public MakeDirectionResponse createMakeDirectionResponse() {
        return new MakeDirectionResponse();
    }

    /**
     * Create an instance of {@link GetDirectionBydirectionId }
     * 
     */
    public GetDirectionBydirectionId createGetDirectionBydirectionId() {
        return new GetDirectionBydirectionId();
    }

    /**
     * Create an instance of {@link QueryStudy }
     * 
     */
    public QueryStudy createQueryStudy() {
        return new QueryStudy();
    }

    /**
     * Create an instance of {@link QueryStudies }
     * 
     */
    public QueryStudies createQueryStudies() {
        return new QueryStudies();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStudiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "queryStudiesResponse")
    public JAXBElement<QueryStudiesResponse> createQueryStudiesResponse(QueryStudiesResponse value) {
        return new JAXBElement<QueryStudiesResponse>(_QueryStudiesResponse_QNAME, QueryStudiesResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectionById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "getDirectionById")
    public JAXBElement<GetDirectionById> createGetDirectionById(GetDirectionById value) {
        return new JAXBElement<GetDirectionById>(_GetDirectionById_QNAME, GetDirectionById.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryDirectionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "queryDirectionsResponse")
    public JAXBElement<QueryDirectionsResponse> createQueryDirectionsResponse(QueryDirectionsResponse value) {
        return new JAXBElement<QueryDirectionsResponse>(_QueryDirectionsResponse_QNAME, QueryDirectionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStudies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "queryStudies")
    public JAXBElement<QueryStudies> createQueryStudies(QueryStudies value) {
        return new JAXBElement<QueryStudies>(_QueryStudies_QNAME, QueryStudies.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryDirections }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "queryDirections")
    public JAXBElement<QueryDirections> createQueryDirections(QueryDirections value) {
        return new JAXBElement<QueryDirections>(_QueryDirections_QNAME, QueryDirections.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MakeDirection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dicom.psystems.org/", name = "makeDirection")
    public JAXBElement<MakeDirection> createMakeDirection(MakeDirection value) {
        return new JAXBElement<MakeDirection>(_MakeDirection_QNAME, MakeDirection.class, null, value);
    }

}
