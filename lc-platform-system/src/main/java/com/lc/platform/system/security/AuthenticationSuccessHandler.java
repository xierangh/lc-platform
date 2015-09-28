package com.lc.platform.system.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.spring.SpringUtil;
import com.lc.platform.system.SystemUtil;

/**
 * 认证成功帮助类,该类会提供用户信息并跳转到首页
 * @author chenjun
 *
 */
@Component 
public class AuthenticationSuccessHandler
		implements
		org.springframework.security.web.authentication.AuthenticationSuccessHandler {
	
	@Value("${system.login.success-url:}")
	private String authSuccessUrl;
	private static final String LOGIN_TYPE = "loginType";
	private static final String AJAX_LOGIN = "ajaxLogin";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String contextPath = request.getContextPath();
		String loginType = request.getParameter(LOGIN_TYPE);
		SpringUtil.setSessionAttribute("user", SystemUtil.getCurrentUser());
		//根据具体的配置信息跳转到指定的成功页面，目前使用配置文件，后期根据用户选择的主题进行跳转
		if(StringUtils.isBlank(authSuccessUrl)){
			authSuccessUrl = request.getContextPath();
		}
		if(AJAX_LOGIN.equals(loginType)){
			request.setAttribute("message", new Message(0,""));
			request.getRequestDispatcher(contextPath + "/system/authsuccess")
			.forward(request, response);
		}else{
			response.sendRedirect(authSuccessUrl);
		}
	}

}
