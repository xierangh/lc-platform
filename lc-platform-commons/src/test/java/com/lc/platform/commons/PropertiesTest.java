package com.lc.platform.commons;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * 属性配置文件读写测试
 * @author 陈均
 *
 */
public class PropertiesTest {

	/**
	 * 对指定的配置文件进行读写操作
	 * @throws ConfigurationException
	 */
	@Test
	public void test01() throws ConfigurationException{
		String filename = "test.properties";
		PropertiesConfiguration config = new PropertiesConfiguration(filename); 
		Assert.assertEquals("0/20 * * * * ?", config.getString("syrj.cron"));
		config.setProperty("syrj.cron", "0/22 * * * * ?");
		config.addProperty("syrj.days", 22);
		config.save();
		
		PropertiesConfiguration config2 = new PropertiesConfiguration(filename); 
		Assert.assertEquals("0/22 * * * * ?", config2.getString("syrj.cron"));
		config2.setProperty("syrj.cron", "0/22 * * * * ?");
		Assert.assertEquals(22, config2.getInt("syrj.days"));
		config2.save();
	} 
	
}
