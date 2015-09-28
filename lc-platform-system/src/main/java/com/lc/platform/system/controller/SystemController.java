package com.lc.platform.system.controller;

import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.spring.SpringUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	public static String system = "system/";
	@Value("${system.login.url:/system/view/login}")
	private String loginUrl;
	
	@RequestMapping(value="view/{page:^[a-zA-Z0-9_]+$}",method=RequestMethod.GET)
	public String view(@PathVariable String page)throws Exception{
		boolean result = SpringUtil.checkBrowser();
		if(result){
			return system + page;
		}else{
			return  system + "update_browser";
		}
	}
	
	@RequestMapping(value="authfailure")
	public @ResponseBody Message authfailure()throws Exception{
		return (Message) SpringUtil.getRequestAttribute("message");
	}
	
	@RequestMapping(value="authsuccess")
	public @ResponseBody Message authsuccess()throws Exception{
		return (Message) SpringUtil.getRequestAttribute("message");
	}
	
	/**
	 * 会话过期后的请求
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="invalidSession")
	public String invalidSession(Map<String, Object> map)throws Exception{
		String xmlHttpRequest = SpringUtil.getRequest().getHeader("x-requested-with");
		if(xmlHttpRequest==null){
			SpringUtil.setSessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new AuthenticationException(MessageUtil.getMessage("14013")));
			return loginUrl;
		}else{
			//throw new InvalidSessionException();
			return null;
		}
	}
	
}
