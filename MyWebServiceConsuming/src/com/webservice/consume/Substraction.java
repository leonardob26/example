package com.webservice.consume;

import com.webservice.ComWebserviceISubstraction;
import com.webservice.SubstractionPortImpl;
import com.webservice.SubstractionService;

public class Substraction {

	public static void main(String[] args) {
		SubstractionService service = new SubstractionService();
		ComWebserviceISubstraction operation = service.getSubstractionPort();
		System.out.println(operation.substract(5, 2));

	}

}
