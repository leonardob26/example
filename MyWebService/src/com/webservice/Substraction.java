package com.webservice;

import javax.jws.WebService;

@WebService(endpointInterface="com.webservice.ISubstraction", targetNamespace="http://www.webservice.com")
public class Substraction implements ISubstraction {

	@Override
	public int substract(int a, int b) {
		return a - b;
	}

}
