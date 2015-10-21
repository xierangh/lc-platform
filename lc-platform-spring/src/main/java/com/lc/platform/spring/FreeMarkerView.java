package com.lc.platform.spring;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContextUtils;

import freemarker.template.SimpleHash;


public class FreeMarkerView extends org.springframework.web.servlet.view.freemarker.FreeMarkerView {

	@Override
	protected void doRender(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Expose model to JSP tags (as request attributes).
		exposeModelAsRequestAttributes(model, request);
		// Expose all standard FreeMarker hash models.
		SimpleHash fmModel = buildTemplateModel(model, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
		}
		// Grab the locale-specific version of the template.
		Locale locale = RequestContextUtils.getLocale(request);
		processTemplate(getTemplate(locale), fmModel, response);
	}
	
}
