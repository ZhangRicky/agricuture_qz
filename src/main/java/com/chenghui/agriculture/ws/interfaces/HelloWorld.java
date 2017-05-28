package com.chenghui.agriculture.ws.interfaces;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	String sayHello(@WebParam(name="text")String text);
}
