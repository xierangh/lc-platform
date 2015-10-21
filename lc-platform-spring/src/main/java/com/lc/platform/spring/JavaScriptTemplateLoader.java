package com.lc.platform.spring;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import freemarker.cache.TemplateLoader;

public class JavaScriptTemplateLoader implements TemplateLoader{
	protected final Log logger = LogFactory.getLog(getClass());
	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	@Override
	public Object findTemplateSource(String name) throws IOException {
		Pattern pattern = Pattern.compile("(.+\\.js)_[\\w]{32}\\..+");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			String path = matcher.group(1);
			Resource[] resources = resourcePatternResolver.getResources("classpath*:static/"+path);
			if(resources.length>0){
				return resources[0];
			} 
		}
		return null;
	}

	@Override
	public long getLastModified(Object templateSource) {
		Resource resource = (Resource) templateSource;
		try {
			return resource.lastModified();
		}
		catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not obtain last-modified timestamp for FreeMarker template in " +
						resource + ": " + ex);
			}
			return -1;
		}
	}

	@Override
	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		Resource resource = (Resource) templateSource;
		try {
			return new InputStreamReader(resource.getInputStream(), encoding);
		}
		catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not find FreeMarker template: " + resource);
			}
			throw ex;
		}
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		
	}

}
