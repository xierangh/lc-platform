package com.lc.platform.jqgrid;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.lc.platform.commons.StaticResUtil;

@Configuration
public class JQGridAutoConfiguration implements ServletContextAware {
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		StaticResUtil srutil = new StaticResUtil(contextPath);
		String static_jqgrid = srutil.buildStatic(
				"jqgrid-4.8.2/js/jquery.jqGrid.src.js",
				"jqgrid-4.8.2/js/i18n/grid.locale-cn.js",
				"jqgrid-4.8.2/css/ui.jqgrid.css");
		servletContext.setAttribute("static_jqgrid", static_jqgrid);
	}

}
