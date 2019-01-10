package com.mastercom.bigdata.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

	public static  Properties readPath(String configName) throws FileNotFoundException, IOException{

		InputStream in = null;
		in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configName);
		if(in == null){
			in = ConfigUtil.class.getClassLoader().getResourceAsStream(configName);
		}
		if(in == null){
			in = ConfigUtil.class.getResourceAsStream(configName);
		}
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		return properties;
	}

}
