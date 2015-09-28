package com.lc.platform.system.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lc.platform.system.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserServiceTests {
	@Autowired
	private UserService userService;
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Test
	public void test01(){
		List<?> list = em.createQuery("select ud.user from UserDept ud where 1=1 and ud.dept.id in ('001-001') order by ud.user.username desc ").getResultList();
		System.out.println(list.size());
	}
	
}
