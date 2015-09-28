package com.lc.platform.jquery;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.lc.platform.commons.StaticResUtil;

/**
 * jquery自动装配css和js，不是必须的
 * @author chenjun
 *
 */
@Configuration
public class JqueryAutoConfiguration implements ServletContextAware{
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();
		StaticResUtil staticResUtil = new StaticResUtil(
				contextPath);
		String static_jquery = staticResUtil
				.buildStatic("jquery/jquery-1.11.1.min.js",
						"jquery/jquery-migrate-1.1.1.js");
		String static_jquery_validate = staticResUtil
				.buildStatic("jquery-validate-1.13.1/jquery.validate.js");
		String static_jquery_plugin = staticResUtil
				.buildStatic("jquery-plugins/jquery.loading.js");
		
		String static_jqueryui = staticResUtil
				.buildStatic("jquery-ui/jquery-ui.min.css",
						"jquery-ui/jquery-ui.min.js");
		String static_validation_engine = staticResUtil
				.buildStatic(
						"validation-engine-2.6.2/css/validationEngine.jquery.css",
						"validation-engine-2.6.2/languages/jquery.validationEngine-zh_CN.js",
						"validation-engine-2.6.2/js/jquery.validationEngine.js");
		
		String static_datepicker = staticResUtil.buildStatic("My97DatePicker4.7.2/WdatePicker.js");
		
		String static_jquery_fileupload = staticResUtil
				.buildStatic("jquery-fileupload/css/jquery.fileupload.css",
						"jquery-fileupload/js/vendor/jquery.ui.widget.js",
						"jquery-fileupload/js/jquery.iframe-transport.js",
						"jquery-fileupload/js/cors/jquery.xdr-transport.js",
						"jquery-fileupload/js/jquery.fileupload.js");
		String static_jquery_jcrop = staticResUtil.buildStatic("jquery-jcrop/jquery.Jcrop.js",
				"jquery-jcrop/jquery.Jcrop.css");
		servletContext.setAttribute("static_jquery", static_jquery);
		servletContext.setAttribute("static_jqueryui",static_jqueryui);
		servletContext.setAttribute("static_jquery_validate", static_jquery_validate);
		servletContext.setAttribute("static_jquery_plugin",static_jquery_plugin);
		servletContext.setAttribute("static_validation_engine",static_validation_engine);
		servletContext.setAttribute("static_datepicker", static_datepicker);
		servletContext.setAttribute("static_jquery_fileupload", static_jquery_fileupload);
		servletContext.setAttribute("static_jquery_jcrop", static_jquery_jcrop);
	}
	
}
