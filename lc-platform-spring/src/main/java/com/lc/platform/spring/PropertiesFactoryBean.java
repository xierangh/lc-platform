package com.lc.platform.spring;

import java.io.IOException;
import java.util.Properties;


public class PropertiesFactoryBean extends org.springframework.beans.factory.config.PropertiesFactoryBean {

	public Properties createProperties() throws IOException {
		return super.createProperties();
	}
	
}
