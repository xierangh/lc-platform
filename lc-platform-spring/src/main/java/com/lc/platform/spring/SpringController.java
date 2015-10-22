package com.lc.platform.spring;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.lc.platform.commons.UUIDUtil;

/**
 * 提供各种提示页面的请求
 * @author chenjun
 *
 */
@Controller
@RequestMapping(value = "/")
public class SpringController  implements ServletContextAware {
	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	@Value("${spring.freemarker.js-ignoring:}")
	private String jsIgnoring;
	
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
	
	@RequestMapping(value="/**/{file:.+\\.js$}",method=RequestMethod.GET)
	public ModelAndView file(@PathVariable String file,HttpServletResponse response)throws Exception{
		String path = SpringUtil.getRequest().getRequestURI();
		PathMatcher matcher = new AntPathMatcher();
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("application/javascript");
		if(StringUtils.isNotBlank(jsIgnoring)){
			for (String pattern : jsIgnoring.split(",")) {
				if(matcher.match(pattern, path)){
					Resource[] resources = resourcePatternResolver.getResources("classpath*:static/"+path);
					String content = IOUtils.toString(resources[0].getURL());
					response.getWriter().write(content);
					return null;
				}
			}
		}
		modelAndView.setViewName(path+"_"+UUIDUtil.uuid());
		return modelAndView;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		servletContext.setAttribute("contextPath",contextPath);
		String crlf=System.getProperty("line.separator");
		StringBuilder context = new StringBuilder();
		context.append("<script type=\"text/javascript\">").append(crlf)
		.append("var contextPath = '" + contextPath + "';").append(crlf)
		.append("var console = console || {log : function(){return false;}};").append(crlf)
		.append("</script>").append(crlf);
		servletContext.setAttribute("static_context",context);
	}
	
}
