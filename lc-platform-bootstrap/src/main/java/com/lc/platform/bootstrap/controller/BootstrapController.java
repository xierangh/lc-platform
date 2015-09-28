package com.lc.platform.bootstrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/bootstrap")
public class BootstrapController{
	
	@RequestMapping(value="view/{page:^[a-zA-Z0-9_]+$}",method=RequestMethod.GET)
	public String view(@PathVariable String page)throws Exception{
		return "bootstrap/" + page;
	}

} 
