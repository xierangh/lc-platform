package com.lc.platform.system.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.system.Application;
import com.lc.platform.system.domain.Menu;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MenuServiceTests {
	@Autowired
	private MenuService menuService;
	
	@Test
	public void testFindAllMenu() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Menu> list = menuService.findAllMenu();
		String content = objectMapper.writeValueAsString(list);
		System.out.println(content);
	}
	
	@Test
	public void testCreateMenuImageData() throws IOException{
		byte[] bytes =FileUtils.readFileToByteArray(new File("D:\\pic\\user_online_48px_1174119_easyicon.net.png"));
		String str = "data:image/png;base64,"+Base64.encodeBase64String(bytes);
		System.out.println(str);
	}
}
