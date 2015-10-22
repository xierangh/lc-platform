package com.lc.platform.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;

@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	
	ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView = new ModelAndView("exception");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String statusText = ex.getMessage();
		int status = -500;
		if(ex instanceof PlatformException){
			status = ((PlatformException)ex).getCode()*-1;
		}else if(ex instanceof DataIntegrityViolationException){
			statusText = MessageUtil.getMessage("DataIntegrityViolationException");
		}
		Message message = new Message(status, statusText);
		message.setData("");
		try {
			ex.printStackTrace();
			String info = objectMapper.writeValueAsString(message);
			modelAndView.getModel().put("message", info);
		} catch (JsonProcessingException e) {
			modelAndView.getModel().put("message", statusText);
		}
		return modelAndView;
	}
	
}
