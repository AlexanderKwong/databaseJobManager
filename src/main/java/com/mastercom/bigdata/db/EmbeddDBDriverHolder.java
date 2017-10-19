package com.mastercom.bigdata.db;

import java.sql.Driver;

import org.apache.derby.jdbc.EmbeddedDriver;

public final class EmbeddDBDriverHolder {

	private static Driver derbyEmbeddedDriver;
	
	private EmbeddDBDriverHolder(){}
	
	public static Driver getInstance(){
		if(derbyEmbeddedDriver == null){
			derbyEmbeddedDriver = new EmbeddedDriver();
		}
		return derbyEmbeddedDriver;
	}
}
