/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lc.platform.xzqh.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lc.platform.xzqh.Application;
import com.lc.platform.xzqh.domain.Xzqh;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class XzqhDaoTests {

	@Autowired
	XzqhDao xzqhDao;

	@Test
	public void findXzqhByParent() {
		List<Xzqh> list = xzqhDao.findXzqhByParent("0");
		for (Xzqh xzqh : list) {
			System.out.println(xzqh.getCodeName());
		}
	}
	
	@Test
	public void testConvertImage() throws IOException{
		byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File("d:\\loading.gif")));
		String str = "data:image/png;base64,"+Base64.encodeBase64String(bytes);
		System.out.println(str);
	}

	/**
	 * 将h2数据迁移到mysql数据库
	 */
	@Test
	public void testDataMigrate() {
		Sort sort = new Sort(Direction.ASC, "id");
		long start = System.currentTimeMillis();
		buildSessionFactory();
		for (int i = 147; i < 148; i++) {
			PageRequest pageRequest = new PageRequest(i, 5000, sort);
			Page<Xzqh> page = xzqhDao.findAll(pageRequest);
			System.out.println("总数据量：" + page.getTotalElements());
			System.out.println("总页数：" + page.getTotalPages());
			System.out.println("当前页码：" + page.getNumber());
			System.out.println("实际数据量：" + page.getNumberOfElements());
		}
		long end = System.currentTimeMillis();
		System.out.println("迁移数据时间：" + (end - start) / 1000 + "秒");
	}

	public SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration()
				.addAnnotatedClass(Xzqh.class)
				.setProperty(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver")
				.setProperty(AvailableSettings.USER, "root")
				.setProperty(AvailableSettings.PASS, "")
				.setProperty(AvailableSettings.URL,
						"jdbc:mysql://localhost:3306/xzqh?useUnicode=true&characterEncoding=utf8")
				.setProperty(AvailableSettings.SHOW_SQL, "true")
				.setProperty(AvailableSettings.FORMAT_SQL, "true")
				.setProperty(AvailableSettings.HBM2DDL_AUTO, "update")
				.setProperty("hibernate.connection.pool_size", "50")
				.setProperty("hibernate.current_session_context_class",
						"thread")
				.setProperty(AvailableSettings.DIALECT,
						MySQLDialect.class.getName());
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}
}
