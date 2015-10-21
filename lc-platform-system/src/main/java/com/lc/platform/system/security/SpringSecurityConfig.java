package com.lc.platform.system.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lc.platform.system.service.UserService;

/**
 * SpringSecurity配置信息
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true,jsr250Enabled=true)
@EnableWebMvcSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
	@Autowired
	private UserService userService;
	@Value("${system.login.url:/system/view/login}")
	private String loginUrl;
	@Value("${spring.security.ignoring:}")
	private String ignoring;
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	/**
	 * 配置不被拦截的资源信息
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		List<String> list = new ArrayList<String>();
		if(StringUtils.isNotBlank(ignoring)){
			for (String item : ignoring.split(",")) {
				list.add(item);
			}
		}
		list.add("/**/*.css");
		list.add("/**/images/**");
		list.add("/**/authfailure");
		list.add("/**/invalidSession");
		web.ignoring().antMatchers(list.toArray(new String[list.size()]));
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
		http.csrf().disable();
		http.logout().logoutSuccessUrl(loginUrl);
		http.headers().contentTypeOptions()
		//.xssProtection()
        .cacheControl()
        .httpStrictTransportSecurity();
		 http.formLogin()
		.loginProcessingUrl("/j_spring_security_check")
		.successHandler(authenticationSuccessHandler)
		.failureHandler(authenticationFailureHandler)
		.loginPage(loginUrl)
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
