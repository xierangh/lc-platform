package com.lc.platform.xzqh.service;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lc.platform.xzqh.Application;
import com.lc.platform.xzqh.domain.Xzqh;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class XzqhServiceTests {
	@Autowired
	XzqhService xzqhService;

	@Test
	public void searchXzqh() {
		long start = System.currentTimeMillis();
		String content = "yutangc";
		Set<Xzqh> list =  xzqhService.searchXzqh(content);
		long end = System.currentTimeMillis();
		for (Xzqh xzqh : list) {
			System.out.println(xzqh.getCodeName()+':' + xzqh.getNumberCode());
		}
		System.out.println("查询时间：" + (end - start) + "命中个数：" + list.size());
	}
	//510000,510500,510504,510504101000,510504101203xx220
}
