package com.chenghui.agriculture.ws.client.website;

import java.io.Closeable;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;

public class Client {
	public static void main(String[] args) {
        try {

            SpringBusFactory bf = new SpringBusFactory();
            URL busFile = Client.class.getResource("wssec.xml");
            Bus bus = bf.createBus(busFile.toString());
            BusFactory.setDefaultBus(bus);

            Map<String, Object> outProps = new HashMap<String, Object>();
            outProps.put("action", "UsernameToken Timestamp Signature Encrypt");
            outProps.put("passwordType", "PasswordDigest");

            outProps.put("user", "serverpublickey");
            outProps.put("signatureUser", "clientprivatekey");

            outProps.put("passwordCallbackClass", "com.chenghui.agriculture.ws.client.UTPasswordClientCallBack");

            outProps.put("encryptionUser", "serverpublickey");
            outProps.put("encryptionPropFile", "ws/etc/Client_Encrypt.properties");
            outProps.put("encryptionKeyIdentifier", "IssuerSerial");
            outProps.put("encryptionParts",
                         "{Element}{" + WSConstants.WSSE_NS + "}UsernameToken;"
                         + "{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");

            outProps.put("signaturePropFile", "ws/etc/Client_Sign.properties");
            outProps.put("signatureKeyIdentifier", "DirectReference");
            outProps.put("signatureParts",
                         "{Element}{" + WSConstants.WSU_NS + "}Timestamp;"
                         + "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;"
                         + "{}{http://www.w3.org/2005/08/addressing}ReplyTo;");

            outProps.put("encryptionKeyTransportAlgorithm", 
                         "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
            outProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");


            Map<String, Object> inProps = new HashMap<String, Object>();

            inProps.put("action", "UsernameToken Timestamp Signature Encrypt");
//            inProps.put("action", "Signature Encrypt");
            inProps.put("passwordType", "PasswordText");
            inProps.put("passwordCallbackClass", "com.chenghui.agriculture.ws.client.UTPasswordClientCallBack");

            inProps.put("decryptionPropFile", "ws/etc/Client_Sign.properties");
            inProps.put("encryptionKeyIdentifier", "IssuerSerial");

            inProps.put("signaturePropFile", "ws/etc/Client_Encrypt.properties");
            inProps.put("signatureKeyIdentifier", "DirectReference");

            inProps.put("encryptionKeyTransportAlgorithm", 
                         "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
            inProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");


            // Check to make sure that the SOAP Body and Timestamp were signed,
            // and that the SOAP Body was encrypted
            DefaultCryptoCoverageChecker coverageChecker = new DefaultCryptoCoverageChecker();
            coverageChecker.setSignBody(true);
            coverageChecker.setSignTimestamp(true);
            coverageChecker.setEncryptBody(true);
//            coverageChecker.setEncryptUsernameToken(true);
            System.setProperty("javax.xml.soap.MessageFactory", "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl");
            WebsiteOper service = new WebsiteOper();
            UserPersonalOperation port = service.getUserPersonalOperationImplPort();
            org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
            client.getInInterceptors().add(new WSS4JInInterceptor(inProps));
            client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
            client.getInInterceptors().add(coverageChecker);
            
//            System.out.println(port.login("120139198411050913", "admin"));
//            System.out.println(port.queryCreditRating(161000L));
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1);
            XMLGregorianCalendar d = convertToXMLGregorianCalendar(calendar.getTime());  
            XMLGregorianCalendar d2 = convertToXMLGregorianCalendar(now);  
            System.out.println(port.queryCreditRecords(161000L, d, d2));

            if (port instanceof Closeable) {
                ((Closeable)port).close();
            }

            bus.shutdown(true);
        } catch (UndeclaredThrowableException ex) {
            ex.getUndeclaredThrowable().printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
	}
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {  
  
        GregorianCalendar cal = new GregorianCalendar();  
        cal.setTime(date);  
        XMLGregorianCalendar gc = null;  
        try {  
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);  
        } catch (Exception e) {  
  
             e.printStackTrace();  
        }  
        return gc;  
    }

}
