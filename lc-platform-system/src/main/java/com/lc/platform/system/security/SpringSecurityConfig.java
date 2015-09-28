package com.lc.platform.system.security;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lc.platform.system.service.UserService;

/**
 * SpringSecurity配置信息
 * @author chenjun
 *
 */
@Configuration
@EnableWebMvcSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
	@Autowired
	private UserService userService;
	@Value("${system.login.url:/system/view/login}")
	private String loginUrl;
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	/**
	 * 配置不被拦截的资源信息
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.css", "/**/*.js", "/**/images/**","/**/authfailure","/**/invalidSession");
	}
	
	@Bean
	@Primary
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	/**
	 * 所有的安全认证，比如csrf,logout,login,xss,cache,hsts
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler(loginUrl);
		http.authorizeRequests().anyRequest().authenticated().and()
				.exceptionHandling().accessDeniedPage("/403");
		//http.sessionManagement().invalidSessionUrl("/system/invalidSession");
		http.csrf().disable();//禁用csrf
		http.logout().logoutSuccessUrl(loginUrl);//登陆设置
		http.headers().contentTypeOptions()
		.xssProtection()//xss设置
        .cacheControl()//cache设置
        .httpStrictTransportSecurity();
		 http.formLogin()
		.loginProcessingUrl("/j_spring_security_check")
		.successHandler(authenticationSuccessHandler)//认证处理
		.failureHandler(authenticationFailureHandler)//失败处理
		.loginPage(loginUrl)//登陆页面
		.permitAll();
	}

	/**
	 * 修改认证源来自用户业务接口提供用户信息
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4, new SecureRandom());
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userService;
	}

}
