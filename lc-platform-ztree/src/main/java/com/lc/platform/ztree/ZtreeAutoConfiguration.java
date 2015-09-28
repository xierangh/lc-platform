package com.lc.platform.ztree;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.lc.platform.commons.StaticResUtil;

/**
 * ztree自动装配css和js，不是必须的
 * @author chenjun
 *
 */
@Configuration
public class ZtreeAutoConfiguration implements ServletContextAware {

	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		StaticResUtil staticResourceUtil = new StaticResUtil(
				contextPath);
		String static_ztree = staticResourceUtil.buildStatic(
				"ztree/css/zTreeStyle/zTreeStyle.css",
				"ztree/js/jquery.ztree.all-3.5.min.js");
		servletContext.setAttribute("static_ztree", static_ztree);
		
	}

}
