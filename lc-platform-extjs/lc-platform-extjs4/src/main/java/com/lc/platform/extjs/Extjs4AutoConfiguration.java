package com.lc.platform.extjs;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.lc.platform.commons.StaticResUtil;

/**
 * extjs4自动装配css和js，不是必须的
 * @author chenjun
 *
 */
@Configuration
public class Extjs4AutoConfiguration implements ServletContextAware{
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		StaticResUtil srutil = new StaticResUtil(contextPath);
		String static_extjs4 = srutil.buildStatic(
				"extjs4/ext-all.js",
				"extjs4/locale/ext-lang-zh_CN.js",
				"extjs4/src/ux/Msg.js",
				"extjs4/src/ux/Ajax.js",
				"extjs4/resources/ext-theme-blue/ext-theme-blue-all-debug.css");
		servletContext.setAttribute("static_extjs4", static_extjs4);
	}
	
}
