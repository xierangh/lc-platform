package com.lc.platform.commons.coder;

import org.junit.Assert;
import org.junit.Test;

import com.lc.platform.commons.UUIDUtil;

public class UUIDUtilTest {

	@Test
	public void testUUID(){
		String uuid = UUIDUtil.uuid();
		Assert.assertNotNull(uuid);
		Assert.assertEquals(32, uuid.length());
	}
}
