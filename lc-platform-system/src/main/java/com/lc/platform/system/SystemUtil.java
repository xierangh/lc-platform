package com.lc.platform.system;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.system.domain.User;

/**
 * 系统工具类
 * @author chenjun
 *
 */
public class SystemUtil {
	
	/**
	 * 获取当前登陆者的用户名信息
	 * @return
	 */
	public static String getUserName(){
		User user = getCurrentUser();
		return user==null?"":user.getUsername();
	}
	
	/**
	 * 当前用户是否是超级管理员
	 * @return
	 */
	public static boolean isSuperAdmin(){
		User user = getCurrentUser();
		return user==null?false:user.getSuperAdmin();
	}
	
	/**
	 * 获取当前用户对象
	 * @return
	 */
	public static User getCurrentUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			return (User)principal;
		}
		throw new RuntimeException(MessageUtil.getMessage("14011"));
	}
}
