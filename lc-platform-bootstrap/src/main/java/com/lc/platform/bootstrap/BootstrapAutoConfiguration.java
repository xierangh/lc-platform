package com.lc.platform.bootstrap;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.lc.platform.commons.StaticResUtil;
/**
 * Bootstrap自动装配css和js，不是必须的
 * @author chenjun
 *
 */
@Configuration
public class BootstrapAutoConfiguration implements ServletContextAware{
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		StaticResUtil staticResourceUtil = new StaticResUtil(
				contextPath);
		String static_bootstrap = staticResourceUtil.buildStatic(
				"bootstrap/css/bootstrap.css",
				"bootstrap/js/bootstrap.min.js");
		servletContext.setAttribute("static_bootstrap", static_bootstrap);
	}
	
}
