
package com.chenghui.agriculture.ws.client.website;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.chenghui.agriculture.ws.client.website package. 
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

    private final static QName _Login_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "login");
    private final static QName _LoginResponse_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "loginResponse");
    private final static QName _QueryCreditRating_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "queryCreditRating");
    private final static QName _QueryCreditRatingResponse_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "queryCreditRatingResponse");
    private final static QName _QueryCreditRecords_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "queryCreditRecords");
    private final static QName _QueryCreditRecordsResponse_QNAME = new QName("http://interfaces.ws.credit.chenghui.com/", "queryCreditRecordsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.chenghui.agriculture.ws.client.website
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link QueryCreditRating }
     * 
     */
    public QueryCreditRating createQueryCreditRating() {
        return new QueryCreditRating();
    }

    /**
     * Create an instance of {@link QueryCreditRatingResponse }
     * 
     */
    public QueryCreditRatingResponse createQueryCreditRatingResponse() {
        return new QueryCreditRatingResponse();
    }

    /**
     * Create an instance of {@link QueryCreditRecords }
     * 
     */
    public QueryCreditRecords createQueryCreditRecords() {
        return new QueryCreditRecords();
    }

    /**
     * Create an instance of {@link QueryCreditRecordsResponse }
     * 
     */
    public QueryCreditRecordsResponse createQueryCreditRecordsResponse() {
        return new QueryCreditRecordsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCreditRating }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "queryCreditRating")
    public JAXBElement<QueryCreditRating> createQueryCreditRating(QueryCreditRating value) {
        return new JAXBElement<QueryCreditRating>(_QueryCreditRating_QNAME, QueryCreditRating.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCreditRatingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "queryCreditRatingResponse")
    public JAXBElement<QueryCreditRatingResponse> createQueryCreditRatingResponse(QueryCreditRatingResponse value) {
        return new JAXBElement<QueryCreditRatingResponse>(_QueryCreditRatingResponse_QNAME, QueryCreditRatingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCreditRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "queryCreditRecords")
    public JAXBElement<QueryCreditRecords> createQueryCreditRecords(QueryCreditRecords value) {
        return new JAXBElement<QueryCreditRecords>(_QueryCreditRecords_QNAME, QueryCreditRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCreditRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.credit.chenghui.com/", name = "queryCreditRecordsResponse")
    public JAXBElement<QueryCreditRecordsResponse> createQueryCreditRecordsResponse(QueryCreditRecordsResponse value) {
        return new JAXBElement<QueryCreditRecordsResponse>(_QueryCreditRecordsResponse_QNAME, QueryCreditRecordsResponse.class, null, value);
    }

}
