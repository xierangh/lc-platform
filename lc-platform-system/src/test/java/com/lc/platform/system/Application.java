package com.lc.platform.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.lc.platform.dao.jpa.GenericRepositoryFactoryBean;

@SpringBootApplication
@ComponentScan(basePackages={"com.lc.platform"})
@EnableJpaRepositories(basePackages={"com.lc.platform"},repositoryFactoryBeanClass=GenericRepositoryFactoryBean.class)
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
