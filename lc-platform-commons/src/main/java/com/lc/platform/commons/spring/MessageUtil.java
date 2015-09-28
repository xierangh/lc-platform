package com.lc.platform.commons.spring;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;



public class MessageUtil{
	
	protected static MessageSourceAccessor accessor;
	protected static MessageSource messageSource;
	protected static Set<String> set = new HashSet<String>();
	private static int defaultStatus = 0;
	static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
 
	static{
		addLocation("classpath*:i18n/*");
		addLocation("classpath*:org/springframework/security/*");
	}
	
	protected static void initMessageSourceAccessor(){
		String[]basenames = new String[set.size()];
		set.toArray(basenames);
		ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
		reloadableResourceBundleMessageSource.setBasenames(basenames);
		reloadableResourceBundleMessageSource.setCacheSeconds(5);
		accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
		messageSource = reloadableResourceBundleMessageSource;
	}
	
	public static void removeLocation(String locationPattern){
		try {
			Resource[] resources = resourcePatternResolver.getResources(locationPattern);
			for (int i = 0; i < resources.length; i++) {
				String url = resources[i].getURL().toString();
				int lastIndex = url.lastIndexOf("/");
				String prefix = url.substring(0,lastIndex + 1);
				String suffix = url.substring(lastIndex+1);
				suffix = suffix.split("\\.")[0].split("_")[0];
				set.remove(prefix + suffix);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		initMessageSourceAccessor();
	}
	
	public static void addLocation(String locationPattern){
		try {
			Resource[] resources = resourcePatternResolver.getResources(locationPattern);
			for (int i = 0; i < resources.length; i++) {
				String url = resources[i].getURL().toString();
				int lastIndex = url.lastIndexOf("/");
				String prefix = url.substring(0,lastIndex + 1);
				String suffix = url.substring(lastIndex+1);
				suffix = suffix.split("\\.")[0].split("_")[0];
				set.add(prefix + suffix);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		initMessageSourceAccessor();
	}
	
	private MessageUtil(){
	}

	public static Message message(int status,String code,Object[] args,Object data){
		return new Message(status, getMessage(code,args), data);
	}
	
	public static Message message(int status,String code,Object data){
		return new Message(status, getMessage(code), data);
	}
	
	public static Message message(int status,String code,Object[] args){
		return new Message(status, getMessage(code,args));
	}
	
	public static Message message(int status,Object[] args){
		return new Message(status, getMessage(status+"",args));
	}
	
	public static Message message(int status){
		return new Message(status,getMessage(status+"",""));
	}
	
	public static Message message(int status,Object data){
		return new Message(status,getMessage(status+"",""),data);
	}
	
	public static Message message(String code){
		return new Message(defaultStatus, getMessage(code));
	}
	
	public static Message message(String code,Object[] args){
		return new Message(defaultStatus, getMessage(code, args));
	}
	
	public static Message message(String code,Object[] args,Object data){
		return new Message(defaultStatus, getMessage(code, args), data);
	}
	
	public static Message message(String code,Object data){
		return new Message(defaultStatus, getMessage(code), data);
	}
	
	public static String getMessage(String code){
		return accessor.getMessage(code,LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String code,String defaultMessage){
		return accessor.getMessage(code,defaultMessage,LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String code, Object[] args){
		return accessor.getMessage(code,args,LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String code, Object[] args,String defaultMessage){
		return accessor.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
	}

	public static MessageSource getMessageSource() {
		return messageSource;
	}

}
