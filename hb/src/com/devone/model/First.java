package com.devone.model;

public class First {

	
	public static void main(String[] args) {
		HbFactory hb = new HbFactory();
		hb.setup();
		CompanyModel cm = new CompanyModel();
		//cm.save();
		//cm.update()
		//cm.get();
		cm.delete();
		hb.exit();
	}

}
