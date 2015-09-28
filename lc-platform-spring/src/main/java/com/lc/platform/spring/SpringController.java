package com.lc.platform.spring;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

/**
 * 提供各种提示页面的请求
 * @author chenjun
 *
 */
@Controller
@RequestMapping(value = "/")
public class SpringController  implements ServletContextAware {
	
	@RequestMapping(value="404")
	public String _404()throws Exception{
		return "404";
	}
	
	@RequestMapping(value="500")
	public String _500()throws Exception{
		return "500";
	}
	
	@RequestMapping(value="400")
	public String _400()throws Exception{
		return "400";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		servletContext.setAttribute("contextPath",contextPath);
		String crlf=System.getProperty("line.separator");
		StringBuilder context = new StringBuilder();
		context.append("<script type=\"text/javascript\">").append(crlf)
		.append("var contextPath = '" + contextPath + "';").append(crlf)
		.append("</script>").append(crlf);
		servletContext.setAttribute("static_context",context);
	}
	
}
