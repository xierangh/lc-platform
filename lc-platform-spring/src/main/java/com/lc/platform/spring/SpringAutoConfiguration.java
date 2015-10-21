package com.lc.platform.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.lc.platform.commons.spring.MessageUtil;

import freemarker.ext.jsp.TaglibFactory.ClasspathMetaInfTldSource;
import freemarker.ext.jsp.TaglibFactory.MetaInfTldSource;

@Configuration
@AutoConfigureBefore(FreeMarkerAutoConfiguration.class)
public class SpringAutoConfiguration extends WebMvcConfigurerAdapter implements BeanPostProcessor{
	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	@Autowired
	protected FreeMarkerProperties properties;
	
	public SpringAutoConfiguration(){
	}
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new com.lc.platform.spring.FreeMarkerConfigurer();
		applyProperties(configurer);
		return configurer;
	}
	
	protected void applyProperties(FreeMarkerConfigurationFactory factory) {
		factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		factory.setDefaultEncoding(this.properties.getCharset());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		factory.setFreemarkerSettings(settings);
	}
	
	@Autowired
	public void setProperties(FreeMarkerProperties properties) {
		try {
			Resource[] resources = resourcePatternResolver.getResources("classpath*:templates");
			String[] templateLoaderPaths = new String[resources.length];
			for (int i = 0; i < resources.length; i++) {
				String templateLoaderPath = resources[i].getURI().toString();
				templateLoaderPaths[i] = templateLoaderPath;
			} 
			String[] oldTemplateLoaderPaths = properties.getTemplateLoaderPath();
			templateLoaderPaths = (String[]) ArrayUtils.addAll(templateLoaderPaths, oldTemplateLoaderPaths);
			properties.setTemplateLoaderPath(templateLoaderPaths);
			properties.getSettings().put("auto_import", "/spring.ftl as spring");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 设置Freemarker静态模块类
	 * @return FreemarkerStaticModels
	 * @throws Exception
	 */
	@Bean
	public FreemarkerStaticModels freemarkerStaticModels() throws Exception{
		FreemarkerStaticModels freemarkerStaticModels = FreemarkerStaticModels.getInstance();
		Resource[] resources = resourcePatternResolver.getResources("classpath*:freemarkerstatic.properties");
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocations(resources);
		freemarkerStaticModels.setStaticModels(propertiesFactoryBean.createProperties());
		return freemarkerStaticModels;
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer(){
		return new EmbeddedServletContainerCustomizerBean();
	}
	
	@Bean
	public MessageSource messageSource() {
		return MessageUtil.getMessageSource();
	}
	
	@Bean
	public LocaleResolver localeResolver(){
		return new SessionLocaleResolver();
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof FreeMarkerConfigurer){
			FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer)bean;
			List<MetaInfTldSource> metaInfTldSources = new ArrayList<MetaInfTldSource>();
			ClasspathMetaInfTldSource metaInfTldSource = new ClasspathMetaInfTldSource(Pattern.compile(".*", Pattern.DOTALL));
			metaInfTldSources.add(metaInfTldSource);
			freeMarkerConfigurer.getTaglibFactory().setMetaInfTldSources(metaInfTldSources);
		}else if(bean instanceof FreeMarkerViewResolver){
			FreeMarkerViewResolver freeMarkerViewResolver = (FreeMarkerViewResolver)bean;
			freeMarkerViewResolver.setViewClass(FreeMarkerView.class);
		}
		return bean;
	}

}
