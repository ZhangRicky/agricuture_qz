package com.chenghui.agriculture.ws.impls;

import javax.jws.WebService;

import com.chenghui.agriculture.ws.interfaces.HelloWorld;

@WebService(endpointInterface="com.chenghui.agriculture.ws.interfaces.HelloWorld",serviceName="helloWorld")
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHello(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
	}

}
