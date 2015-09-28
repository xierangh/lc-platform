package com.lc.platform.desktop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 桌面控制器
 * @author chenjun
 *
 */
@Controller
@RequestMapping("/desktop")
public class DesktopController {
	
	private static final String DESKTOP = "desktop/";
	
	/**
	 * 桌面首页处理
	 * @return
	 */
	@RequestMapping
	public String index(){
		return DESKTOP + "index";
	}

}
