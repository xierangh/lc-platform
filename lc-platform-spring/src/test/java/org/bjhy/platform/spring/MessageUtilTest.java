package org.bjhy.platform.spring;

import org.junit.Assert;
import org.junit.Test;

import com.lc.platform.commons.spring.MessageUtil;

public class MessageUtilTest {

	@Test
	public void testGetMessage(){
		String notFound = MessageUtil.getMessage("NotFound");
		Assert.assertTrue(!notFound.equals(""));
	}
	
}
