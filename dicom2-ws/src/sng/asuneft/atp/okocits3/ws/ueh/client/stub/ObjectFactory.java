
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sng.asuneft.atp.okocits3.ws.ueh.client.stub package. 
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

    private final static QName _DeleteCalcGroup_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "deleteCalcGroup");
    private final static QName _DeleteCalcGroupResponse_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "deleteCalcGroupResponse");
    private final static QName _SetLimitsResponse_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "setLimitsResponse");
    private final static QName _NewCalcGroupResponse_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "newCalcGroupResponse");
    private final static QName _GetCalcGroupsResponse_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "getCalcGroupsResponse");
    private final static QName _GetCalcGroups_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "getCalcGroups");
    private final static QName _SetLimits_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "setLimits");
    private final static QName _NewCalcGroup_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "newCalcGroup");
    private final static QName _GetCalcGroup_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "getCalcGroup");
    private final static QName _GetCalcGroupResponse_QNAME = new QName("http://ueh.ws.okocits3.atp.asuneft.sng/", "getCalcGroupResponse");
    private final static QName _CalcGroupRequestFilterId_QNAME = new QName("", "id");
    private final static QName _CalcGroupRequestFilterOwner_QNAME = new QName("", "owner");
    private final static QName _CalcGroupRequestFilterType_QNAME = new QName("", "type");
    private final static QName _CalcGroupValue_QNAME = new QName("", "value");
    private final static QName _CalcGroupDate_QNAME = new QName("", "date");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sng.asuneft.atp.okocits3.ws.ueh.client.stub
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CalcGroupFormulaOperand }
     * 
     */
    public CalcGroupFormulaOperand createCalcGroupFormulaOperand() {
        return new CalcGroupFormulaOperand();
    }

    /**
     * Create an instance of {@link DeleteCalcGroup }
     * 
     */
    public DeleteCalcGroup createDeleteCalcGroup() {
        return new DeleteCalcGroup();
    }

    /**
     * Create an instance of {@link GetCalcGroup }
     * 
     */
    public GetCalcGroup createGetCalcGroup() {
        return new GetCalcGroup();
    }

    /**
     * Create an instance of {@link CalcLimit }
     * 
     */
    public CalcLimit createCalcLimit() {
        return new CalcLimit();
    }

    /**
     * Create an instance of {@link NewCalcGroupResponse }
     * 
     */
    public NewCalcGroupResponse createNewCalcGroupResponse() {
        return new NewCalcGroupResponse();
    }

    /**
     * Create an instance of {@link SetLimits }
     * 
     */
    public SetLimits createSetLimits() {
        return new SetLimits();
    }

    /**
     * Create an instance of {@link SetLimitsResponse }
     * 
     */
    public SetLimitsResponse createSetLimitsResponse() {
        return new SetLimitsResponse();
    }

    /**
     * Create an instance of {@link CalcGroupRequestFilter }
     * 
     */
    public CalcGroupRequestFilter createCalcGroupRequestFilter() {
        return new CalcGroupRequestFilter();
    }

    /**
     * Create an instance of {@link NewCalcGroup }
     * 
     */
    public NewCalcGroup createNewCalcGroup() {
        return new NewCalcGroup();
    }

    /**
     * Create an instance of {@link GetCalcGroups }
     * 
     */
    public GetCalcGroups createGetCalcGroups() {
        return new GetCalcGroups();
    }

    /**
     * Create an instance of {@link GetCalcGroupsResponse }
     * 
     */
    public GetCalcGroupsResponse createGetCalcGroupsResponse() {
        return new GetCalcGroupsResponse();
    }

    /**
     * Create an instance of {@link GetCalcGroupResponse }
     * 
     */
    public GetCalcGroupResponse createGetCalcGroupResponse() {
        return new GetCalcGroupResponse();
    }

    /**
     * Create an instance of {@link CalcGroupPI }
     * 
     */
    public CalcGroupPI createCalcGroupPI() {
        return new CalcGroupPI();
    }

    /**
     * Create an instance of {@link DeleteCalcGroupResponse }
     * 
     */
    public DeleteCalcGroupResponse createDeleteCalcGroupResponse() {
        return new DeleteCalcGroupResponse();
    }

    /**
     * Create an instance of {@link CalcGroupLimit }
     * 
     */
    public CalcGroupLimit createCalcGroupLimit() {
        return new CalcGroupLimit();
    }

    /**
     * Create an instance of {@link CalcGroup }
     * 
     */
    public CalcGroup createCalcGroup() {
        return new CalcGroup();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCalcGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "deleteCalcGroup")
    public JAXBElement<DeleteCalcGroup> createDeleteCalcGroup(DeleteCalcGroup value) {
        return new JAXBElement<DeleteCalcGroup>(_DeleteCalcGroup_QNAME, DeleteCalcGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCalcGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "deleteCalcGroupResponse")
    public JAXBElement<DeleteCalcGroupResponse> createDeleteCalcGroupResponse(DeleteCalcGroupResponse value) {
        return new JAXBElement<DeleteCalcGroupResponse>(_DeleteCalcGroupResponse_QNAME, DeleteCalcGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetLimitsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "setLimitsResponse")
    public JAXBElement<SetLimitsResponse> createSetLimitsResponse(SetLimitsResponse value) {
        return new JAXBElement<SetLimitsResponse>(_SetLimitsResponse_QNAME, SetLimitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCalcGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "newCalcGroupResponse")
    public JAXBElement<NewCalcGroupResponse> createNewCalcGroupResponse(NewCalcGroupResponse value) {
        return new JAXBElement<NewCalcGroupResponse>(_NewCalcGroupResponse_QNAME, NewCalcGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalcGroupsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "getCalcGroupsResponse")
    public JAXBElement<GetCalcGroupsResponse> createGetCalcGroupsResponse(GetCalcGroupsResponse value) {
        return new JAXBElement<GetCalcGroupsResponse>(_GetCalcGroupsResponse_QNAME, GetCalcGroupsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalcGroups }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "getCalcGroups")
    public JAXBElement<GetCalcGroups> createGetCalcGroups(GetCalcGroups value) {
        return new JAXBElement<GetCalcGroups>(_GetCalcGroups_QNAME, GetCalcGroups.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetLimits }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "setLimits")
    public JAXBElement<SetLimits> createSetLimits(SetLimits value) {
        return new JAXBElement<SetLimits>(_SetLimits_QNAME, SetLimits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCalcGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "newCalcGroup")
    public JAXBElement<NewCalcGroup> createNewCalcGroup(NewCalcGroup value) {
        return new JAXBElement<NewCalcGroup>(_NewCalcGroup_QNAME, NewCalcGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalcGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "getCalcGroup")
    public JAXBElement<GetCalcGroup> createGetCalcGroup(GetCalcGroup value) {
        return new JAXBElement<GetCalcGroup>(_GetCalcGroup_QNAME, GetCalcGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalcGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ueh.ws.okocits3.atp.asuneft.sng/", name = "getCalcGroupResponse")
    public JAXBElement<GetCalcGroupResponse> createGetCalcGroupResponse(GetCalcGroupResponse value) {
        return new JAXBElement<GetCalcGroupResponse>(_GetCalcGroupResponse_QNAME, GetCalcGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "id", scope = CalcGroupRequestFilter.class)
    public JAXBElement<Integer> createCalcGroupRequestFilterId(Integer value) {
        return new JAXBElement<Integer>(_CalcGroupRequestFilterId_QNAME, Integer.class, CalcGroupRequestFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "owner", scope = CalcGroupRequestFilter.class)
    public JAXBElement<String> createCalcGroupRequestFilterOwner(String value) {
        return new JAXBElement<String>(_CalcGroupRequestFilterOwner_QNAME, String.class, CalcGroupRequestFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type", scope = CalcGroupRequestFilter.class)
    public JAXBElement<String> createCalcGroupRequestFilterType(String value) {
        return new JAXBElement<String>(_CalcGroupRequestFilterType_QNAME, String.class, CalcGroupRequestFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "value", scope = CalcGroup.class)
    public JAXBElement<Double> createCalcGroupValue(Double value) {
        return new JAXBElement<Double>(_CalcGroupValue_QNAME, Double.class, CalcGroup.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "date", scope = CalcGroup.class)
    public JAXBElement<XMLGregorianCalendar> createCalcGroupDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CalcGroupDate_QNAME, XMLGregorianCalendar.class, CalcGroup.class, value);
    }

}
