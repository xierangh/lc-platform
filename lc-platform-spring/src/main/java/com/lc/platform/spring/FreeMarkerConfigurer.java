package com.lc.platform.spring;

import java.util.List;

import freemarker.cache.TemplateLoader;


public class FreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

	@Override
	protected TemplateLoader getAggregateTemplateLoader(
			List<TemplateLoader> templateLoaders) {
		templateLoaders.add(new JavaScriptTemplateLoader());
		return super.getAggregateTemplateLoader(templateLoaders);
	}
	
}
