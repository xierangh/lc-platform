package com.lc.platform.system.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lc.platform.system.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PermServiceTests {
	@Autowired
	private PermService permService;
	
	@Test
	public void testFindAllGrantPerm() throws Exception {
		List<String> list = permService.findAllGrantPerm(new String[]{"8","9"});
		System.out.println(list);
	}
	
	
}
