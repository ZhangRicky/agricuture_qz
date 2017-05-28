/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.chenghui.agriculture.ws.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;



public final class Client_BAK {

    private Client_BAK() {
    }

    public static void main(String args[]) throws Exception {
        // START SNIPPET: client
//        ClassPathXmlApplicationContext context 
//            = new ClassPathXmlApplicationContext(new String[] {"client-beans.xml"});
//
//        HelloWorld client = (HelloWorld)context.getBean("client");
//        
//        String response = client.sayHello("Joe");
//        System.out.println("Response: " + response);
//        System.exit(0);
        // END SNIPPET: client
        HelloWorld_Service gs = new HelloWorld_Service();
        HelloWorld helloworld = gs.getHelloWorldImplPort();
        
        org.apache.cxf.endpoint.Client client = ClientProxy.getClient(helloworld);
        org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();
         
        Map<String,Object> outProps = new HashMap<String,Object>();  
        outProps.put(WSHandlerConstants.ACTION,WSHandlerConstants.SIGNATURE+ " "+ WSHandlerConstants.ENCRYPT);
        outProps.put(WSHandlerConstants.USER,"clientprivatekey");
        outProps.put(WSHandlerConstants.ENCRYPTION_USER,"serverpublickey");
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,UTPasswordClientCallBack.class.getName());
        outProps.put(WSHandlerConstants.SIG_PROP_FILE,"ws/etc/Client_Sign.properties");
        outProps.put(WSHandlerConstants.ENC_PROP_FILE,"ws/etc/Client_Encrypt.properties");
        outProps.put(WSHandlerConstants.ACTOR,"clientprivatekey");
        
        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        cxfEndpoint.getOutInterceptors().add(wssOut);
        //------------------
        Map<String, Object> inProp = new HashMap<String, Object>();

        inProp.put(WSHandlerConstants.ACTOR,"clientprivatekey");
        inProp.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE +" " + WSHandlerConstants.ENCRYPT);
        inProp.put(WSHandlerConstants.PW_CALLBACK_CLASS,UTPasswordClientCallBack.class.getName());        
        inProp.put(WSHandlerConstants.DEC_PROP_FILE,"ws/etc/Client_Sign.properties");
        inProp.put(WSHandlerConstants.SIG_PROP_FILE,"ws/etc/Client_Encrypt.properties");
        cxfEndpoint.getInInterceptors().add(new WSS4JInInterceptor(inProp));
//---------------------------------
        JaxWsProxyFactoryBean factory=new JaxWsProxyFactoryBean();
        factory.setServiceClass(HelloWorld.class);
        factory.setAddress("http://localhost:8080/credit_reference_center/service/helloWorld");
        HelloWorld hello = (HelloWorld)factory.create();
        hello.sayHello("ssddd");

    }
}
