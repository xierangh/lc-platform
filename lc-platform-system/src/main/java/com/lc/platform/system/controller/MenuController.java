package com.lc.platform.system.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.dao.Condition;
import com.lc.platform.dao.Operation;
import com.lc.platform.dao.Order;
import com.lc.platform.dao.OrderType;
import com.lc.platform.dao.PageBean;
import com.lc.platform.dao.RelateType;
import com.lc.platform.extjs.ExtUtil;
import com.lc.platform.extjs.JsonReader;
import com.lc.platform.spring.SpringUtil;
import com.lc.platform.system.SystemUtil;
import com.lc.platform.system.domain.Menu;
import com.lc.platform.system.service.MenuService;


@Controller
@RequestMapping(value = "/system/menus")
public class MenuController {
	@Autowired
	private MenuService menuService;
	
	public MenuController(){
	}
	
	@RequestMapping(method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody JsonReader readDesktopMenus(int desktopNumber,String menuId)
			throws Exception {
		Menu menu = menuService.findMenuById(menuId);
		String parentId = "0";
		if(menu != null){
			parentId = menu.getMenuId();
		}
		
		PageBean pageBean = new PageBean();
		pageBean.setRowsPerPage(Integer.MAX_VALUE);
		if(desktopNumber!=-1){
			pageBean.addCondition(new Condition("desktopNumber", desktopNumber, Operation.EQ));
			pageBean.addCondition(new Condition("parentId", parentId, Operation.EQ));
		}else{
			pageBean.addCondition(new Condition("menuType", "dir", Operation.NE));
		}
		pageBean.addCondition(new Condition(true, RelateType.AND, "userId", null, Operation.NU));
		pageBean.addCondition(new Condition(RelateType.OR, "userId", SystemUtil.getCurrentUser().getUserId()
				, Operation.EQ, true));
		pageBean.addOrder(new Order("menuOrder", OrderType.ASC));
		menuService.readMenusByPageInfo(pageBean);
		pageBean.getItems().add(createAddMenu(desktopNumber,menuId));
		if(!"0".equals(parentId)){
			pageBean.getItems().add(createReturnMenu(desktopNumber,menu.getParentId()));
		}
		JsonReader jsonReader = ExtUtil.getJsonReader(pageBean);
		return jsonReader;
	}
	
	
	/**
	 * 创建返回菜单的按钮信息
	 * @return
	 */
	public Menu createReturnMenu(int desktopNumber,String parentId) {
		Menu desktopMenu = new Menu();
		desktopMenu.setImage48(SpringUtil.getContextPath() + "/desktop/images/return.png");
		desktopMenu.setMenuName("返回上级");
		desktopMenu.setMenuId("-2");
		desktopMenu.setDesktopNumber(desktopNumber);
		desktopMenu.setMenuType("return");
		desktopMenu.setMenuOrder(-1);
		desktopMenu.setParentId(parentId);
		return desktopMenu;
	}
	
	
	/**
	 * 创建添加菜单的按钮信息
	 * @return
	 */
	public Menu createAddMenu(int desktopNumber,String parentId) {
		Menu desktopMenu = new Menu();
		desktopMenu.setImage48(SpringUtil.getContextPath() + "/desktop/images/useadd.png");
		desktopMenu.setMenuName("添加应用");
		desktopMenu.setMenuId("-1");
		desktopMenu.setDesktopNumber(0);
		desktopMenu.setMenuVal("desktop.comp.DesktopMenuAdd");
		desktopMenu.setMenuType("add");
		desktopMenu.setParentId(parentId);
		desktopMenu.setMenuOrder(Integer.MAX_VALUE);
		return desktopMenu;
	}
	
	/**
	 * 创建用户自定义菜单信息
	 * @param menu 
	 * @return
	 */
	@RequestMapping(value="create",method={RequestMethod.POST})
	public @ResponseBody Message create(Menu menu){
		menu.setUserId(SystemUtil.getCurrentUser().getUserId());
		menu.setMenuLevel(2);
		menu.setCreateDate(new Date());
		menuService.saveMenu(menu);
		return MessageUtil.message(11002, menu.getMenuId());
	}
	
	/**
	 * 删除用户自定义菜单信息
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public @ResponseBody Message delete(@RequestBody Menu menu)throws Exception{
		menuService.deleteMenu(menu.getMenuId());
		return MessageUtil.message(11003);
	}
	
	/**
	 * 交换菜单的位置
	 * @param destId
	 * @param srcId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="swapMenuOrder",method=RequestMethod.POST)
	public @ResponseBody Message swapMenuOrder(String destId,String srcId)throws Exception{
		menuService.swapMenuOrder(destId,srcId);
		return MessageUtil.message(11004);
	}
	
}
