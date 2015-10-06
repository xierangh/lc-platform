package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.dao.PageBean;
import com.lc.platform.system.domain.Menu;

/**
 * 菜单服务接口
 * @author chenjun
 *
 */
public interface MenuService {

	/**
	 * 获取所有的菜单信息
	 * @return
	 */
	public List<Menu> findAllMenu();

	public void saveMenu(Menu menu);

	public void readMenusByPageInfo(PageBean pageBean);

	public Menu findMenuById(String menuId);

	public void deleteMenu(String menuId);
	
	/**
	 * 交换菜单位置信息
	 * @param destId
	 * @param srcId
	 */
	public void swapMenuOrder(String destId, String srcId);
	
}
