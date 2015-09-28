package com.lc.platform.system.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.spring.SpringUtil;

public class AuthenticationFailureHandler
		implements
		org.springframework.security.web.authentication.AuthenticationFailureHandler {
	private static final String LOGIN_TYPE = "loginType";
	private static final String AJAX_LOGIN = "ajaxLogin";
	
	private String loginUrl;
	
	public AuthenticationFailureHandler(String loginUrl){
		this.loginUrl = loginUrl;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String contextPath = request.getContextPath();
		String loginType = request.getParameter(LOGIN_TYPE);
		
		if (AJAX_LOGIN.equals(loginType)) {
			request.setAttribute("message", new Message(-500, exception.getMessage()));
			request.getRequestDispatcher(contextPath + "/system/authfailure")
			.forward(request, response);
		} else {
			SpringUtil.setSessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
			response.sendRedirect(contextPath + loginUrl);
		}
	}

}
