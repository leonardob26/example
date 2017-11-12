package com.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="com.webservice.ISubstraction", targetNamespace="http://www.webservice.com")
public interface ISubstraction {
	@WebMethod(operationName = "substract", action = "urn:Substract")
	int substract(int a, int b);

}
