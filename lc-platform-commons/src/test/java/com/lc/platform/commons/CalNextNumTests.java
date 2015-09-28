package com.lc.platform.commons;

import org.junit.Assert;
import org.junit.Test;

import com.lc.platform.commons.CalNextNum;

public class CalNextNumTests {
	@Test
	public void testCalcNumber(){
		CalNextNum calNextNum = new CalNextNum();
		Assert.assertEquals("001", calNextNum.nextNum("000"));
		Assert.assertEquals("000a", calNextNum.nextNum("999"));
		Assert.assertEquals("000b", calNextNum.nextNum("999a"));
		Assert.assertEquals("001-003", calNextNum.nextNum("001-002"));
		Assert.assertEquals("001a-002a", calNextNum.nextNum("001a-001a"));
	}
}
